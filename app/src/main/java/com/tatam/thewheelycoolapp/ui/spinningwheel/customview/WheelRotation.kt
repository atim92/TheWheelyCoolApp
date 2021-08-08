package com.tatam.thewheelycoolapp.ui.spinningwheel.customview

import android.os.CountDownTimer

class WheelRotation(millisInFuture: Long, countDownInterval: Long) :
    CountDownTimer(millisInFuture, countDownInterval) {

    companion object {
        const val SLOW_FACTOR = 1f/4f

        fun init(millisInFuture: Long, countDownInterval: Long) : WheelRotation {
            return WheelRotation(millisInFuture, countDownInterval)
        }
    }

    private val rotateScaleFactor = 3
    private var maxAngle = 0f
    private var angle = 1F
    private var thresholdSlow : Long = 0
    private var duration : Long = 0
    private lateinit var rotationListener : SpinningWheelRotationCallback

    init {
        thresholdSlow = (millisInFuture * SLOW_FACTOR).toLong()
        duration = millisInFuture
    }

    override fun onTick(millisUntilFinished: Long) {
        if(millisUntilFinished <= thresholdSlow) {
            angle = maxAngle * (millisUntilFinished/duration)
        } else if (angle < maxAngle) {
            rotationListener.onRotate(angle)

            angle *= rotateScaleFactor

            if(angle > maxAngle) {
                angle = maxAngle
            }
        } else {
            rotationListener.onRotate(angle)
        }
    }

    override fun onFinish() {
        rotationListener.onStop()
    }

    fun setListener(listner : SpinningWheelRotationCallback)  : WheelRotation{
        this.rotationListener = listner
        return this
    }

    fun setMaxAngle(maxAngle : Float) : WheelRotation {
        this.maxAngle = maxAngle
        return this
    }
}