import org.jetbrains.kotlin.config.KotlinCompilerVersion

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
        release {
            proguardFiles("proguard-rules-app.pro")
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

    sourceSets {
        getByName("main") {
            // Split resources.
            // https://medium.com/google-developer-experts/android-project-structure-alternative-way-29ce766682f0#.sjnhetuhb
            res.srcDirs(
                "src/main/res/widget"
            )
        }
    }
}

dependencies {
    implementation(project(":base"))

    // Kotlin
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))

    // Other
    implementation("com.google.dagger:dagger:2.39")
    kapt("com.google.dagger:dagger-compiler:2.39")
}
