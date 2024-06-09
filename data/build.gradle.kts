import dependencies.Dep
import dependencies.Versions

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    kotlin("kapt")
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
    namespace = "com.zinc.data"
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.coroutines.core)
    implementation(Dep.Kotlin.serialization)

    // Dagger Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    api(Dep.Retrofit.core)
    implementation(Dep.Retrofit.core)
    implementation(Dep.Retrofit.serialization)
    implementation(Dep.Retrofit.converter)

    implementation(Dep.OkHttp.core)
    implementation(Dep.OkHttp.loggingInterceptor)

}

kapt {
    correctErrorTypes = true
}