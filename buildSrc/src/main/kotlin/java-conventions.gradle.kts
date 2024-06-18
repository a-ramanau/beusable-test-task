val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
fun findLibrary(libraryName: String) = libs.findLibrary(libraryName).get()
fun findVersion(toolName: String) = libs.findVersion(toolName).get().requiredVersion

val javaVersion: String = findVersion("java")

plugins {
    id("java")
    id("com.github.johnrengelman.shadow")
    id("io.freefair.lombok")
    id("jacoco")
    id("checkstyle")
    id("pmd")
}

group = "com.github.aramanau"

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation(platform(findLibrary("junit")))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(findLibrary("mockito-core"))
    testImplementation(findLibrary("mockito-junit-jupiter"))
    testImplementation(findLibrary("junit-pioneer"))
    testImplementation(findLibrary("assertj-core"))
}

checkstyle {
    maxWarnings = 0
    toolVersion = findVersion("checkstyle")
    configFile = file("../code-quality/checkstyle/checkstyle.xml")
}

pmd {
    isConsoleOutput = true
    toolVersion = findVersion("pmd")
    ruleSets = emptyList()
    ruleSetFiles = files("../code-quality/pmd/rules.xml")
}

jacoco {
    toolVersion = findVersion("jacoco")
}

tasks {
    val testJvmArgs = listOf(
        "--add-opens=java.base/java.util=ALL-UNNAMED",
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
    )

    jar {
        enabled = false
    }

    shadowJar {
        archiveClassifier.set("")
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

    build {
        dependsOn(jacocoTestCoverageVerification)
        finalizedBy(shadowJar)
    }

    test {
        jvmArgs(testJvmArgs)
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }

    val exclusionsFilterForJacoco = listOf(
        "**/**Application.*"
    )

    jacocoTestReport {
        reports {
            xml.required = true
            csv.required = false
        }

        classDirectories = files(classDirectories.files.map {
            fileTree(it) {
                exclude(exclusionsFilterForJacoco)
            }
        })
    }

    jacocoTestCoverageVerification {
        classDirectories = files(classDirectories.files.map {
            fileTree(it) {
                exclude(exclusionsFilterForJacoco)
            }
        })

        violationRules {
            rule {
                limit {
                    minimum = "0.95".toBigDecimal()
                }
            }
        }
    }
}
