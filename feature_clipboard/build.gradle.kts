plugins {
    id("fr.bowser.android.feature")
}

android {
    namespace = "fr.bowser.feature_clipboard"
}

dependencies {

    implementation(projects.featureString)
}
