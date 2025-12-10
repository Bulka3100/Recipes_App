plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "2.0.21"
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
    id("com.google.dagger.hilt.android")
}

android {
    // нормально ли таким образом указывать версию?

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
        kotlinOptions {
            jvmTarget = "21"
        }
    
    namespace = "com.example.recipesapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.recipesapp"
        minSdk = 24
        targetSdk = 34
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    val hiltVersion = "2.57.2"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.firebase.crashlytics.buildtools)
    ksp(libs.room.compiler)
    implementation (libs.glide)
    implementation (libs.retrofit2.kotlinx.serialization.converter)
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.retrofit)
    implementation(libs.okhttp3.okhttp)
    implementation(libs.logging.interceptor)
    ksp("com.google.dagger:hilt-android-compiler:$hiltVersion")
    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)

    // Navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

    // Тестирование
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}