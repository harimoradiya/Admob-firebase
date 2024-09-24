package com.example.firebasedemo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedemo.databinding.ActivityMainBinding
import com.example.firebasedemo.databinding.ActivitySplashBinding
import com.example.firebasedemo.utils.AdConstants
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var consentInformation: ConsentInformation
    private val TAG = SplashActivity::class.simpleName
    private lateinit var binding: ActivitySplashBinding

    private  lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val application = application as MyApplication
        preferences  = getSharedPreferences("adPrefs", Context.MODE_PRIVATE)

        application.adDataFetchStatus.observe(this, { isFetched ->
            Log.d(TAG, "onCreate: isFetched - $isFetched")
            if (isFetched) {
                //Appopen
                Log.d(TAG, "onCreate: calling requestConsentFrom - ")
                requestConsentFrom()


            } else {
                //move
                Log.d(TAG, "onCreate: move to next screen - ")
                moveToNextScreen()
            }
        })

    }



    private fun requestConsentFrom() {
        // Create a ConsentRequestParameters object.


        val params = ConsentRequestParameters
            .Builder()
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {
                Log.d(TAG, "onCreate: loadAndShowConsentFormIfRequired - ")
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    this@SplashActivity
                ) {
                    // Consent gathering failed.
                    Log.d(TAG, "requestConsentFrom: Showing")

                    // Consent has been gathered.
                    if (consentInformation.canRequestAds()) {

                        appOpenAdLoading()
                    }
                }
            },
            {

                Log.d(TAG, "onCreate: Failed - ")

                // Consent gathering failed.
                appOpenAdLoading()

            })


    }








    private fun appOpenAdLoading() {
        Log.d(TAG,"Showing appopen ad")

        CoroutineScope(Dispatchers.Main).launch {
            delay(500L)

            val adStatus = preferences.getBoolean(AdConstants.AD_STATUS,false)
            if(adStatus){

                val  appOpen_Ad = preferences.getString(AdConstants.APP_OPEN_AD_ID,"")
                val loadCallback: AppOpenAd.AppOpenAdLoadCallback =
                    object : AppOpenAd.AppOpenAdLoadCallback() {
                        override fun onAdLoaded(ad: AppOpenAd) {
                            val fullScreenContentCallback: FullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        Log.d(TAG, "onAdDismissedFullScreenContent: ")
                                        moveToNextScreen()


                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                        Log.d(TAG, "onAdFailedToShowFullScreenContent: ")

                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        Log.d(TAG, "onAdShowedFullScreenContent: ")
                                    }
                                }
                            ad.fullScreenContentCallback = fullScreenContentCallback
                            ad.show(this@SplashActivity)

                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            Log.d(TAG, "error in loading")
                            moveToNextScreen()
                        }
                    }
                val request: AdRequest = AdRequest.Builder().build()
                AppOpenAd.load(
                    this@SplashActivity, appOpen_Ad!!, request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback
                )
            }
            else{
                moveToNextScreen()
            }
        }

    }

    private fun moveToNextScreen(count : Int = 0) {
        var c =count +1
        Log.d(TAG, "moveToNextScreen: $c")
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()  // Ensure splash screen activity is not in the back stack
    }
}