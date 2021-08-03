package me.vanjavk.isa_shows_app_vanjavk.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animateTriangle()

        with(binding.showsText) {
            alpha = 0f
        }

        Handler(Looper.getMainLooper()).postDelayed({
            animateTitle1()
            Handler (Looper.getMainLooper()).postDelayed({
                animateTitle2()
            }, 400)
        }, 1200)
        Handler(Looper.getMainLooper()).postDelayed({
            me.vanjavk.isa_shows_app_vanjavk.SplashFragmentDirections.actionSplashToLogin()
                .let { findNavController().navigate(it) }
        }, 2000)
    }

    private fun animateTitle1() {
        with(binding.showsText) {
            alpha = 1f
            animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .setDuration(400)
                .setInterpolator(OvershootInterpolator())
                .start()
        }

    }

    private fun animateTitle2() {
        with(binding.showsText) {
            scaleX = 1.5f
            scaleY = 1.5f
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(400)
                .setInterpolator(OvershootInterpolator())
                .start()
        }

    }

    private fun animateTriangle() {
        with(binding.triangleIcon) {
            translationY = -1000f
            animate()
                .translationY(0f)
                .setDuration(1200)
                .setInterpolator(BounceInterpolator())
                .start()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}