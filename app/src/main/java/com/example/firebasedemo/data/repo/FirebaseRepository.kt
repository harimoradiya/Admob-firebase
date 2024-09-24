package com.example.firebasedemo.data.repo

import com.example.firebasedemo.data.model.AdData


interface FirebaseRepository {
    suspend fun fetchAdData(): AdData

}