# ProductivityHub

Модульная микросервисная платформа управления личной продуктивностью.

## Стек

Java 17, Spring Boot 3.2.x, Spring Cloud Gateway, PostgreSQL 15, Docker.

## Структура

- `auth-service` — аутентификация (JWT, BCrypt)
- `gateway-service` — API Gateway (Spring Cloud Gateway)
- `todo-service` — управление задачами
- `finance-service` — учёт финансов
- `notes-service` — заметки
- `planner-service` — планировщик событий
- `dashboard-service` — дашборд с агрегацией метрик
- `module-registry-service` — реестр модулей

## Запуск

```bash
# Сборка проекта
gradlew build

# Запуск через Docker
docker-compose up -d

# Просмотр логов
docker-compose logs -f

# Остановка
docker-compose down

# Полная пересборка и запуск
docker-compose up -d --build
```

Gateway доступен на http://localhost:8080
