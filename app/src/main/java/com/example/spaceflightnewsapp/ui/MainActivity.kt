package com.example.spaceflightnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.databinding.ActivityMainBinding
import com.example.spaceflightnewsapp.network.RetrofitInstance

import com.example.spaceflightnewsapp.repository.AppRepository

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var toolbar : Toolbar

    val viewModel: AppViewModel by viewModels {
        AppViewModelFactory(AppRepository(RetrofitInstance.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
           binding.bottomNavigationView.setupWithNavController(findNavController(R.id.navHostFragment))

         toolbar = binding.tbMain
         toolbar.setTitle(R.string.app_name)
         setSupportActionBar(toolbar)
    }
}