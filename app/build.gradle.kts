plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.healthscanai"

    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.healthscanai"

        minSdk = 26
        targetSdk = 37

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }

        release {
            isMinifyEnabled = false
            isShrinkResources = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget =
                org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // ---------------------------------------------------------
    // AndroidX
    // ---------------------------------------------------------

    implementation("androidx.core:core-ktx:1.17.0")

    implementation(
        "androidx.lifecycle:lifecycle-runtime-ktx:2.9.3"
    )

    implementation(
        "androidx.activity:activity-compose:1.10.1"
    )

    // ---------------------------------------------------------
    // Compose
    // ---------------------------------------------------------

    implementation(
        platform("androidx.compose:compose-bom:2026.09.00")
    )

    implementation("androidx.compose.ui:ui")

    implementation(
        "androidx.compose.ui:ui-tooling-preview"
    )

    implementation(
        "androidx.compose.material3:material3"
    )

    implementation(
        "androidx.compose.material:material-icons-extended"
    )

    debugImplementation(
        "androidx.compose.ui:ui-tooling"
    )

    // ---------------------------------------------------------
    // ViewModel
    // ---------------------------------------------------------

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.9.3"
    )

    // ---------------------------------------------------------
    // Room
    // ---------------------------------------------------------

    implementation(
        "androidx.room:room-runtime:2.8.5"
    )

    implementation(
        "androidx.room:room-ktx:2.8.5"
    )

    ksp(
        "androidx.room:room-compiler:2.8.5"
    )

    // ---------------------------------------------------------
    // CameraX
    // ---------------------------------------------------------

    implementation(
        "androidx.camera:camera-core:1.6.2"
    )

    implementation(
        "androidx.camera:camera-camera2:1.6.2"
    )

    implementation(
        "androidx.camera:camera-lifecycle:1.6.2"
    )

    implementation(
        "androidx.camera:camera-view:1.6.2"
    )

    // ---------------------------------------------------------
    // ML Kit OCR
    // ---------------------------------------------------------

    implementation(
        "com.google.mlkit:text-recognition:16.0.1"
    )

    // ---------------------------------------------------------
    // Health Connect
    // ---------------------------------------------------------

    implementation(
        "androidx.health.connect:connect-client:1.1.0"
    )

    // ---------------------------------------------------------
    // WorkManager
    // ---------------------------------------------------------

    implementation(
        "androidx.work:work-runtime-ktx:2.11.2"
    )

    // ---------------------------------------------------------
    // Retrofit
    // ---------------------------------------------------------

    implementation(
        "com.squareup.retrofit2:retrofit:3.0.0"
    )

    implementation(
        "com.squareup.retrofit2:converter-gson:3.0.0"
    )

    // ---------------------------------------------------------
    // OkHttp
    // ---------------------------------------------------------

    implementation(
        "com.squareup.okhttp3:okhttp:5.1.0"
    )

    implementation(
        "com.squareup.okhttp3:logging-interceptor:5.1.0"
    )

    // ---------------------------------------------------------
    // Gson
    // ---------------------------------------------------------

    implementation(
        "com.google.code.gson:gson:2.13.2"
    )

    // ---------------------------------------------------------
    // Coroutines
    // ---------------------------------------------------------

    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2"
    )

    // ---------------------------------------------------------
    // Unit tests
    // ---------------------------------------------------------

    testImplementation(
        "junit:junit:4.13.2"
    )

    // ---------------------------------------------------------
    // Android tests
    // ---------------------------------------------------------

    androidTestImplementation(
        "androidx.test.ext:junit:1.3.0"
    )

    androidTestImplementation(
        "androidx.test.espresso:espresso-core:3.7.0"
    )

    androidTestImplementation(
        platform(
            "androidx.compose:compose-bom:2026.09.00"
        )
    )

    androidTestImplementation(
        "androidx.compose.ui:ui-test-junit4"
    )
}
