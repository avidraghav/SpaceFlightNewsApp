package com.raghav.spacedawn.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.raghav.spacedawn.R
import com.raghav.spacedawn.databinding.ActivityMainBinding
import com.raghav.spacedawn.db.ReminderDatabase
import com.raghav.spacedawn.repository.AppRepository

class MainActivity : AppCompatActivity(){
    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    val viewModel: AppViewModel by viewModels {
        AppViewModelFactory(
            application,
            AppRepository(ReminderDatabase(this))
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       binding.bottomNavigationView.setupWithNavController(findNavController(R.id.navHostFragment))


        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navMenu.itemIconTintList = null;
        binding.navMenu.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.rate_on_playstore ->
                    try {
                        startActivity(
                            Intent(
                                ACTION_VIEW,
                                Uri.parse("market://details?id=$packageName")
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                            )
                        )
                    }
                R.id.github_repo ->
                    startActivity(
                        Intent(
                            ACTION_VIEW,
                            Uri.parse("https://github.com/avidraghav/SpaceFlightNewsApp")
                        )
                    )

                R.id.qsol_playstore_link -> startActivity(
                    Intent(
                        ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.application.kurukshetrauniversitypapers")
                    )
                )
            }

            true
        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }





}