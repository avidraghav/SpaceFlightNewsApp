package com.example.spaceflightnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
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
    lateinit var toggle : ActionBarDrawerToggle

    val viewModel: AppViewModel by viewModels {
        AppViewModelFactory(application,AppRepository(RetrofitInstance.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
           binding.bottomNavigationView.setupWithNavController(findNavController(R.id.navHostFragment))

          toggle = ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open,R.string.close)
          binding.drawerLayout.addDrawerListener(toggle)
          toggle.syncState()
          supportActionBar?.setDisplayHomeAsUpEnabled(true)

//         toolbar = binding.tbMain
//         toolbar.setTitle(R.string.app_name)
//         setSupportActionBar(toolbar)
         //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navMenu.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item1 -> Toast.makeText(this,"janf",Toast.LENGTH_SHORT).show()
            }
            true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}