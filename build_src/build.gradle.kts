import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.4.2")
    compileOnly("com.android.tools:common:31.7.1")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
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
