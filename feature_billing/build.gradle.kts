import fr.bowser.build_src.ProjectConfig

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = ProjectConfig.SdkVersions.compileSdkVersion
    namespace = "fr.bowser.feature.billing"

    defaultConfig {
        minSdk = ProjectConfig.SdkVersions.minSdkVersion
        targetSdk = ProjectConfig.SdkVersions.targetSdkVersion
    }
}

dependencies {
    implementation("com.android.billingclient:billing:5.0.0")
}
