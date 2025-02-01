plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt")
}

android {
    namespace = "com.awesome.notes"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.awesome.notes"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {


    implementation("androidx.room:room-runtime:2.6.0")  // Replace with your current version
    kapt("androidx.room:room-compiler:2.6.0")           // Kotlin annotation processor

    // Add this line for Room coroutine support
    implementation("androidx.room:room-ktx:2.6.0")      // Room coroutine support

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")  // LiveData support
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")  // ViewModel sup

    implementation ("androidx.fragment:fragment-ktx:1.5.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}