package com.example.firebasedemo.utils

import android.content.SharedPreferences
import com.example.firebasedemo.data.model.AdData

class SharedPreferencesManager(private val preferences: SharedPreferences) {

    fun saveAdData(adData: AdData){
        preferences.edit()
            .putBoolean(AdConstants.AD_STATUS, adData.adStatus)
            .putString(AdConstants.BANNER_AD_ID, adData.bannerAdId)
            .putString(AdConstants.INTERSTITIAL_AD_ID, adData.interstitialAdId)
            .putString(AdConstants.APP_OPEN_AD_ID, adData.appOpenAdId)
            .putString(AdConstants.NATIVE_AD_ID, adData.nativeAdId)
            .putString(AdConstants.REWARDED_AD_ID, adData.rewardedAdId)
            .apply()
    }

    fun getAdData() :AdData{
        return AdData(
            adStatus = preferences.getBoolean(AdConstants.AD_STATUS,false),
            bannerAdId = preferences.getString(AdConstants.BANNER_AD_ID, "") ?: "",
            interstitialAdId = preferences.getString(AdConstants.INTERSTITIAL_AD_ID, "") ?: "",
            appOpenAdId = preferences.getString(AdConstants.APP_OPEN_AD_ID, "") ?: "",
            nativeAdId = preferences.getString(AdConstants.NATIVE_AD_ID, "") ?: "",
            rewardedAdId = preferences.getString(AdConstants.REWARDED_AD_ID, "") ?: ""
        )
    }

}
