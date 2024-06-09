// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.4.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
        classpath("com.google.gms:google-services:4.3.14")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
    plugins {
        id("com.google.devtools.ksp") version "2.0.0-1.0.22" apply false
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    // https://proandroiddev.com/stop-using-gradle-buildsrc-use-composite-builds-instead-3c38ac7a2ab3
    id("fr.bowser.build_src.projectconfig")
}

subprojects {
    // KtLint - Static code analysis
    // https://github.com/pinterest/ktlint/releases
    val ktlint by configurations.creating

    dependencies {
        // KtLint - Static code analysis
        // https://github.com/pinterest/ktlint/releases
        ktlint("com.pinterest:ktlint:0.49.1") {
            attributes {
                attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
            }
        }
    }

    // KtLint - Static code analysis
    // https://github.com/pinterest/ktlint/releases
    tasks.register<JavaExec>("ktlint") {
        group = "verification"
        description = "Check Kotlin code style."
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        args("--android", "src/**/*.kt")
    }

    // KtLint - Static code format
    // https://github.com/pinterest/ktlint/releases
    tasks.register<JavaExec>("ktformat") {
        group = "verification"
        description = "Check Kotlin code style."
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        args("--android", "-F", "src/**/*.kt")
    }
}
