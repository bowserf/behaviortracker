import fr.bowser.build_src.ProjectConfig

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = ProjectConfig.SdkVersions.compileSdkVersion
    namespace = "fr.bowser.feature_review"

    defaultConfig {
        minSdk = ProjectConfig.SdkVersions.minSdkVersion
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {

    // AndroidX
    implementation("com.google.android.play:review-ktx:2.0.1")
}
