<h1 align="center">"Мини-банк" Middle-сервис</h1>

<p align="center"> Создано в рамках обучения в академии Backend-разработки <a href="https://gpb.fut.ru/itfactory/backend"> GPB IT Factory</a></p>

<h1 id="contents">Оглавление</h1>

1. [О проекте](#1)
2. [Начало работы](#2)
3. [Необходимые компоненты](#3)
4. [Скачивание и установка](#4)
5. [Используемые команды и примеры](#5)
6. [Контакты](#6)
7. [To Do](#7)

<h2 id="1">О проекте</h2>

Данный проект представляет собой часть банковского приложения\
Middle-сервис, часть "мини-банка", принимающая запросы от [telegram-бота](https://github.com/gpb-it-factory/elenberger-telergam-bot). Также проводит валидацию, выполняет бизнес логику, маршрутизирует запросы в "банк"\
Полная схема "мини-банка":

```plantuml
@startuml
!theme spacelab
actor "Пользователь" as User
participant "Frontend" as Front
participant Middle
participant "Backend" as Back
rnote over of Front: Телеграмм-бот\nВыполняет роль клиентского приложения\nИнициализирует запросы пользователя
/ rnote over of Middle: Сервис на Java\nПринимает запросы бота\nПроводит валидацию\nВыполняет бизнес-логику\nМаршрутизирует запросы в "банк"
/ rnote over of Back: Выступает в качестве АБС\nОбрабатывает транзакции\nХранит клиентские данные
User -> Front : Обращается к боту
activate Front
Front -> Middle : Инициализация запроса
activate Middle
Middle -> Back : Маршрутизация запроса в "банк"
activate Back
Back -> Back : Сохранение изменений
Back --> Middle : Возвращение данных
deactivate Back
Middle --> Front : Возвращение ответного запроса
deactivate Middle
Front --> User : Вывод ответного сообщения
deactivate Front
@enduml
```

[Вверх :arrow_up:](#contents)

<h2 id="2">Начало работы</h2>

:warning: Раздел в разработке



[Вверх :arrow_up:](#contents)

<h2 id="3">Необходимые компоненты</h2>

:warning: Новые компоненты будут добавляться по мере развития проекта

- [Java 17+](https://www.java.com/ru/)
- [Gradle 7.4/8.7](https://www.gradle.org/)
- [SpringBoot](https://www.spring.io/projects/spring-boot)

[Вверх :arrow_up:](#contents)

<h2 id="4">Скачивание и установка</h2>

:warning: Раздел в разработке\


[Вверх :arrow_up:](#contents)

<h2 id="5">Используемые команды и примеры</h2>

:warning: Будут добавляться по мере развития функционала \
В данный момент доступны команды:\
**/start** - начинает работу с ботом\
**/ping** - дает в ответ pong\
**/help** - команда в разработке, будет выводить список доступных действий

[Вверх :arrow_up:](#contents)

<h2 id="6">Контакты</h2>

E-mail: [elnberg2397@gmail.com](mailto:elnberg2397@gmail.com)  
Telegram: [LohmatiyShmel](https://t.me/LohmatiyShmel)

[Вверх :arrow_up:](#contents)

<h2 id="7">To Do</h2>

Учет текущих задач по проекту
- [x] Задача 1. Создать репозиторий для telegram-бота
- [x] Задача 2. Добавить ReadMe
- [x] Задача 3. Создать "скелет" бота
- [x] Задача 4. Добавить базовый CI
- [x] Задача 5. Создать репозиторий для middle-сервиса
- [ ] Задача 6. Добавить функциональность регистрации пользователя
- [ ] .... TBD

[Вверх :arrow_up:](#contents)

