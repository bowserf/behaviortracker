import org.jetbrains.kotlin.config.KotlinCompilerVersion
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

    lintOptions {
        isAbortOnError = false
    }
}

dependencies {
    implementation(project(":base"))

    // Kotlin
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))

    // Google
    implementation("com.google.android.gms:play-services-instantapps:17.0.0")

    // Other
    implementation("com.google.dagger:dagger:2.39")
    kapt("com.google.dagger:dagger-compiler:2.39")
}

