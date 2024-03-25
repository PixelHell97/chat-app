package com.pixel.toctalk.ui.home

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.pixel.toctalk.Constants
import com.pixel.toctalk.R
import com.pixel.toctalk.data.model.User
import com.pixel.toctalk.databinding.ActivityMainBinding
import com.pixel.toctalk.databinding.NavHeaderBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var ownUser: User
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    // private var listener: NavController.OnDestinationChangedListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.contentMain.homeToolbar)
        ownUser = getMyUser()
        val navHeader = NavHeaderBinding.bind(binding.sideNavMenu.getHeaderView(0))
        navHeader.user = ownUser
        setUpNav()
    }

    private fun getMyUser(): User {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.PARSE_USER, User::class.java) ?: User()
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Constants.PARSE_USER) ?: User()
        }
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
                R.id.nav_account,
            ),
            drawerLayout,
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        
        return super.onCreateOptionsMenu(menu)
    }*/
}
