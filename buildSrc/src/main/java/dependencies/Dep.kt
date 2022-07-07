package dependencies

object Versions {
    const val compileSdk = 31
    const val buildTools = "31.0.0"

    const val minSdk = 23
    const val targetSdk = 31
    const val versionCode = 26
    const val versionName = "1.5.6"
}

object Dep {
    object GradlePlugin {
        private const val androidStudioGradlePluginVersion = "7.0.3"
        const val android = "com.android.tools.build:gradle:$androidStudioGradlePluginVersion"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val kotlinSerialization =
            "org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Dagger.version}"
    }

    object AndroidX {
        object activity {
            const val activityVersion = "1.3.1"
            const val activity = "androidx.activity:activity:$activityVersion"
            const val ktx = "androidx.activity:activity-ktx:$activityVersion"
        }

        const val appcompat = "androidx.appcompat:appcompat:1.4.1"
        const val coreKtx = "androidx.core:core-ktx:1.7.0"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.3"

        object Lifecycle {
            private const val lifecycleVersion = "2.3.1"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
            const val runTime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
            const val extension = "androidx.lifecycle:lifecycle-extensions:2.2.0"
        }

        object UI {
            const val material = "com.google.android.material:material:1.5.0"
            const val cardView = "androidx.cardview:cardview:1.0.0"
        }

        object RecyclerView {
            const val core = "androidx.recyclerview:recyclerview:1.2.1"
            const val selection = "androidx.recyclerview:recyclerview-selection:1.1.0"
        }

        object Navigation {
            private const val version = "2.4.0"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"

        }

    }

    object Dagger {
        const val version = "2.39.1"
        const val dagger = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"

        object Hilt {
            const val android = "com.google.dagger:hilt-android:$version"
            const val compiler = "com.google.dagger:hilt-compiler:$version"
        }
    }


    object Kotlin {
        const val version = "1.5.31"
        const val stdlibJvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"

        object coroutines {
            private const val coroutinesVersion = "1.5.2"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
            const val android =
                "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"
        }

        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0"
    }

    object OkHttp {
        private const val version = "4.9.2"
        const val core = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val core = "com.squareup.retrofit2:retrofit:$version"
        const val serialization = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
        const val converter = "com.squareup.retrofit2:converter-gson:$version"
        const val rxJava = "com.squareup.retrofit2:adapter-rxjava2:2.3.0"
    }

    object Reactivex {
        const val rxandroid = "io.reactivex.rxjava2:rxandroid:2.1.0"
        const val rxkotlin = "io.reactivex.rxjava2:rxkotlin:2.2.0"
    }

    object Firebase {
        const val auth = "com.google.firebase:firebase-auth:21.0.1"
        const val crashlytics = "com.google.firebase:firebase-crashlytics:18.2.7"
        const val analytics = "com.google.firebase:firebase-analytics:20.0.2"
    }

    object Gms {
        const val auth = "com.google.android.gms:play-services-auth:20.0.1"
        const val ads = "com.google.android.gms:play-services-ads:20.5.0"
        const val base = "com.google.android.gms:play-services-base:18.0.1"
    }

    object InAllBilling {
        private const val version = "3.0.2"
        const val core = "com.android.billingclient:billing:$version"
        const val coreKtx = "com.android.billingclient:billing-ktx:$version"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val androidJunit = "androidx.test.ext:junit:1.1.3"
        const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
        const val fragment = "androidx.fragment:fragment-testing:1.3.6"
        const val runner = "androidx.test:runner:1.4.0"
    }

    const val glide = "com.github.bumptech.glide:glide:4.12.0"
    const val calendarview = "com.prolificinteractive:material-calendarview:1.4.3"
    const val jsoup = "org.jsoup:jsoup:1.14.3"
    const val lottie = "com.airbnb.android:lottie:3.0.7"
    const val flexbox = "com.google.android.flexbox:flexbox:3.0.0"
}

