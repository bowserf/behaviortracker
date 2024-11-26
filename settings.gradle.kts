enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// Plugin management doc
// https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_management
pluginManagement {
    /**
     * The pluginManagement.repositories block configures the
     * repositories Gradle uses to search or download the Gradle plugins and
     * their transitive dependencies.
     */
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    /**
     * The dependencyResolutionManagement.repositories
     * block is where you configure the repositories and dependencies used by
     * all modules in your project, such as libraries that you are using to
     * create your application. However, you should configure module-specific
     * dependencies in each module-level build.gradle file.
     */
    repositories {
        google()
        mavenCentral()
    }
}

includeBuild("build_src")

include(
    ":base",
    ":feature_alarm",
    ":feature_billing",
    ":feature_clipboard",
    ":feature_do_not_disturb",
    ":feature_review",
    ":feature_string",
    ":feature_toast",
    ":translations"
)
