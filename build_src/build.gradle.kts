import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
}

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    }
}

dependencies {
    implementation(gradleApi())
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
}

gradlePlugin {
    plugins {
        create("constPlugin") {
            id = "fr.bowser.build_src.projectconfig"
            implementationClass = "fr.bowser.build_src.ProjectConfig"
        }
    }
}
