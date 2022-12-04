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
        targetSdk = ProjectConfig.SdkVersions.targetSdkVersion
    }

    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    // AndroidX
    implementation("com.google.android.play:core:1.10.3")
}
