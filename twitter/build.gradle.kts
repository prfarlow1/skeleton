plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    kotlin("plugin.serialization") version "1.8.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    val KTOR_VERSION = "2.2.2"
    implementation("io.ktor:ktor-client-core:$KTOR_VERSION")
    implementation("io.ktor:ktor-client-content-negotiation:$KTOR_VERSION")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$KTOR_VERSION")
    implementation("io.ktor:ktor-client-okhttp:$KTOR_VERSION")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.0")


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
}
