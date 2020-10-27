plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.1"

    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.10")
    implementation("androidx.appcompat:appcompat:1.2.0")
    testImplementation("junit:junit:4.13.1")
}