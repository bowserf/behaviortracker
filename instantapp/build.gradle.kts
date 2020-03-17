import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.dynamic-feature")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(ProjectConfig.SdkVersions.compileSdkVersion)
    buildToolsVersion(ProjectConfig.SdkVersions.buildToolsVersion)

    defaultConfig {
        minSdkVersion(ProjectConfig.SdkVersions.minSdkVersion)
        targetSdkVersion(ProjectConfig.SdkVersions.targetSdkVersion)
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

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    lintOptions {
        isAbortOnError = false
    }

    sourceSets {
        getByName("main") {
            // Split resources.
            // https://medium.com/google-developer-experts/android-project-structure-alternative-way-29ce766682f0#.sjnhetuhb
            res.srcDirs(
                    "src/main/res/home"
            )
        }
    }
}

dependencies {
    implementation(project(":base"))

    // Kotlin
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))

    // Google
    implementation("com.google.android.gms:play-services-instantapps:17.0.0")

    // Other
    implementation("com.google.dagger:dagger:2.14.1")
    kapt("com.google.dagger:dagger-compiler:2.14.1")
}

