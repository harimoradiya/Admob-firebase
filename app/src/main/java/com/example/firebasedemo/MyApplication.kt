package com.example.firebasedemo

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.firebasedemo.data.repo.FirebaseRepositoryImpl
import com.example.firebasedemo.domain.FetchAdDataUseCase
import com.example.firebasedemo.utils.AdConstants
import com.example.firebasedemo.utils.SharedPreferencesManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class MyApplication: Application() {
    private lateinit var fetchAdDataUseCase: FetchAdDataUseCase
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    val adDataFetchStatus  = MutableLiveData<Boolean>()
    var mInterstitialAd: InterstitialAd? = null
    private var ads_status: String? = null
    private var interstitialAdId: String? = null
    private  lateinit var preferences: SharedPreferences
    private var appOpenManager: AppOpenAd? = null
    override fun onCreate() {
        super.onCreate()

        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@MyApplication) {}
        }

         preferences  = getSharedPreferences("adPrefs", Context.MODE_PRIVATE)
        val firestore = FirebaseFirestore.getInstance()
        sharedPreferencesManager = SharedPreferencesManager(preferences)
        val firebaseRepositoryImpl = FirebaseRepositoryImpl(firestore)
        fetchAdDataUseCase = FetchAdDataUseCase(firebaseRepositoryImpl,sharedPreferencesManager)


        CoroutineScope(Dispatchers.IO).launch {
            try {

                fetchAdDataUseCase.execute()
                adDataFetchStatus.postValue(true)
                val ads_status = preferences.getBoolean(AdConstants.AD_STATUS, false)
                if (ads_status) {
                    interstitialAdId = preferences.getString(AdConstants.INTERSTITIAL_AD_ID, "")
                    Log.e("MyApplication", "Hello i ads $interstitialAdId!!")


                    // Switch to Main thread to load the interstitial ad
                    withContext(Dispatchers.Main) {
                        loadInterstitialAd()
                    }
                }

            }
            catch (e :Exception){
                Log.e("MyApplication", "Failed to fetch ad data ${e.message}",)
                adDataFetchStatus.postValue(false)

            }
        }

        appOpenManager = AppOpenAd(this@MyApplication)

    }


    fun loadInterstitialAd() {
        Log.d("MyApplication", "loadInterstitialAd: ")
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            interstitialAdId ?: "",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    mInterstitialAd!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdClicked() {
                                super.onAdClicked()

                            }

                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()

                            }
                        }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    mInterstitialAd = null

                }
            })
    }

    fun displayInterstitialAd(context: Context?) {
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show((context as Activity?)!!)
            loadInterstitialAd()
        }
    }

}