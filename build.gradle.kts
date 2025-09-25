plugins {
    java
    id("io.qameta.allure") version "2.11.2"
}

group = "com.spribe"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val aspectjVersion = "1.9.22.1"

configurations {
    create("aspectjAgent") {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

dependencies {
    // RestAssured
    implementation("io.rest-assured:rest-assured:5.5.0")
    implementation("io.rest-assured:json-schema-validator:5.5.0")

    // TestNG
    implementation("org.testng:testng:7.11.0")

    // Allure
    implementation("io.qameta.allure:allure-testng:2.27.0")
    implementation("io.qameta.allure:allure-rest-assured:2.27.0")

    // Logging
    implementation("org.apache.logging.log4j:log4j-core:2.25.2")
    implementation("org.apache.logging.log4j:log4j-api:2.25.2")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.25.2")
    compileOnly("org.apache.logging.log4j:log4j-api:2.25.2")
    annotationProcessor("org.apache.logging.log4j:log4j-core:2.25.2")

    // Allure Steps
    add("aspectjAgent", "org.aspectj:aspectjweaver:$aspectjVersion")
    testImplementation("org.aspectj:aspectjweaver:$aspectjVersion")
}

tasks.test {
    useTestNG() {
        suites("src/test/resources/regression-suite.xml")
    }

    doFirst {
        val weaverJar = configurations.getByName("aspectjAgent").singleFile
        jvmArgs = listOf("-javaagent:$weaverJar")
    }

    systemProperty(
        "allure.results.directory",
        layout.buildDirectory.dir("allure-results").get().asFile.absolutePath
    )

    testLogging {
        events("PASSED", "FAILED", "SKIPPED", "STANDARD_OUT", "STANDARD_ERROR")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
    }
}

allure {
    version.set("2.27.0")
    adapter {
        frameworks {
            testng {
                adapterVersion.set("2.27.0")
            }
        }
    }
}
