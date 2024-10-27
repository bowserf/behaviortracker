includeBuild("build_src")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":base",
    ":feature_alarm",
    ":feature_billing",
    ":feature_clipboard",
    ":feature_do_not_disturb",
    ":feature_review",
    ":feature_string",
    ":translations"
)
