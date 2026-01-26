package fr.bowser.build_src

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }
        }
    }
}
