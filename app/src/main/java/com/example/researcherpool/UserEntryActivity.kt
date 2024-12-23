package com.example.researcherpool

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.researcherpool.databinding.ActivityUserEntryBinding
import com.example.researcherpool.preference.DataStoreViewModel
import com.example.researcherpool.preference.UserPreference
import com.example.researcherpool.preference.dataStore
import com.example.researcherpool.ui.MainActiviy.MainActivity

class UserEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_user_entry)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_forum_user, R.id.navigation_participant
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val pref = UserPreference.getInstance(application.dataStore)
        val dataStoreViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            DataStoreViewModel::class.java)

        dataStoreViewModel.getUsername().observe(this){username->
            binding.txtUser.text = username
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val startMain = Intent(Intent.ACTION_MAIN)
                startMain.addCategory(Intent.CATEGORY_HOME)
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(startMain)
            }
        })

        binding.btnLogout.setOnClickListener {
            val intentMain = Intent(this, MainActivity::class.java)
            dataStoreViewModel.clearData()
            startActivity(intentMain)
        }
    }
}