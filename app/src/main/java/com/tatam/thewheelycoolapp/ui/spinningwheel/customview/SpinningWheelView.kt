package com.tatam.thewheelycoolapp.ui.spinningwheel.customview

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.tatam.thewheelycoolapp.R
import java.util.ArrayList
import kotlin.math.roundToInt


class SpinningWheelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle), SpinningWheelRotationCallback {

    companion object {
        private const val MIN_COLORS = 3
        private const val ANGLE = 360f
        private const val COLORS_RES = R.array.sample_palette
        private const val TEXT_SIZE = 30F
        private const val TEXT_COLOR = Color.WHITE
        private const val ARROW_COLOR = Color.DKGRAY
        private const val ARROW_SIZE = 40
    }

    @ColorInt
    private var wheelStrokeColor: Int = 0
    private var wheelStrokeWidth: Float = 0F
    private var wheelStrokeRadius: Float = 0F
    private var wheelTextColor: Int = 0
    private var wheelTextSize: Float = 0F
    private var wheelArrowColor: Int = 0
    private var wheelArrowWidth: Float = 0F
    private var wheelArrowHeight: Float = 0F
    private var wheelRotation: WheelRotation? = null
    private lateinit var circle: Circle
    private var angle: Float = 0F
    private var items: List<String>? = null
    private lateinit var points: Array<Point>

    @ColorInt
    private lateinit var colors: IntArray
    private lateinit var onRotationListener: OnRotationListener
    private var onRotationListenerTicket: Boolean = false
    private var onRotation: Boolean = false
    private lateinit var textPaint: Paint
    private lateinit var strokePaint: Paint
    private lateinit var trianglePaint: Paint
    private lateinit var itemPaint: Paint

    init {
        initAttrs(attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        initCircle()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpinningWheelView, 0, 0)
        val colorsResId = typedArray.getResourceId(R.styleable.SpinningWheelView_wheel_colors, 0)
        setColors(colorsResId)
        val wheelStrokeColor = typedArray.getColor(
            R.styleable.SpinningWheelView_wheel_stroke_color,
            ContextCompat.getColor(context, android.R.color.transparent)
        )
        setWheelStrokeColor(wheelStrokeColor)
        val wheelStrokeWidth =
            typedArray.getDimension(R.styleable.SpinningWheelView_wheel_stroke_width, 0f)
        setWheelStrokeWidth(wheelStrokeWidth)
        val itemsResId = typedArray.getResourceId(R.styleable.SpinningWheelView_wheel_items, 0)
        setItems(itemsResId)
        val wheelTextSize = typedArray.getDimension(
            R.styleable.SpinningWheelView_wheel_text_size,
            TEXT_SIZE
        )
        setWheelTextSize(wheelTextSize)
        val wheelTextColor = typedArray.getColor(
            R.styleable.SpinningWheelView_wheel_text_color,
            TEXT_COLOR
        )
        setWheelTextColor(wheelTextColor)
        val wheelArrowColor = typedArray.getColor(
            R.styleable.SpinningWheelView_wheel_arrow_color,
            ARROW_COLOR
        )
        setWheelArrowColor(wheelArrowColor)
        val wheelArrowWidth = typedArray.getDimension(
            R.styleable.SpinningWheelView_wheel_arrow_width, dpToPx(
                ARROW_SIZE
            ).toFloat()
        )
        setWheelArrowWidth(wheelArrowWidth)
        val wheelArrowHeight = typedArray.getDimension(
            R.styleable.SpinningWheelView_wheel_arrow_height, dpToPx(
                ARROW_SIZE
            ).toFloat()
        )
        setWheelArrowHeight(wheelArrowHeight)
        init()
    }

    private fun init() {
        textPaint = Paint()
        textPaint.style = Paint.Style.FILL
        textPaint.color = wheelTextColor
        textPaint.textSize = wheelTextSize
        strokePaint = Paint()
        strokePaint.style = Paint.Style.STROKE
        strokePaint.color = wheelStrokeColor
        strokePaint.strokeWidth = wheelStrokeWidth
        strokePaint.strokeCap = Paint.Cap.ROUND
        trianglePaint = Paint()
        trianglePaint.color = wheelArrowColor
        trianglePaint.style = Paint.Style.FILL_AND_STROKE
        trianglePaint.isAntiAlias = true
        itemPaint = Paint()
        itemPaint.style = Paint.Style.FILL
    }

