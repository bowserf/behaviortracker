import io.gitlab.arturbosch.detekt.detekt
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("io.gitlab.arturbosch.detekt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = ProjectConfig.SdkVersions.compileSdkVersion

    defaultConfig {
        applicationId = "fr.bowser.time"
        minSdk = ProjectConfig.SdkVersions.minSdkVersion
        targetSdk = ProjectConfig.SdkVersions.targetSdkVersion
        versionCode = ProjectConfig.SdkVersions.versionCode
        versionName = ProjectConfig.SdkVersions.versionName

        resConfigs("en", "fr", "it", "tr")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        release {
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
            setMatchingFallbacks("ua", "debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    lintOptions {
        isAbortOnError = false
    }

    sourceSets {
        getByName("main") {
            // Split resources.
            // https://medium.com/google-developer-experts/android-project-structure-alternative-way-29ce766682f0#.sjnhetuhb
            res.srcDirs(
                    "src/main/res/alarm",
                    "src/main/res/choosepomodorotimer",
                    "src/main/res/common",
                    "src/main/res/createtimer",
                    "src/main/res/home",
                    "src/main/res/instantapp",
                    "src/main/res/notification",
                    "src/main/res/pomodoro",
                    "src/main/res/rewards",
                    "src/main/res/rewardsrow",
                    "src/main/res/setting_view",
                    "src/main/res/showmode",
                    "src/main/res/timer",
                    "src/main/res/timerlist",
                    "src/main/res/ua",
                    "src/main/res/update_timer_time",
                    "src/main/res/widget"
            )
        }
    }

    setDynamicFeatures(setOf(":installedapp", ":instantapp"))
}

dependencies {

    implementation(project(":feature_alarm"))
    implementation(project(":feature_do_not_disturb"))
    implementation(project(":feature_string"))

    // Kotlin
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))
    implementation(kotlin("reflect", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.0.1")

    implementation("com.google.android.play:core:1.8.3")

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
    implementation("com.android.billingclient:billing:3.0.1")

    // Architecture component
    implementation("androidx.room:room-runtime:2.1.0-alpha06")
    kapt("androidx.room:room-compiler:2.1.0-alpha06")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:29.0.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    // Other
    implementation("com.google.dagger:dagger:2.39")
    kapt("com.google.dagger:dagger-compiler:2.39")

    implementation("com.hannesdorfmann:adapterdelegates4:4.3.0")
    implementation("com.hannesdorfmann:adapterdelegates4-kotlin-dsl:4.3.0")

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
