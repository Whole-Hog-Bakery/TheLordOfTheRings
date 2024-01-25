plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("app.cash.molecule")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "org.middle.earth.lotr"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.middle.earth.lotr"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        testOptions {
            unitTests {
                isIncludeAndroidResources = true
            }
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    flavorDimensions += "default"
    productFlavors {
        configureEach {
            buildConfigField("String", "THE_ONE_API_BASE_API_URL", "\"https://the-one-api.dev/v2/\"")
            buildConfigField("String", "THE_ONE_API_ACCESS_TOKEN", "\"kU7vBYIaLencO1Z_mP1T\"")
        }
        create("development") {
            dimension = "default"
        }
        create("staging") {
            dimension = "default"
        }
        create("production") {
            dimension = "default"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"
        }
    }
}

dependencies {

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation(platform("io.arrow-kt:arrow-stack:1.2.1"))
    implementation("io.arrow-kt:arrow-core")
    implementation("io.arrow-kt:arrow-fx-coroutines")
    
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.12")

    implementation("io.ktor:ktor-client-android:2.3.7")
    implementation("io.ktor:ktor-client-serialization:2.3.7")
    implementation("io.ktor:ktor-client-logging:2.3.7")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("io.ktor:ktor-client-okhttp:2.3.7")
    implementation("io.ktor:ktor-client-logging-jvm:2.3.7")

    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0-alpha01")

    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-paging:2.6.1")
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.paging:paging-compose:3.2.1")

    implementation("com.github.requery:sqlite-android:3.43.0")

    testImplementation("androidx.room:room-testing:2.6.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.10")

    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-android-compiler:2.50")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.window:window:1.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("com.google.android.material:material:1.5.0")

    implementation("org.slf4j:slf4j-api:2.0.11")
    implementation("org.slf4j:slf4j-simple:2.0.11")

    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.foundation:foundation-layout")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.lifecycle:lifecycle-runtime-compose")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.slf4j:slf4j-api:2.0.11")
    testImplementation("org.slf4j:slf4j-simple:2.0.11")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("io.ktor:ktor-client-mock:2.3.7")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("org.robolectric:robolectric:4.11.1")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}