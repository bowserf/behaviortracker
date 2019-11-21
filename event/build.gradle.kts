import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(ProjectConfig.SdkVersions.compileSdkVersion)

    defaultConfig {
        minSdkVersion(ProjectConfig.SdkVersions.minSdkVersion)
        targetSdkVersion(ProjectConfig.SdkVersions.targetSdkVersion)
        versionCode = 1
        versionName = "1"
    }
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))

    // Firebase
    implementation("com.google.firebase:firebase-analytics:17.2.1")

    // Other
    implementation("com.google.dagger:dagger:2.14.1")
    kapt("com.google.dagger:dagger-compiler:2.14.1")
}
