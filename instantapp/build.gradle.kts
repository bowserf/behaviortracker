import io.gitlab.arturbosch.detekt.detekt
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    id("io.fabric")
    kotlin("kapt")
    id("io.gitlab.arturbosch.detekt")
}

val ktlint by configurations.creating

val buildType: String? by project

android {
    compileSdkVersion(ProjectConfig.SdkVersions.compileSdkVersion)
    buildToolsVersion(ProjectConfig.SdkVersions.buildToolsVersion)

    defaultConfig {
        applicationId = "fr.bowser.time"
        minSdkVersion(ProjectConfig.SdkVersions.minSdkVersion)
        targetSdkVersion(ProjectConfig.SdkVersions.targetSdkVersion)
        versionCode = ProjectConfig.SdkVersions.versionCodeInstantApp
        versionName = ProjectConfig.SdkVersions.versionName
    }

    signingConfigs {
        SigningData.of(project.rootProject.properties("signing.properties"))?.let {
            create("release") {
                storeFile = file(it.storeFile)
                storePassword = it.storePassword
                keyAlias = it.keyAlias
                keyPassword = it.keyPassword
            }
        }
    }

    buildTypes {
        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles("proguard-rules.pro")

            signingConfig = signingConfigs.findByName("release")
        }
        getByName("debug") {
            versionNameSuffix = ".dev"
        }
        create("ua") {
            initWith(getByName("debug"))
            versionNameSuffix = ".ua"
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

    // Static analyzer
    ktlint("com.github.shyiko:ktlint:0.31.0")
}



// detect configuration
detekt {
    config = files("../config/quality/detekt-config.yml")
    input = files("src/main/java")
    parallel = true
    disableDefaultRuleSets = false
    debug = false
    reports {
        html {
            enabled = true
            destination = file("build/reports/detekt.html")
        }
    }
}

// ktlint configuration
tasks.register<JavaExec>("ktlint") {
    group = "verification"
    description = "Check Kotlin code style."
    classpath = ktlint
    main = "com.github.shyiko.ktlint.Main"
    args("--android", "src/**/*.kt")
}

// skip variant release for CI
if (buildType == "CI") {
    android.variantFilter {
        if (name == "release") {
            setIgnore(true)
        }
    }
}

apply(mapOf("plugin" to "com.google.gms.google-services"))
