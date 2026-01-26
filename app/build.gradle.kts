import fr.bowser.build_src.ProjectConfig
import fr.bowser.build_src.getPropertiesFromFile

plugins {
    id("fr.bowser.android.application")
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.ksp)
}

android {
    namespace = "fr.bowser.behaviortracker"

    defaultConfig {
        applicationId = "fr.bowser.time"
        versionCode = ProjectConfig.SdkVersions.versionCode
        versionName = ProjectConfig.SdkVersions.versionName

        androidResources.localeFilters.addAll(
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
        testInstrumentationRunnerArguments["useTestStorageService"] = "true"
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
        debug {
            versionNameSuffix = ".dev"
            buildConfigField("boolean", "UA", "false")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "UA", "false")

            signingConfig = signingConfigs.findByName("release")
        }
        create("ua") {
            initWith(getByName("debug"))
            versionNameSuffix = ".ua"
            buildConfigField("boolean", "UA", "true")
            matchingFallbacks.addAll(listOf("ua", "debug"))
        }
    }

    lint {
        abortOnError = false
    }

    sourceSets {
        getByName("main") {
            // Split resources.
            // https://medium.com/google-developer-experts/android-project-structure-alternative-way-29ce766682f0#.sjnhetuhb
            res.directories.addAll(
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

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(projects.featureAlarm)
    implementation(projects.featureBilling)
    implementation(projects.featureClipboard)
    implementation(projects.featureDoNotDisturb)
    implementation(projects.featureReview)
    implementation(projects.featureString)
    implementation(projects.featureToast)
    implementation(projects.translations)

    // Kotlin
    implementation(kotlin("reflect"))
    implementation(libs.kotlinx.coroutine)
    implementation(libs.kotlinx.coroutines.android)

    // Design
    implementation(libs.material)

    // AndroidX library
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Architecture component
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // Other
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    // Unit test
    testImplementation(libs.json)
    testImplementation(libs.testing.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.konsist)

    // Android test
    androidTestImplementation(libs.testing.androidx.room)
    androidTestImplementation(libs.testing.androidx.runner)
    androidTestImplementation(libs.testing.androidx.rules)
    androidTestImplementation(libs.testing.androidx.junit) {
        because("We need this to define the running Activity with \"ActivityScenarioRule\"")
    }
    androidTestImplementation(libs.testing.espresso.core)
    androidTestImplementation(libs.testing.espresso.contrib) {
        because("We need this to interact with some UI elements not in the OS (Drawer, RecyclerView, ViewPager, etc...")
    }
    androidTestImplementation(libs.testing.hamcrest)
    androidTestUtil(libs.testing.androidx.services) {
        because("We need this dependency to save screenshot on device disk when we call \"writeToTestStorage\"")
    }
}
