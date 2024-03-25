package com.pixel.toctalk.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.pixel.toctalk.Constants
import com.pixel.toctalk.R
import com.pixel.toctalk.data.database.MyDatabase
import com.pixel.toctalk.data.model.User
import com.pixel.toctalk.data.utils.FirebaseUtils
import com.pixel.toctalk.ui.auth.AuthHostActivity
import com.pixel.toctalk.ui.extensions.model.MessageDialogModel
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
                    getUserFromDB(FirebaseUtils.getCurrentUserID())
                } else {
                    launchToAuth()
                }
            }
        }, 2000)
    }

    private fun getUserFromDB(uid: String?) {
        MyDatabase
            .getUser(uid) { task ->
                if (task.isSuccessful) {
                    val user = task.result.toObject(User::class.java)
                    launchToHome(user!!)
                } else {
                    showErrorDialog(
                        MessageDialogModel(
                            message = task.exception?.localizedMessage
                                ?: resources.getString(R.string.login_failed),
                            posActionName = resources.getString(R.string.login_again),
                            posAction = { launchToAuth() },
                        ),
                    )
                }
            }
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

    private fun launchToHome(user: User) {
        startActivity(
            Intent(
                this,
                MainActivity::class.java,
            ).putExtra(Constants.PARSE_USER, user),
        )
        finish()
    }

    private fun showErrorDialog(message: MessageDialogModel) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog
            .setMessage(message.message)
            .setCancelable(message.isCancelable)
        alertDialog.show()
    }
}
