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
    testImplementation("io.rest-assured:rest-assured:5.5.0")
    testImplementation("io.rest-assured:json-schema-validator:5.5.0")

    // TestNG
    testImplementation("org.testng:testng:7.11.0")

    // Allure
    testImplementation("io.qameta.allure:allure-testng:2.27.0")
    testImplementation("io.qameta.allure:allure-rest-assured:2.27.0")
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