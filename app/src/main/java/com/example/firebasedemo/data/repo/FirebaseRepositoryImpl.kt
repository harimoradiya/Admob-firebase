package com.example.firebasedemo.data.repo

import com.example.firebasedemo.data.model.AdData
import com.example.firebasedemo.utils.AdConstants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRepositoryImpl(private val firestore: FirebaseFirestore) : FirebaseRepository {
    override suspend fun fetchAdData(): AdData {

        val doc = firestore.collection("Demo").document("wo0TCCSMtKNSFAEblD1j").get().await()
        return AdData(
            adStatus = doc.getBoolean(AdConstants.AD_STATUS) ?: false,
            bannerAdId = doc.getString(AdConstants.BANNER_AD_ID) ?: "",
            interstitialAdId = doc.getString(AdConstants.INTERSTITIAL_AD_ID) ?: "",
            appOpenAdId = doc.getString(AdConstants.APP_OPEN_AD_ID) ?: "",
            nativeAdId = doc.getString(AdConstants.NATIVE_AD_ID) ?: "",
            rewardedAdId = doc.getString(AdConstants.REWARDED_AD_ID) ?: ""

        )
    }
}