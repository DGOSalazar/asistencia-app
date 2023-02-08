package com.example.myapplication.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.airbnb.lottie.LottieAnimationView
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySplashBinding
import com.example.myapplication.ui.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setAnimation(mBinding.ivAnimation,R.raw.splash_asistenciagdl_1)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
    private fun setAnimation(iv: LottieAnimationView, animation: Int){
        iv.setAnimation(animation)
        iv.playAnimation()
    }
}