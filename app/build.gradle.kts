plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.serialization)
    alias(libs.plugins.secrets.gradle)
}

android {
    namespace = "com.kristianskokars.tasky"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kristianskokars.tasky"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // Flag to enable support for the new language APIs, required for Compose DatePicker library
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
    // Android
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.splashscreen)

    // Navigation
    implementation(libs.compose.destinations)
    implementation(libs.compose.destinations.animations)
    ksp(libs.compose.destinations.ksp)

    // Dependency Injection
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    // Logging
    implementation(libs.timber)

    // Result
    implementation(libs.kotlin.result)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.logging.interceptor)

    // Local Data
    implementation(libs.androidx.datastore.preferences)

    // Time
    implementation(libs.kotlinx.datetime)

    // Coil
    implementation(libs.coil.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)

    // Date Time Picker
    implementation(libs.compose.datetime)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Fonts
    implementation(libs.androidx.ui.text.google.fonts)

    // Debug
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // Required desugaring for Compose DateTime picker to support API < 26
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}
