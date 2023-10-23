import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.skeleton.android.application)
    alias(libs.plugins.skeleton.android.application.compose)
    alias(libs.plugins.skeleton.android.room)
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")

// Initialize a new Properties() object called keystoreProperties.
val keystoreProperties = Properties()

// Load your keystore.properties file into the keystoreProperties object.
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.peterfarlow.skeleton"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.peterfarlow.skeleton"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {

    // core

    testImplementation(libs.junit4)
    testImplementation(kotlin("test"))

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.play.services.auth)
    /*    implementation(platform("io.insert-koin:koin-bom:3.5.1"))
        implementation("io.insert-koin:koin-android")
        implementation("io.insert-koin:koin-androidx-compose")
        implementation("io.insert-koin:koin-androidx-workmanager")*/
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.tinylog.api.kotlin)
    implementation(libs.tinylog.impl)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.process)
}
