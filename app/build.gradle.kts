import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")

// Initialize a new Properties() object called keystoreProperties.
val keystoreProperties = Properties()

// Load your keystore.properties file into the keystoreProperties object.
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.peterfarlow"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.peterfarlow.skeleton"
        minSdk = 31
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "web_client_id",
            "\"${keystoreProperties.getProperty("webClientId")}\""
        )
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
            storeFile = file(keystoreProperties.getProperty("storeFile"))
            storePassword = keystoreProperties.getProperty("storePassword")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

dependencies {

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")

    implementation("androidx.activity:activity-compose:1.6.1")

    implementation("androidx.compose.material3:material3:1.0.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.3.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.lifecycle:lifecycle-process:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.5.1")

    implementation("androidx.navigation:navigation-compose:2.5.3")

    implementation("androidx.startup:startup-runtime:1.1.1")

    implementation("androidx.work:work-runtime-ktx:2.7.1")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")

    implementation("io.insert-koin:koin-android:3.3.0")
    implementation("io.insert-koin:koin-androidx-compose:3.4.2")
    implementation("io.insert-koin:koin-androidx-workmanager:3.3.0")

    implementation("org.tinylog:tinylog-impl:2.5.0")
    implementation("org.tinylog:tinylog-api-kotlin:2.5.0")

    implementation("com.google.android.gms:play-services-auth:20.3.0")

    implementation("androidx.datastore:datastore-preferences:1.1.0-alpha01")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")
}
