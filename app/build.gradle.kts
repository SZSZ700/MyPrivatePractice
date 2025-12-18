plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.myfinaltopapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myfinaltopapplication"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.androidx.junit)

    // ============================
    // OkHttp + MockWebServer 4.12.0
    // (אותה גרסה ל-main, unit test, androidTest)
    // ============================
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    testImplementation("com.squareup.okhttp3:okhttp:4.12.0")
    androidTestImplementation("com.squareup.okhttp3:okhttp:4.12.0")

    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")

    // Unit tests (JVM)
    testImplementation(libs.junit)

    // Android instrumented tests (includes AndroidJUnit4)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation("androidx.test:runner:1.6.2")
}
