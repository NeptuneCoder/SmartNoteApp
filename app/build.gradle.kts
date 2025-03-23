import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

// 读取 local.properties
val localProperties = File(rootProject.projectDir, "local.properties").inputStream().use {
    Properties().apply { load(it) }
}
android {
    namespace = "com.smart.note"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.smart.note"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 将变量注入 BuildConfig
        buildConfigField(
            "String",
            "DEEPSEEK_API_KEY",
            "\"${localProperties.getProperty("deepseek.api.key")}\""
        )
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    // 自定义输出文件名
    applicationVariants.all {
        val variant = this
        variant.outputs.all {
            if (this is com.android.build.gradle.internal.api.BaseVariantOutputImpl) {
                val versionName = variant.versionName ?: "1.0"
                val versionCode = variant.versionCode
                val buildType = variant.buildType.name
                val formattedAppName = "SmartNote" // 可改成动态获取的应用名称

                // 获取当前时间（格式：yyyy-MM-dd）
                val buildTime = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                // 自定义 APK 文件名
                (this).outputFileName =
                    "$formattedAppName-v${versionName}_$versionCode-$buildType-$buildTime.apk"

            }
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // Dagger 核心库
    implementation(libs.dagger)
    implementation(project(":basic"))
    kapt(libs.dagger.compiler)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)  // 使用最新版本

    // Room 核心库

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)   // Kotlin 注解处理器
    // 可选：Kotlin 扩展和协程支持
    implementation(libs.androidx.room.ktx)

    implementation(libs.retrofit)
    implementation(libs.converter.gson) // Gson 解析 JSON
    // 支持原始数据流解析
    implementation(libs.converter.scalars)
    implementation(libs.logging.interceptor) // 可选，日志拦截器
    implementation(libs.retrofit2.kotlin.coroutines.adapter)

    implementation(libs.xx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}