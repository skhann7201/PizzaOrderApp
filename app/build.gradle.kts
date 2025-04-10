plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.rupizzeria"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.rupizzeria"
        minSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.cardview)
    implementation(fileTree(mapOf(
        "dir" to "/Users/khan/Library/Android/sdk/platforms/android-34",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf<String>()
    )))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}