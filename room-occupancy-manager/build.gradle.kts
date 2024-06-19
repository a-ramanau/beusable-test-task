import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("spring-conventions")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(libs.openapi)
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(libs.jsonunit)
}


tasks.withType<BootJar> {
    getMainClass().set("com.github.aramanau.roomoccupancymanager.OccupancyManagerApplication")
}

