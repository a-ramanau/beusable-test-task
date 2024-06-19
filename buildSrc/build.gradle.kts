val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
fun findLibrary(name: String) = libs.findLibrary(name).get()
fun findVersion(toolName: String) = libs.findVersion(toolName).get().requiredVersion

val javaVersion: String = findVersion("java")

plugins {
    `kotlin-dsl`
}

group = "com.github.aramanau"

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(findLibrary("lombok-plugin"))
    implementation(findLibrary("shadow-plugin"))
    implementation(findLibrary("springboot-plugin"))
    implementation(findLibrary("springdm-plugin"))
}
