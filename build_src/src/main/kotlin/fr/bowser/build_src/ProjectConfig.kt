package fr.bowser.build_src

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.Properties

class ProjectConfig : Plugin<Project> {

    override fun apply(project: Project) {
        // Possibly common dependencies or can stay empty
    }

    object SdkVersions {
        const val versionCode = 70000010
        const val versionName = "0.08.01"
        const val compileSdkVersion = 33
        const val targetSdkVersion = 33
        const val minSdkVersion = 21
    }

    data class SigningData(
        val storeFile: String,
        val storePassword: String,
        val keyAlias: String,
        val keyPassword: String
    ) {
        companion object {
            fun of(properties: Properties): SigningData {
                val storeFile = properties.getProperty("storeFile")
                    ?: throw IllegalStateException("Properties should contain \"storeFile\" key")
                val storePassword = properties.getProperty("storePassword")
                    ?: throw IllegalStateException("Properties should contain \"storePassword\" key")
                val keyAlias = properties.getProperty("keyAlias")
                    ?: throw IllegalStateException("Properties should contain \"keyAlias\" key")
                val keyPassword = properties.getProperty("keyPassword")
                    ?: throw IllegalStateException("Properties should contain \"keyPassword\" key")
                return SigningData(storeFile, storePassword, keyAlias, keyPassword)
            }
        }
    }
}

fun Project.getPropertiesFromFile(fileName: String): Properties {
    val file = file(fileName)
    if (!file.exists()) {
        throw IllegalStateException("Following file \"$fileName\" doesn't exist.")
    }
    val props = Properties()
    props.load(file.inputStream())
    return props
}
