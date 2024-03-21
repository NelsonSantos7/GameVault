package com.example.gamevault

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gamevault.databinding.ActivityMainBinding
import com.example.gamevault.login.Login
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_im_playing, R.id.nav_want_to_playing, R.id.nav_Ive_played, R.id.nav_AddGame
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener(this)

        val headerView = navView.getHeaderView(0)
        headerView.findViewById<ImageView>(R.id.imageView).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            applyNavigationAnimations(R.id.userProfileFragment)
        }
    }

    private fun applyNavigationAnimations(destinationId: Int) {
        val options = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()
        navController.navigate(destinationId, null, options)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        drawerLayout.closeDrawer(GravityCompat.START) // Fechar a barra lateral para todos os itens antes da navegação
        when (item.itemId) {
            R.id.nav_logout -> {
                logout()
                return true
            }
            R.id.nav_home, R.id.nav_im_playing, R.id.nav_want_to_playing, R.id.nav_Ive_played, R.id.nav_AddGame -> {
                applyNavigationAnimations(item.itemId)
                return true
            }
        }
        return true
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences("com.example.gamevault.prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear().apply()
        val loginIntent = Intent(this, Login::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(loginIntent)
        finish()
    }
}
