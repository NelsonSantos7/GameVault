package com.example.gamevault

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
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
import com.example.gamevault.firebase.FirebaseHelper
import com.example.gamevault.login.Login
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseHelper: FirebaseHelper

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

        sharedPreferences = getSharedPreferences("com.example.gamevault.prefs", MODE_PRIVATE)
        firebaseHelper = FirebaseHelper()

        updateNavHeader()
        setupProfileImageClickListener()
    }

    private fun updateNavHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.textViewName)
        val userEmailTextView = headerView.findViewById<TextView>(R.id.textViewEmail)
        val profileImageView: CircleImageView = headerView.findViewById(R.id.imageView)

        userNameTextView.text = sharedPreferences.getString("USERNAME", "Usuário Padrão")
        userEmailTextView.text = sharedPreferences.getString("USER_EMAIL", "Email Padrão")
        sharedPreferences.getString("USER_PROFILE_IMAGE_URI", null)?.let {
            profileImageView.setImageURI(Uri.parse(it))
        }
    }

    private fun setupProfileImageClickListener() {
        binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.imageView).setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
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
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return when (item.itemId) {
            R.id.nav_logout -> {
                logout()
                true
            }
            else -> {
                applyNavigationAnimations(item.itemId)
                true
            }
        }
    }

    private fun logout() {
        firebaseHelper.logoutUser()
        sharedPreferences.edit().clear().apply()
        val loginIntent = Intent(this, Login::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(loginIntent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        updateNavHeader()
    }
}
