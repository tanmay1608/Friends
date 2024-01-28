# FRIENDS Readme

## Overview

Friends is a native Android application that provides a platform for one-to-one messaging, group messaging, and user profile creation. The app focuses on a simple and intuitive user interface to facilitate seamless communication between users.

## Features

1. **One-to-One Messaging:** Users can easily connect and chat privately with each other through one-to-one messaging.

2. **Group Messaging:** Create and join groups to engage in group conversations. Share messages with multiple users simultaneously.

3. **User Profiles:** Users can create and personalize their profiles, adding details such as a profile picture and other relevant information.

4. **Phone Number Validation using OTP:** To ensure the security of user accounts, ChatApp implements phone number validation using OTP (One-Time Password). Users will receive a unique OTP to verify their phone number during the registration process.

## Screenshots

Here are some screenshots from ChatApp to give you a glimpse of the user interface:

| Login Screen | Verification Screen | One-to-One Messaging | Group Messaging | User Profile |
|--------------|-------------|-----------------------|------------------|--------------|
| ![Login Screen](app/images/ss3.jpeg) | ![Home Screen](app/images/ss1.jpeg) | ![One-to-One Messaging](app/images/ss2.jpeg) | ![Group Messaging](app/images/ss5.jpeg) | ![User Profile](app/images/ss4.jpeg) |


## Dependencies Used

```groovy
dependencies {

    // AndroidX
    implementation 'androidx.appcompat:appcompat:1.4.1'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.2'
    implementation 'androidx.navigation:navigation-fragment:2.3.5'
    implementation 'androidx.navigation:navigation-ui:2.3.5'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'

    // Firebase
    implementation 'com.google.firebase:firebase-auth:21.0.6'
    implementation 'com.google.firebase:firebase-database:20.0.5'
    implementation 'com.google.firebase:firebase-firestore:24.2.0'
    implementation 'com.google.firebase:firebase-storage:20.0.1'
    implementation 'com.google.firebase:firebase-messaging:23.0.6'

    // Testing
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'

    // Material Design
    implementation 'com.google.android.material:material:1.4.0-beta01'

    // Country Picker Library
    implementation 'com.hbb20:ccp:2.5.1'

    // Picasso
    implementation 'com.squareup.picasso:picasso:2.8'

    // Firestore
    implementation 'com.firebaseui:firebase-ui-firestore:4.1.0'

    // Lottie
    implementation 'com.airbnb.android:lottie:3.4.0'
}