    private fun setWheelStrokeColor(wheelStrokeColor: Int) {
        this.wheelStrokeColor = wheelStrokeColor
        invalidate()
    }

    private fun setWheelStrokeWidth(wheelStrokeWidth: Float) {
        this.wheelStrokeWidth = wheelStrokeWidth
        initWheelStrokeRadius()
        invalidate()
    }

    private fun initWheelStrokeRadius() {
        wheelStrokeRadius = wheelStrokeWidth / 2
        wheelStrokeRadius = if (wheelStrokeRadius == 0f) 1f else wheelStrokeRadius
    }

    private fun setWheelTextSize(wheelTextSize: Float) {
        this.wheelTextSize = wheelTextSize
        invalidate()
    }

    private fun setWheelTextColor(wheelTextColor: Int) {
        this.wheelTextColor = wheelTextColor
        invalidate()
    }

    private fun setWheelArrowColor(wheelArrowColor: Int) {
        this.wheelArrowColor = wheelArrowColor
        invalidate()
    }

    private fun setWheelArrowWidth(wheelArrowWidth: Float) {
        this.wheelArrowWidth = wheelArrowWidth
        invalidate()
    }

    private fun setWheelArrowHeight(wheelArrowHeight: Float) {
        this.wheelArrowHeight = wheelArrowHeight
        invalidate()
    }

    private fun setColors(colors: IntArray?) {
        this.colors = colors!!
        invalidate()
    }

    private fun setColors(@ArrayRes colorsResId: Int) {
        if (colorsResId == 0) {
            setColors(COLORS_RES)
            return
        }
        val typedArray: IntArray

        if (isInEditMode) {
            val sTypeArray = resources.getStringArray(colorsResId)
            typedArray = IntArray(sTypeArray.size)
            for (i in sTypeArray.indices) {
                typedArray[i] = Color.parseColor(sTypeArray[i])
            }
        } else {
            typedArray = resources.getIntArray(colorsResId)
        }
        if (typedArray.size < MIN_COLORS) {
            setColors(COLORS_RES)
            return
        }
        val colors = IntArray(typedArray.size)
        for (i in typedArray.indices) {
            colors[i] = typedArray[i]
        }
        setColors(colors)
    }

    fun setItemsList(items: List<String>) {
        this.items = items
        initPoints()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initCircle()
        if (hasData() && (points.size != items?.size)) {
            initPoints()
        }

        drawCircle(canvas)
        drawCircleStroke(canvas)
        drawWheel(canvas)
        drawWheelItems(canvas)
        drawTriangle(canvas)
    }

    private fun setItems(@ArrayRes itemsResId: Int) {
        if (itemsResId == 0) {
            return
        }
        val typedArray = resources.getStringArray(itemsResId)
        val items = ArrayList<String>()
        for (i in typedArray.indices) {
            items.add(typedArray[i])
        }
        setItemsList(items)
    }

    private fun drawCircle(canvas: Canvas) {
        canvas.drawCircle(circle.cx, circle.cy, circle.radius, Paint())
    }

    private fun drawCircleStroke(canvas: Canvas) {
        canvas.drawCircle(circle.cx, circle.cy, circle.radius - wheelStrokeRadius, strokePaint)
    }

    private fun drawWheel(canvas: Canvas) {
        if (!hasData()) {
            return
        }

        val cx: Float = circle.cx
        val cy: Float = circle.cy
        val radius: Float = circle.radius
        val endOfRight = cx + radius
        val left = cx - radius + (wheelStrokeRadius * 2)
        val top = cy - radius + (wheelStrokeRadius * 2)
        val right = cx + radius - (wheelStrokeRadius * 2)
        val bottom = cy + radius - (wheelStrokeRadius * 2)

        canvas.rotate(angle, cx, cy)

        val rectF = RectF(left, top, right, bottom)
        var angle = 0F
        items?.forEachIndexed { index, _ ->
            canvas.save()
            canvas.rotate(angle, cx, cy)
            canvas.drawArc(rectF, 0f, ANGLE / items?.size!!, true, getItemPaint(index))
            canvas.restore()
            points[index] = circle.rotate(angle + this.angle, endOfRight, cy)
            angle += ANGLE / items?.size!!
        }
    }

