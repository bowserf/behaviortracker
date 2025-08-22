plugins {
    id("fr.bowser.android.feature")
}

android {
    namespace = "fr.bowser.feature.billing"
}

dependencies {
    implementation(libs.billing)
}
