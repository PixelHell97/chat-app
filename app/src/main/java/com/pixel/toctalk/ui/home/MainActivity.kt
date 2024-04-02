package com.pixel.toctalk.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.pixel.toctalk.R
import com.pixel.toctalk.data.database.UserMdb
import com.pixel.toctalk.data.model.User
import com.pixel.toctalk.data.utils.FirebaseUtils
import com.pixel.toctalk.databinding.ActivityMainBinding
import com.pixel.toctalk.databinding.NavHeaderBinding
import com.pixel.toctalk.ui.auth.AuthHostActivity
import com.pixel.toctalk.ui.extensions.model.MessageDialogModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    // private var listener: NavController.OnDestinationChangedListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.contentMain.homeToolbar)
        lifecycleScope.launch {
            getUserFromDB(FirebaseUtils.getCurrentUserID())
        }
        setUpNav()
    }

    fun setActionbarTitle(title: String) {
        supportActionBar?.title = title
    }
    private fun setUpNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.homeFragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        val drawerLayout = binding.drawerLayout
        val navView = binding.sideNavMenu
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.editAccountFragment,
                R.id.settingsFragment,
            ),
            drawerLayout,
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getUserFromDB(uid: String?) {
        UserMdb
            .getUser(uid) { task ->
                if (task.isSuccessful) {
                    val user = task.result.toObject(User::class.java)
                    val navHeader = NavHeaderBinding.bind(binding.sideNavMenu.getHeaderView(0))
                    navHeader.user = user
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

    private fun showErrorDialog(message: MessageDialogModel) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog
            .setMessage(message.message)
            .setCancelable(message.isCancelable)
        alertDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        setSupportActionBar(null)
    }
}