    private fun drawWheelItems(canvas: Canvas) {
        val cx: Float = circle.cx
        val cy: Float = circle.cy
        val radius: Float = circle.radius
        val x = cx - radius + (wheelStrokeRadius * 5)
        val y = cy
        val textWidth = radius - (wheelStrokeRadius * 15)
        val textPaint = TextPaint()
        textPaint.set(this.textPaint)

        var angle = ANGLE / items?.size!! / 2

        items?.forEachIndexed { index, _ ->
            val item = TextUtils
                .ellipsize(
                    items!![index].toString(),
                    textPaint,
                    textWidth,
                    TextUtils.TruncateAt.END
                )
            canvas.save()
            canvas.rotate(angle + 180, cx, cy) // +180 for start from right
            canvas.drawText(item.toString(), x + 40, y + 40, this.textPaint)
            canvas.restore()

            angle += ANGLE / items?.size!!
        }
    }

    private fun drawTriangle(canvas: Canvas) {
        val cx: Float = circle.cx
        val cy: Float = circle.cy
        val radius: Float = circle.radius

        canvas.rotate(-angle, cx, cy)
        drawTriangle(canvas, trianglePaint, cx, cy - radius, wheelArrowWidth, wheelArrowHeight)
    }

    private fun drawTriangle(
        canvas: Canvas,
        paint: Paint,
        x: Float,
        y: Float,
        width: Float,
        height: Float
    ) {
        val halfWidth = width / 2
        val halfHeight = height / 2
        val path = Path()
        path.moveTo(x - halfWidth, y - halfHeight) // Top left
        path.lineTo(x + halfWidth, y - halfHeight) // Top right
        path.lineTo(x, y + halfHeight) // Bottom Center
        path.lineTo(x - halfWidth, y - halfHeight) // Back to top left
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun hasData(): Boolean {
        return items != null && items?.isNotEmpty()!!
    }

    private fun initCircle() {
        val width = if (measuredWidth == 0) width else measuredWidth
        val height = if (measuredHeight == 0) height else measuredHeight

        circle = Circle(width.toFloat(), height.toFloat())
    }


    private fun getItemPaint(position: Int): Paint {
        var i = position % colors.size
        if (items?.size!! - 1 == position && position % colors.size == 0) {
            i = colors.size / 2
        }
        itemPaint.color = colors[i]
        return itemPaint
    }

    private fun initPoints() {
        if (items != null && items?.isNotEmpty()!!) {
            points = Array(items?.size!!) { Point() }
        }
    }

    override fun onRotate(angle: Float) {
        rotate(angle)
    }

    override fun onStop() {
        onRotation = false
        onRotationListener.onStopRotation(getSelectedItem())
    }

    private fun getSelectedItem(): String {
        val itemSize = items?.size!!
        val cx = circle.cx
        points.indices.forEach { i ->
            if (points[i].x <= cx && cx <= points[(i + 1) % itemSize].x) { // validate point x
                return items!![i]
            }
        }
        return "No value"
    }

    fun rotate(angle: Float) {
        this.angle += angle
        this.angle %= ANGLE
        invalidate()
        if (onRotationListenerTicket && angle != 0f) {
            onRotationListener.onRotation()
            onRotationListenerTicket = false
        }
    }

    fun rotate(maxAngle: Float, duration: Long, interval: Long) {
        if (maxAngle == 0F) {
            return
        }
        onRotationListenerTicket = true
        onRotation = true

        if (wheelRotation != null) {
            wheelRotation?.cancel()
        }

        wheelRotation = WheelRotation
            .init(duration, interval)
            .setMaxAngle(maxAngle)
            .setListener(this)
        wheelRotation?.start()
    }

    fun setOnRotationListener(onRotationListener: OnRotationListener) {
        this.onRotationListener = onRotationListener
    }

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

}

