plugins {
    id("java")
}

group = "co.com.Automatizacion"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("net.serenity-bdd:serenity-core:4.0.31")
    testImplementation("net.serenity-bdd:serenity-cucumber:4.0.31")
    testImplementation("net.serenity-bdd:serenity-screenplay:4.0.31")
    testImplementation("net.serenity-bdd:serenity-screenplay-webdriver:4.0.31")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.15.0")
    testImplementation("io.cucumber:cucumber-java:7.14.0")
    testImplementation("io.cucumber:cucumber-junit:7.14.0")
    testImplementation("net.serenity-bdd:serenity-junit:4.0.31")
}

tasks.test {
    useJUnitPlatform()
}
