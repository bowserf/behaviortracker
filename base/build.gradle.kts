import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("io.gitlab.arturbosch.detekt")
    id("androidx.navigation.safeargs.kotlin")
}

val ktlint by configurations.creating

android {
    compileSdkVersion(ProjectConfig.SdkVersions.compileSdkVersion)
    buildToolsVersion(ProjectConfig.SdkVersions.buildToolsVersion)

    defaultConfig {
        minSdkVersion(ProjectConfig.SdkVersions.minSdkVersion)
        targetSdkVersion(ProjectConfig.SdkVersions.targetSdkVersion)
        versionCode = ProjectConfig.SdkVersions.versionCode
        versionName = ProjectConfig.SdkVersions.versionName

        resConfigs("en", "fr", "it", "tr")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles("proguard-rules.pro")
            buildConfigField("boolean", "UA", "false")

            signingConfig = signingConfigs.findByName("release")
        }
        getByName("debug") {
            versionNameSuffix = ".dev"
            buildConfigField("boolean", "UA", "false")
        }
        create("ua") {
            initWith(getByName("debug"))
            versionNameSuffix = ".ua"
            buildConfigField("boolean", "UA", "true")
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

    sourceSets {
        getByName("main") {
            // Split resources.
            // https://medium.com/google-developer-experts/android-project-structure-alternative-way-29ce766682f0#.sjnhetuhb
            res.srcDirs(
                    "src/main/res/common",
                    "src/main/res/home",
                    "src/main/res/timer",
                    "src/main/res/createtimer",
                    "src/main/res/timerlist",
                    "src/main/res/notification",
                    "src/main/res/setting",
                    "src/main/res/showmode",
                    "src/main/res/alarm",
                    "src/main/res/ua",
                    "src/main/res/rewards",
                    "src/main/res/rewardsrow",
                    "src/main/res/pomodoro",
                    "src/main/res/choosepomodorotimer"
            )
        }
    }
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.0.1")

    // Design
    implementation("com.google.android.material:material:1.1.0-alpha09")

    // AndroidX library
    implementation("androidx.preference:preference-ktx:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta3")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.annotation:annotation:1.1.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.1.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.1.0")

    // Billing
    implementation("com.android.billingclient:billing:1.1")

    // Architecture component
    implementation("androidx.room:room-runtime:2.1.0-alpha06")
    kapt("androidx.room:room-compiler:2.1.0-alpha06")

    // Firebase
    implementation("com.google.firebase:firebase-analytics:17.2.1")

    // Other
    implementation("com.google.dagger:dagger:2.14.1")
    kapt("com.google.dagger:dagger-compiler:2.14.1")

    // Debug
    implementation("com.crashlytics.sdk.android:crashlytics:2.9.5@aar") {
        isTransitive = true
    }

    // Static analyzer
    ktlint("com.github.shyiko:ktlint:0.31.0")

    // Test
    testImplementation("org.json:json:20180813")
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.27.0")
    androidTestImplementation("androidx.room:room-testing:2.1.0-alpha06")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test:rules:1.2.0")
    androidTestImplementation("org.hamcrest:hamcrest-library:1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
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