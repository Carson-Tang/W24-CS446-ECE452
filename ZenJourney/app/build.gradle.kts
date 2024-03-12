plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
  // The version before the - needs to match `id "org.jetbrains.kotlin.android" version`
  // in the other build.gradle, see https://github.com/google/ksp/releases
  id("com.google.devtools.ksp") version "1.9.22-1.0.16"
}

android {
  namespace = "ca.uwaterloo.cs"
  compileSdk = 34

  defaultConfig {
    applicationId = "ca.uwaterloo.cs"
    minSdk = 33
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.9"
  }
}

dependencies {
  implementation(project(":shared"))

  implementation("androidx.activity:activity-compose:1.8.2")
  implementation("androidx.compose.ui:ui:1.6.2")
  implementation("androidx.compose.ui:ui-tooling:1.6.2")
  implementation("io.coil-kt:coil-compose:2.1.0")                            // For loading images.
  implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.7")                // For making HTTP calls.
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")   // For serializing/deserializing JSON.

  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
  implementation("androidx.test.ext:junit-ktx:1.1.5")

  implementation("androidx.compose.material3:material3:1.2.0")
  implementation("androidx.compose.material3:material3-android:1.2.0")

  testImplementation("junit:junit:4.13.2")
  testImplementation("androidx.test.ext:junit:1.1.5")
  androidTestUtil("androidx.test:orchestrator:1.4.2")
  androidTestImplementation("androidx.test:runner:1.5.2")
  androidTestImplementation("androidx.test:rules:1.5.0")

  val roomVersion = "2.6.1"

  implementation("androidx.room:room-runtime:$roomVersion")
  annotationProcessor("androidx.room:room-compiler:$roomVersion")

  // To use Kotlin Symbol Processing (KSP)
  ksp("androidx.room:room-compiler:$roomVersion")

  // optional - Kotlin Extensions and Coroutines support for Room
  implementation("androidx.room:room-ktx:$roomVersion")

  // optional - RxJava2 support for Room
  implementation("androidx.room:room-rxjava2:$roomVersion")

  // optional - RxJava3 support for Room
  implementation("androidx.room:room-rxjava3:$roomVersion")

  // optional - Guava support for Room, including Optional and ListenableFuture
  implementation("androidx.room:room-guava:$roomVersion")

  // optional - Test helpers
  testImplementation("androidx.room:room-testing:$roomVersion")

  // optional - Paging 3 Integration
  implementation("androidx.room:room-paging:$roomVersion")

  val ktorVersion = "2.3.8"

  implementation("io.ktor:ktor-client-core:$ktorVersion")
  implementation("io.ktor:ktor-client-json:$ktorVersion")
  implementation("io.ktor:ktor-serialization-gson-jvm")
  implementation("io.ktor:ktor-client-serialization:$ktorVersion")
  implementation("io.ktor:ktor-client-logging:$ktorVersion")
  implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
  implementation("io.ktor:ktor-client-cio:$ktorVersion")
  implementation("io.ktor:ktor-client-android:$ktorVersion")
  implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")


  implementation("com.google.code.gson:gson:2.10.1")
  // The view calendar library
  implementation ("com.kizitonwose.calendar:view:2.5.0")

  // The compose calendar library
  implementation ("com.kizitonwose.calendar:compose:2.5.0")

  // Used for adding material 3 icons
  implementation("androidx.compose.material:material-icons-extended:1.6.2")

  implementation("org.mindrot:jbcrypt:0.4")
}
