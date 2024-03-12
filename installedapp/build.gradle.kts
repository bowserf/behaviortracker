import fr.bowser.build_src.ProjectConfig

plugins {
    id("com.android.dynamic-feature")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = ProjectConfig.SdkVersions.compileSdkVersion
    namespace = "fr.bowser.behaviortracker.app"

    defaultConfig {
        minSdk = ProjectConfig.SdkVersions.minSdkVersion
    }

    buildTypes {
        release {
            proguardFiles("proguard-rules-app.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    lint {
        abortOnError = false
    }

    sourceSets {
        getByName("main") {
            // Split resources.
            // https://medium.com/google-developer-experts/android-project-structure-alternative-way-29ce766682f0#.sjnhetuhb
            res.setSrcDirs(
                listOf(
                    "src/main/res/widget"
                )
            )
        }
    }
}

dependencies {
    implementation(project(":base"))

    // Other
    implementation("com.google.dagger:dagger:2.46.1")
    kapt("com.google.dagger:dagger-compiler:2.46.1")
}
