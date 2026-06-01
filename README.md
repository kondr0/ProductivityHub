# ProductivityHub

Модульная микросервисная платформа управления личной продуктивностью.

Дипломный проект. Платформа состоит из 8 микросервисов, объединённых через API Gateway, с единой базой данных PostgreSQL и контейнеризацией Docker.

## Архитектура

```
Client → Gateway (8080) → auth-service (8081)
                        → todo-service (8082)
                        → finance-service (8083)
                        → notes-service (8084)
                        → planner-service (8085)
                        → dashboard-service (8086)
                        → module-registry-service (8087)
                               ↓
                        PostgreSQL (5432)
```

Gateway — единая точка входа. Все запросы кроме `/api/auth/register` и `/api/auth/login` требуют JWT токен в заголовке `Authorization: Bearer <token>`.

## Стек

- Java 17, Spring Boot 3.2.5, Spring Cloud Gateway 2023.0.1
- PostgreSQL 15, Flyway, Hibernate JPA
- Docker, Docker Compose
- JWT (jjwt 0.12.5), BCrypt, Lombok
- OpenAPI/Swagger, JUnit 5, Mockito
- Gradle 8.7 (wrapper)

## Структура

| Сервис | Порт | Описание |
|--------|------|----------|
| `auth-service` | 8081 | Регистрация, логин, JWT, профили |
| `gateway-service` | 8080 | API Gateway, filter, CORS, роутинг |
| `todo-service` | 8082 | Задачи (CRUD, статусы, приоритеты, теги) |
| `finance-service` | 8083 | Финансы (транзакции, категории, бюджеты) |
| `notes-service` | 8084 | Заметки (CRUD, поиск, теги) |
| `planner-service` | 8085 | События и напоминания |
| `dashboard-service` | 8086 | Агрегация данных из сервисов |
| `module-registry-service` | 8087 | Реестр модулей и доступов |

## Быстрый старт

### Требования

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (с WSL2)
- Git

### Запуск

```bash
# 1. Клонировать
git clone https://github.com/kondr0/ProductivityHub.git
cd ProductivityHub

# 2. Собрать (опционально — сборка происходит в Docker)
.\gradlew build -x test

# 3. Запустить все сервисы
docker compose up -d --build

# 4. Проверить
curl http://localhost:8080/actuator/health
# → {"status":"UP"}
```

### Остановка

```bash
docker compose down
```

## Демонстрация (пошагово)

Всё работает через `http://localhost:8080`. Используйте **Postman** или **curl**.

### 1. Health-check

```
GET http://localhost:8080/actuator/health
→ {"status":"UP"}
```

### 2. Регистрация

```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "demo",
  "email": "demo@test.com",
  "password": "123456"
}
```

В ответе придёт JWT токен — скопируйте его.

### 3. Логин

```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "demo@test.com",
  "password": "123456"
}
```

Тоже возвращает токен.

### 4. Запросы с токеном

Добавьте заголовок ко всем следующим запросам:

```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

### 5. Создать задачу

```
POST http://localhost:8080/api/todos
Content-Type: application/json

{
  "title": "Купить продукты",
  "description": "Молоко, хлеб, яйца",
  "priority": "HIGH"
}
```

### 6. Получить список задач

```
GET http://localhost:8080/api/todos
```

### 7. Создать заметку

```
POST http://localhost:8080/api/notes
Content-Type: application/json

{
  "title": "Идея для проекта",
  "content": "Автоматизировать отчёты"
}
```

### 8. Добавить транзакцию

```
POST http://localhost:8080/api/finance/transactions
Content-Type: application/json

{
  "type": "EXPENSE",
  "amount": 1500.00,
  "category": "Еда",
  "description": "Обед в кафе"
}
```

### 9. Создать событие в планировщике

```
POST http://localhost:8080/api/planner/events
Content-Type: application/json

{
  "title": "Созвон с командой",
  "dateTime": "2026-06-02T10:00:00",
  "duration": 60
}
```

### 10. Дашборд (агрегация всех данных)

```
GET http://localhost:8080/api/dashboard/summary
```

### 11. Список доступных модулей

```
GET http://localhost:8080/api/modules
```

### 12. Swagger UI (документация API)

Откройте в браузере:

```
http://localhost:8080/swagger-ui/index.html
```

### 13. Health-статусы сервисов

```
GET http://localhost:8080/actuator/health
GET http://localhost:8080/actuator/gateway
GET http://localhost:8080/actuator/metrics
```

## Примечания

- **JWT secret** задаётся в `.env` (файл не попадает в репозиторий). По умолчанию используется тот же ключ, что в `docker-compose.yml`.
- База данных PostgreSQL поднимается автоматически, миграции Flyway применяются при старте каждого сервиса.
- Пароль БД: `postgres` (только для разработки).
- Все сервисы имеют тесты JUnit 5 + Mockito. Запуск: `.\gradlew test`.
- Проект рассчитан на локальную разработку. Для продакшена требуется доработка безопасности, логирования и мониторинга.

## Требования к дипломному проекту

Данный проект разработан в рамках выпускной квалификационной работы и удовлетворяет следующим требованиям:
1. Микросервисная архитектура (не менее 5 микросервисов) — реализовано 8 микросервисов
2. Единый API Gateway — Spring Cloud Gateway с фильтром аутентификации
3. JWT-аутентификация с ролевой моделью
4. База данных PostgreSQL с миграциями Flyway
5. Контейнеризация Docker (каждый сервис имеет свой Dockerfile)
6. API-документация OpenAPI/Swagger
7. Модульное тестирование (JUnit 5 + Mockito)
8. Реестр модулей с возможностью включения/отключения функционала
9. Агрегация данных в дашборде из нескольких сервисов
