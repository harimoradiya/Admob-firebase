package com.example.firebasedemo.domain

import android.content.SharedPreferences
import com.example.firebasedemo.data.repo.FirebaseRepository
import com.example.firebasedemo.utils.SharedPreferencesManager

class FetchAdDataUseCase(
    private val firebaseRepository: FirebaseRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) {

    suspend fun execute(){
        val adData = firebaseRepository.fetchAdData()
        sharedPreferencesManager.saveAdData(adData)
    }
}