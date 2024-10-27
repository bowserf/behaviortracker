plugins {
    id("fr.bowser.android.feature")
}

android {
    namespace = "fr.bowser.feature.billing"
}

dependencies {
    implementation("com.android.billingclient:billing:7.0.0")
}
