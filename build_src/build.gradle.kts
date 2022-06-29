import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
    }
}

dependencies {
    implementation(gradleApi())
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.0")
}

gradlePlugin {
    plugins {
        create("constPlugin") {
            id = "fr.bowser.build_src.projectconfig"
            implementationClass = "fr.bowser.build_src.ProjectConfig"
        }
    }
}
