plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.ivar7284.monerominingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ivar7284.monerominingapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    //default
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //added after
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.12.0.202106070339-r")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

}

repositories {
    google()
    mavenCentral()
    maven {
        url = uri( "https://jitpack.io")
    }
}

tasks.register<Exec>("make") {
    description = "Run make command"
    group = "build"

    // Set the working directory to your project root
    workingDir = project.rootDir

    // Specify the command to be executed (in this case, 'make')
    commandLine("make", "-j10")
}

// Ensure that the 'make' task is executed during the build process
tasks.named("assemble") {
    dependsOn("make")
}
// Add this block at the end of your build.gradle.kts file

tasks.register<Exec>("cmake") {
    description = "Run cmake command"
    group = "build"

    // Set the working directory to your project root
    workingDir = project.rootDir

    // Specify the command to be executed (in this case, 'cmake' with the required options)
    commandLine("cmake", "..", "-DWITH_HWLOC=OFF")
}

// Ensure that the 'cmake' task is executed before the 'make' task
tasks.named("make") {
    dependsOn("cmake")
}
