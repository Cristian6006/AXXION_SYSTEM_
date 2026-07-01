plugins {
    java
    idea
    id("net.serenity-bdd.serenity-gradle-plugin") version "5.3.9"
}

group = "co.com.Automatizacion.AxxionSystem"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val serenityVersion = "5.3.9"
val cucumberVersion = "7.34.3"
val junitPlatformVersion = "1.12.2"

dependencies {
    implementation("org.hamcrest:hamcrest:2.2")

    // Serenity
    implementation("net.serenity-bdd:serenity-core:${serenityVersion}")
    implementation("net.serenity-bdd:serenity-cucumber:${serenityVersion}")
    implementation("net.serenity-bdd:serenity-screenplay:${serenityVersion}")
    implementation("net.serenity-bdd:serenity-screenplay-webdriver:${serenityVersion}")
    implementation("net.serenity-bdd:serenity-screenplay-rest:${serenityVersion}")
    implementation("net.serenity-bdd:serenity-ensure:${serenityVersion}")

    // Cucumber + JUnit 5
    testImplementation("net.serenity-bdd:serenity-junit5:${serenityVersion}")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:${cucumberVersion}")
    testImplementation("org.junit.platform:junit-platform-suite:${junitPlatformVersion}")

    // Utilidades
    implementation("org.slf4j:slf4j-simple:2.0.18")
    implementation("com.jcraft:jsch:0.1.55")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.poi:poi:4.1.2")
    implementation("org.apache.poi:poi-ooxml:5.4.0")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("net.datafaker:datafaker:2.1.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}


val headless = (System.getenv("HEADLESS") ?: "false").toBoolean()

tasks.withType<Test> {
    useJUnitPlatform()
    maxParallelForks = 1
    if (headless) {
        systemProperty("environment", "headless")
    }
    systemProperty("cucumber.publish.quiet", "true")
    jvmArgs(
        "--add-opens", "java.base/java.lang=ALL-UNNAMED",
        "--add-opens", "java.base/java.util=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
        "-Djava.util.logging.config.file=${project.projectDir}/src/test/resources/logging.properties"
    )
    finalizedBy("aggregate")
}

tasks.named<Test>("test") {
    filter {
        includeTestsMatching("co.com.Automatizacion.AxxionSystem.runners.AxxionSystemSuite")
    }
}
