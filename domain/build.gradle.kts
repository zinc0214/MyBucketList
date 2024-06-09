import dependencies.Dep
import dependencies.Versions

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    namespace = "com.zinc.domain"
}

dependencies {
    implementation(project(":data"))

    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.appcompat)

    implementation(Dep.Dagger.dagger)
    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.coroutines.core)
}