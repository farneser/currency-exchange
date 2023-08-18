# Третье задание курса [java-backend-learning-course](https://zhukovsd.github.io/java-backend-learning-course/)

[![Java CI with Maven](https://github.com/farneser/currency-exchange/actions/workflows/maven.yml/badge.svg)](https://github.com/farneser/currency-exchange/actions/workflows/maven.yml)

## [Задание](https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/)

REST API для описания валют и обменных курсов. Позволяет просматривать и редактировать списки валют и обменных курсов, и
совершать расчёт конвертации произвольных сумм из одной валюты в другую.

Веб-интерфейс для проекта не подразумевается.

## Разработка

- Сборка происходит в формат [War (WebArchive)](https://www.geeksforgeeks.org/servlet-war-file/)
- Веб сервер для запуска War файла [Tomcat 1.1.12](https://tomcat.apache.org/download-10.cgi)
- Мини веб приложения для расширения или реализации
  функционала [Servlet](https://mvnrepository.com/artifact/jakarta.servlet/jakarta.servlet-api)
- Описание всех точек апи [docs](/src/main/java/com/farneser/servlets/README.md)
- Возможно введение простого клиента

## Что нужно знать

- [Java](https://github.com/zhukovsd/java-backend-learning-course/Technologies/Java/) - коллекции, ООП
- [Maven/Gradle](https://github.com/zhukovsd/java-backend-learning-course/Technologies/BuildSystems/)
- [Backend](https://github.com/zhukovsd/java-backend-learning-course/Technologies/Backend/)
    - Java сервлеты
    - HTTP - GET и POST запросы, коды ответа
    - REST API, JSON
- [Базы данных](https://github.com/zhukovsd/java-backend-learning-course/Technologies/Databases/) - SQL, JDBC
- [Деплой](https://github.com/zhukovsd/java-backend-learning-course/Technologies/DevOps/#деплой) - облачный хостинг,
  командная строка Linux, Tomcat

Фреймворки не используем.

## Мотивация проекта

- REST API - правильное именование ресурсов, использование HTTP кодов ответа
- SQL - базовый синтаксис, создание таблиц
