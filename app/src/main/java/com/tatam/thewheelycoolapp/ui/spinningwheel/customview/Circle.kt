package com.tatam.thewheelycoolapp.ui.spinningwheel.customview

import android.graphics.Matrix
import android.graphics.Point
import android.util.Log

class Circle(width : Float, height : Float) {

    var cx = 0f
    var cy = 0f
    var radius = 0f
    private var matrix : Matrix = Matrix()

    init {
        cx = width/2f
        cy = height/2f
        radius = Math.min(cx, cy)
    }

    fun contains(x : Float, y : Float) : Boolean{
        val tempx = cx - x
        val tempy = cy - y
        return tempx * tempx + tempy * tempy <= radius * radius
    }

    //Performing rotate around rectangular center
    fun rotate(angle : Float, x : Float, y : Float) : Point {
        matrix.setRotate(angle, cx, cy)

        var pts = FloatArray(2)
        pts[0] = x
        pts[1] = y

        // Changing coordinates using this call
        matrix.mapPoints(pts)

        return Point(pts[0].toInt(), pts[1].toInt())
    }

}