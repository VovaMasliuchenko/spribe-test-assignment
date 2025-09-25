# Используем Debian-based JDK17
FROM eclipse-temurin:17-jdk

# Устанавливаем утилиты для Gradle и Allure
RUN apt-get update && apt-get install -y \
    curl unzip git bash fontconfig locales \
    && rm -rf /var/lib/apt/lists/*

# Устанавливаем Gradle
RUN curl -sLo /tmp/gradle.zip https://services.gradle.org/distributions/gradle-8.4-bin.zip \
    && unzip /tmp/gradle.zip -d /opt/gradle \
    && rm /tmp/gradle.zip

# Добавляем Gradle в PATH
ENV PATH="/opt/gradle/gradle-8.4/bin:${PATH}"

# Устанавливаем Allure CLI
RUN curl -sLo /tmp/allure.zip https://github.com/allure-framework/allure2/releases/download/2.27.0/allure-2.27.0.zip \
    && unzip /tmp/allure.zip -d /opt/allure \
    && ln -s /opt/allure/allure-2.27.0/bin/allure /usr/local/bin/allure \
    && rm /tmp/allure.zip

# Рабочая директория
WORKDIR /spribe-test-assignment

# Копируем проект
COPY . /spribe-test-assignment

# Сборка без тестов, чтобы контейнер собирался
RUN gradle build -x test --no-daemon

# Запуск тестов и генерация Allure отчёта
CMD ["sh", "-c", "gradle test --no-daemon || true && allure generate build/allure-results --clean -o /spribe-test-assignment/allure-report"]
