plugins {
    id("java")
    id("io.qameta.allure") version "2.11.2"
}

group = "com.spribe"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
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

    //Logging
    implementation("org.apache.logging.log4j:log4j-core:2.25.2")
    implementation("org.apache.logging.log4j:log4j-api:2.25.2")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.25.2")
    compileOnly("org.apache.logging.log4j:log4j-api:2.25.2")
    annotationProcessor("org.apache.logging.log4j:log4j-core:2.25.2")
}

tasks.test {
    useTestNG() {
        suites("src/test/resources/regression-suite.xml")
    }
    systemProperty(
        "allure.results.directory",
        layout.buildDirectory.dir("allure-results").get().asFile.absolutePath
    )
    testLogging {
        events("passed", "skipped", "failed")
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