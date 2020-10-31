// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
        classpath("com.google.gms:google-services:4.3.4")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.14.2")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0-rc01")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.3.0")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}