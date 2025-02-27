import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.firebaseCrashlytics)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.pixel.toctalk"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pixel.toctalk"
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
                "proguard-rules.pro",
            )
        }
        getByName("release") {
            // Add this extension
            configure<CrashlyticsExtension> {
                // Enable processing and uploading of native symbols to Firebase servers.
                // By default, this is disabled to improve build speeds.
                // This flag must be enabled to see properly-symbolicated native
                // stack traces in the Crashlytics dashboard.
                nativeSymbolUploadEnabled = true
                unstrippedNativeLibsDir = "build/intermediates/merged_native_libs/release/out/lib"
            }
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
        dataBinding = true
    }
    buildToolsVersion = "35.0.0"
    ndkVersion = "28.0.12674087 rc2"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // LeakCanary
    // debugImplementation(libs.leakcanary.android)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.firebase.ui.storage)
    implementation(libs.firebase.ui.firestore)

    // glide
    implementation(libs.glide)

    // viewPager2
    implementation(libs.androidx.viewpager2)

    // imagePicker
    implementation(libs.imagepicker)

    // touchImageView

    implementation(libs.touch.imageview)

    // koin

    implementation(libs.bundles.koin)
}
