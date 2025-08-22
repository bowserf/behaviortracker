// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // https://proandroiddev.com/stop-using-gradle-buildsrc-use-composite-builds-instead-3c38ac7a2ab3
    id("fr.bowser.build_src.projectconfig")
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.androidx.navigation.safeargs) apply false
    alias(libs.plugins.ksp) apply false
}

subprojects {
    // KtLint - Static code analysis
    // https://github.com/pinterest/ktlint/releases
    val ktlint by configurations.creating

    dependencies {
        ktlint("com.pinterest.ktlint:ktlint-cli:1.0.1")
    }

    tasks.register<JavaExec>("ktlint") {
        group = "verification"
        description = "Check Kotlin code style for project $name."
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        args("src/**/*.kt")
    }
    tasks.register<JavaExec>("ktformat") {
        group = "verification"
        description = "Format kotlin code for project $name."
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        args("-F", "src/**/*.kt")
    }
}
