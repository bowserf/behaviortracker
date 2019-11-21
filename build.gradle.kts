// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        jcenter()
        maven(url = "https://maven.fabric.io/public")
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
        classpath("io.fabric.tools:gradle:1.31.2")
        classpath("com.google.gms:google-services:4.3.3")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.0.0-RC14")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0-rc01")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://maven.fabric.io/public")
    }
}