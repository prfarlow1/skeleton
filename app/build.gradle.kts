plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.peterfarlow"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.peterfarlow"
        minSdk = 31
        targetSdk = 33
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

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation("androidx.activity:activity-compose:1.6.0")

    implementation("androidx.compose.material3:material3:1.0.0-rc01")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.2.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.lifecycle:lifecycle-process:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.5.1")

    implementation("androidx.startup:startup-runtime:1.1.1")

    implementation("androidx.work:work-runtime-ktx:2.7.1")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.9.1")

    implementation("io.insert-koin:koin-android:3.2.2")
    implementation("io.insert-koin:koin-androidx-compose:3.2.1")
    implementation("io.insert-koin:koin-androidx-workmanager:3.3.0")

    implementation("org.tinylog:tinylog-impl:2.5.0")
    implementation("org.tinylog:tinylog-api-kotlin:2.5.0")
}
