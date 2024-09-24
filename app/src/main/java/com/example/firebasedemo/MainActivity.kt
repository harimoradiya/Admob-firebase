package com.example.firebasedemo

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasedemo.databinding.ActivityMainBinding
import com.example.firebasedemo.domain.FetchAdDataUseCase
import com.example.firebasedemo.utils.SharedPreferencesManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val preferences = getSharedPreferences("adPrefs", Context.MODE_PRIVATE)
        sharedPreferencesManager = SharedPreferencesManager(preferences)

        val adData = sharedPreferencesManager.getAdData()

        binding.tvDemo.text = adData.toString()

    }
}