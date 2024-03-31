package com.pixel.toctalk.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.pixel.toctalk.R
import com.pixel.toctalk.data.utils.FirebaseUtils
import com.pixel.toctalk.ui.auth.AuthHostActivity
import com.pixel.toctalk.ui.home.MainActivity
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(mainLooper).postDelayed({
            lifecycleScope.launch {
                if (FirebaseUtils.isLoggedIn()) {
                    launchToHome()
                } else {
                    launchToAuth()
                }
            }
        }, 2000)
    }

    private fun launchToAuth() {
        startActivity(
            Intent(
                this,
                AuthHostActivity::class.java,
            ),
        )
        finish()
    }

    private fun launchToHome() {
        startActivity(
            Intent(
                this,
                MainActivity::class.java,
            ),
        )
        finish()
    }
}
