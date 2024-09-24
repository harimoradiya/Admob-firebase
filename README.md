# Firebase AdMob Integration in Android

This Android project integrates Google AdMob ads (banner, interstitial, app open, native, rewarded ads) with Firebase Firestore for remote ad configuration management. The project leverages Kotlin Coroutines, Clean Architecture, and SharedPreferences for efficient data fetching and storage. 

## Features
- Fetch AdMob configuration from Firebase Firestore (e.g., ad status, banner ad ID).
- Store and retrieve ad configuration using `SharedPreferences`.
- Display ads (banner, interstitial, app open) based on fetched data.
- Show splash screen until ad data is fetched.
- Use Kotlin coroutines for background tasks like data fetching and ad loading.

## Architecture
This project follows Clean Architecture principles to ensure separation of concerns and scalability. The key components include:
- **UseCases**: Encapsulate the logic for fetching ad data from Firebase Firestore.
- **Repository**: Handles data sources like Firebase and `SharedPreferences`.
- **ViewModel**: Manages UI-related data and interacts with the UseCases.

## Key Technologies
- **Firebase Firestore**: Remote backend for storing and retrieving AdMob configuration data.
- **AdMob SDK**: Displays various types of ads in the app.
- **Kotlin Coroutines**: Handles asynchronous tasks like fetching ad data.
- **SharedPreferences**: Caches ad data locally for persistence across app restarts.
- **Clean Architecture**: Ensures separation of concerns.

## Setup and Configuration
1. **AdMob Integration**: 
   - Add your AdMob App ID in the `AndroidManifest.xml`.
   ```xml
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="your-admob-app-id" />
