pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
  }
}

rootProject.name = "ZenJourney"
include(":app", ":backend", ":shared")
