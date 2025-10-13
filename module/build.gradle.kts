import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.jetbrains.kotlin.plugin.parcelize)
    alias(libs.plugins.kotlin.compose)
    id("org.sonarqube") version "3.3"
    id("jacoco")
    id("kotlin-parcelize")
}

apply {
    from(ConfigureApp.Gradle.sonarqube)
    from(ConfigureApp.Gradle.jacoco)
    from(ConfigureApp.Gradle.uploadArtifact)
    from("graph.gradle.kts")
}

val localProperties = rootProject.file("local.properties")
val properties = Properties()
if (localProperties.exists()) {
    properties.load(localProperties.inputStream())
}

val googleMapsApiKey: String = properties.getProperty("GOOGLE_MAPS_API_KEY") ?: ""

android {
    namespace = "com.afoxplus.places"
    compileSdk = ConfigureApp.Versions.compileSdkVersion

    defaultConfig {
        minSdk = ConfigureApp.Versions.minSdkVersion
        testInstrumentationRunner = ConfigureApp.Versions.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
        renderscriptSupportModeEnabled = true
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "GOOGLE_MAPS_API_KEY", "\"$googleMapsApiKey\"")
            resValue("string", "GOOGLE_MAPS_API_KEY", googleMapsApiKey)
        }
        getByName("release") {
            buildConfigField("String", "GOOGLE_MAPS_API_KEY", "\"$googleMapsApiKey\"")
            resValue("string", "GOOGLE_MAPS_API_KEY", googleMapsApiKey)
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("staging") {
            initWith(getByName("debug"))
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin { jvmToolchain(ConfigureApp.Versions.jdkVersion) }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    lint {
        disable.addAll(
            listOf(
                "TypographyFractions",
                "TypographyQuotes",
                "JvmStaticProvidesInObjectDetector",
                "FieldSiteTargetOnQualifierAnnotation",
                "ModuleCompanionObjects",
                "ModuleCompanionObjectsNotInModuleParent"
            )
        )
        checkDependencies = true
        abortOnError = false
        ignoreWarnings = false
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    //Jetpack
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.fragment.ktx)

    //Jetpack UI
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)


    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.androidx.compose)

    //Image Async
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    //Map
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.maps.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //Lifecycle Scope
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.androidx.hilt.compiler)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Test
    testImplementation(libs.bundles.unit.test)

    // Business Dependencies
    implementation(project(ModuleDependency.Core.DESIGN_SYSTEM))
    implementation(project(ModuleDependency.Integration.NETWORK))

    //Compose Debug
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.tooling.preview)
}