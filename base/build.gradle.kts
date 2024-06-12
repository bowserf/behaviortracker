import fr.bowser.build_src.ProjectConfig
import fr.bowser.build_src.getPropertiesFromFile
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = ProjectConfig.SdkVersions.compileSdkVersion
    namespace = "fr.bowser.behaviortracker"

    defaultConfig {
        applicationId = "fr.bowser.time"
        minSdk = ProjectConfig.SdkVersions.minSdkVersion
        targetSdk = ProjectConfig.SdkVersions.targetSdkVersion
        versionCode = ProjectConfig.SdkVersions.versionCode
        versionName = ProjectConfig.SdkVersions.versionName

        resourceConfigurations.addAll(
            listOf(
                "en",
                "de",
                "es",
                "fr",
                "hi",
                "it",
                "ja",
                "pt",
                "tr",
                "zh-rCN",
                "zh-rTW"
            )
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        ProjectConfig.SigningData.of(
            project.rootProject.getPropertiesFromFile("signing.properties")
        ).let {
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
            isShrinkResources = true
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
            matchingFallbacks.addAll(listOf("ua", "debug"))
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    lint {
        abortOnError = false
    }

    sourceSets {
        getByName("main") {
            // Split resources.
            // https://medium.com/google-developer-experts/android-project-structure-alternative-way-29ce766682f0#.sjnhetuhb
            res.setSrcDirs(
                listOf(
                    "src/main/res/alarm_notification",
                    "src/main/res/alarm_view",
                    "src/main/res/app_icon",
                    "src/main/res/common",
                    "src/main/res/create_timer_view",
                    "src/main/res/explain_permission_request_view",
                    "src/main/res/floating_running_timer_view",
                    "src/main/res/home_activity",
                    "src/main/res/notification",
                    "src/main/res/pomodoro_choose_timer_view",
                    "src/main/res/pomodoro_view",
                    "src/main/res/rewards_row_view",
                    "src/main/res/rewards_view",
                    "src/main/res/settings_view",
                    "src/main/res/shortcut",
                    "src/main/res/show_mode_item_view",
                    "src/main/res/show_mode_view",
                    "src/main/res/timer_item_view",
                    "src/main/res/timer_list_view",
                    "src/main/res/timer_service",
                    "src/main/res/ua",
                    "src/main/res/update_timer_time_view",
                    "src/main/res/widget"
                )
            )
        }
    }
}

dependencies {

    implementation(project(":feature_alarm"))
    implementation(project(":feature_billing"))
    implementation(project(":feature_clipboard"))
    implementation(project(":feature_do_not_disturb"))
    implementation(project(":feature_review"))
    implementation(project(":feature_string"))
    implementation(project(":translations"))

    // Kotlin
    implementation(kotlin("reflect", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    implementation("com.google.android.play:core:1.10.3")

    // Design
    implementation("com.google.android.material:material:1.12.0")

    // AndroidX library
    implementation("androidx.activity:activity:1.9.0")
    implementation("androidx.preference:preference-ktx:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.annotation:annotation:1.5.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    // Architecture component
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:31.1.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    // Other
    implementation("com.google.dagger:dagger:2.48.1")
    ksp("com.google.dagger:dagger-compiler:2.48.1")

    // Unit test
    testImplementation("org.json:json:20220320")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.3.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")

    // Android test
    androidTestImplementation("androidx.room:room-testing:2.6.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5") {
        because("We need this to define the running Activity with \"ActivityScenarioRule\"")
    }
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1") {
        because("We need this to interact with some UI elements not in the OS (Drawer, RecyclerView, ViewPager, etc...")
    }
    androidTestImplementation("org.hamcrest:hamcrest-library:2.2")
    androidTestUtil("androidx.test.services:test-services:1.4.2") {
        because("We need this dependency to save screenshot on device disk when we call \"writeToTestStorage\"")
    }
}
