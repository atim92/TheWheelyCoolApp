package com.tatam.thewheelycoolapp.ui.spinningwheel.customview

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class CircleTest {

    lateinit var SUT : Circle

    @Before
    fun setUp() {
        var width = 10f
        var height = 10f
        SUT = Circle(width, height)
    }

    @Test
    fun contains_coordinates_inside_area_success_check() {
        val testPointX = 5f
        val testPointY = 5f
        assertEquals(SUT.contains(testPointX, testPointY), true)
    }

    @Test
    fun contains_coordinates_outside_area_success_check() {
        val testPointX = 12f
        val testPointY = 12f
        assertEquals(SUT.contains(testPointX, testPointY), false)
    }

    @Test
    fun contains_coordinates_on_border_success_check() {
        val testPointX = 10f
        val testPointY = 10f
        assertEquals(SUT.contains(testPointX, testPointY), false)
    }

    @Test
    fun rotate() {
        val testPoint = FloatArray(2)
        testPoint[0] = 5f
        testPoint[1] = 5f
        val newPoint = SUT.rotate(90f, 5f, 5f)
        assertEquals(newPoint, testPoint)

    }
}