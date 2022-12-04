import fr.bowser.build_src.ProjectConfig

plugins {
    id("com.android.dynamic-feature")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = ProjectConfig.SdkVersions.compileSdkVersion

    defaultConfig {
        minSdk = ProjectConfig.SdkVersions.minSdkVersion
    }

    buildTypes {
        getByName("release") {
            proguardFiles("proguard-rules-instantapp.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    lint {
        abortOnError = false
    }
}

dependencies {
    implementation(project(":base"))

    // Google
    implementation("com.google.android.gms:play-services-instantapps:18.0.1")

    // Other
    implementation("com.google.dagger:dagger:2.42")
    kapt("com.google.dagger:dagger-compiler:2.42")
}

