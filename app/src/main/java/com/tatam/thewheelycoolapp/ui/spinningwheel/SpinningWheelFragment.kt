package com.tatam.thewheelycoolapp.ui.spinningwheel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.jinatonic.confetti.CommonConfetti
import com.tatam.thewheelycoolapp.R
import com.tatam.thewheelycoolapp.databinding.FragmentSpinningWheelBinding
import com.tatam.thewheelycoolapp.ui.spinningwheel.customview.OnRotationListener
import com.tatam.thewheelycoolapp.viewmodel.MainViewModel
import java.util.*

class SpinningWheelFragment : Fragment() {

    private lateinit var binding: FragmentSpinningWheelBinding
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpinningWheelBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        viewModel.getWheelItems.observe(viewLifecycleOwner, {
            binding.swView.setItemsList(it.map { it.name })

        })

        binding.btnSpinWheel.setOnClickListener {
            binding.swView.rotate(150f, 4000, 1);
        }

        binding.swView.setOnRotationListener(object : OnRotationListener {
            override fun onRotation() {}
            override fun onStopRotation(item: String) {
                binding.tvWinWord.text = item
                CommonConfetti.rainingConfetti(binding.funView, resources.getIntArray(R.array.sample_palette))
                    .oneShot()
                    .setEmissionDuration(2000)
                    .setEmissionRate(2f)
            }
        })
    }

//    fun randFloat(min: Float, max: Float): Float {
//        val rand = Random()
//        return rand.nextFloat() * (max - min) + min
//    }
}