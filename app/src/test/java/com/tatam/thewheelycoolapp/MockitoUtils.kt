package com.tatam.thewheelycoolapp

import org.mockito.Mockito

class MockitoUtils {
    inline fun <reified T> mockClass(): T = Mockito.mock(T::class.java)
}