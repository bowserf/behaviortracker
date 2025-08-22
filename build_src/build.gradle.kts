import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradle) {
        because("Access to Android gradle plugin DSL to configure the android part")
    }
    compileOnly(libs.kotlin.gradle) {
        because("Access to the Kotlin gradle plugin DSL to configure the kotlin part")
    }
}

gradlePlugin {
    plugins {
        register("constPlugin") {
            id = "fr.bowser.build_src.projectconfig"
            implementationClass = "fr.bowser.build_src.ProjectConfig"
        }
        register("androidApp") {
            id = "fr.bowser.android.application"
            implementationClass = "fr.bowser.build_src.AndroidApplicationConventionPlugin"
        }
        register("androidFeature") {
            id = "fr.bowser.android.feature"
            implementationClass = "fr.bowser.build_src.AndroidFeatureConventionPlugin"
        }
    }
}
