package com.tatam.thewheelycoolapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.tatam.thewheelycoolapp.databinding.ActivityMainBinding

import com.tatam.thewheelycoolapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}