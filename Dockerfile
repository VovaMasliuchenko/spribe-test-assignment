FROM eclipse-temurin:17-jdk

RUN apt-get update && apt-get install -y \
    curl unzip git bash fontconfig locales \
    && rm -rf /var/lib/apt/lists/*

RUN curl -sLo /tmp/gradle.zip https://services.gradle.org/distributions/gradle-8.4-bin.zip \
    && unzip /tmp/gradle.zip -d /opt/gradle \
    && rm /tmp/gradle.zip

ENV PATH="/opt/gradle/gradle-8.4/bin:${PATH}"

RUN curl -sLo /tmp/allure.zip https://github.com/allure-framework/allure2/releases/download/2.27.0/allure-2.27.0.zip \
    && unzip /tmp/allure.zip -d /opt/allure \
    && ln -s /opt/allure/allure-2.27.0/bin/allure /usr/local/bin/allure \
    && rm /tmp/allure.zip

WORKDIR /spribe-test-assignment

COPY . /spribe-test-assignment

RUN gradle build -x test --no-daemon

CMD ["sh", "-c", "gradle test --no-daemon || true && allure generate build/allure-results --clean -o /spribe-test-assignment/allure-report"]
