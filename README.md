# AuthServer

**AuthServer** - это сервер аутентификации, реализованный на языке Java с использованием Spring Boot. Основная задача проекта - предоставить надежный и масштабируемый сервис для управления аутентификацией пользователей, включая регистрацию, вход, а также защиту ресурсов с помощью JWT (JSON Web Tokens).

Проект построен с учетом современных практик безопасности и контейнеризации (Docker), что облегчает его развертывание и интеграцию в микросервисную архитектуру.

---

## Основные возможности

- Регистрация новых пользователей с валидацией данных
- Аутентификация пользователей с выдачей JWT токенов
- Защита API с использованием Spring Security и JWT
- Конфигурация и запуск через Docker и Docker Compose
- Использование Gradle для сборки и управления зависимостями

---

## Технологии

- Java 17
- Spring Boot 2.7.5
- Spring Security
- JWT (JSON Web Tokens)
- Gradle
- Docker, Docker Compose

---

## Структура проекта

```
AuthServer/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/bai/beicha/authserver/  	# Основной код приложения
│   │   │       ├── config/                 	# Конфигурации Spring Security и JWT
│   │   │       ├── controller/             	# REST контроллеры для API
│   │   │       ├── dto/                    	# Dto классы
│   │   │       ├── entity/                 	# Модели данных (например, User)
│   │   │       ├── repository/             	# Репозитории для работы с БД
│   │   │       ├── service/                	# Сервисный слой (логика аутентификации)
│   │   │       └── AuthServerApplication.java 	# Точка входа в приложение
│   │   └── resources/
│   │       └── application.yml            	# Конфигурационные файлы
└── build.gradle                          	# Скрипт сборки Gradle
```

---

## Установка и запуск

### Локальный запуск без Docker

1. Клонируйте репозиторий:
   ``` bash
   git clone https://github.com/BaiBeiCha/AuthServer.git
   cd AuthServer
   ```

2. Соберите проект с помощью Gradle:
	``` bash
   ./gradlew build
   ```

3. Запустите приложение:
   ``` bash
   ./gradlew bootRun
   ```

4. Приложение будет доступно по адресу: `http://localhost:9000`

---

### Запуск с Docker

1. Убедитесь, что у вас установлен Docker и Docker Compose.

2. Соберите Docker образ:
   ``` bash
   docker build -t authserver .
   ```

3. Запустите контейнер с помощью docker-compose:
   ``` bash
   docker-compose up
   ```

4. Сервер будет доступен на `http://localhost:9000`

---
## Описание переменных окружения для docker-compose

## Переменные для базы данных (PostgreSQL)

- **DATASOURCE_URL**  
    URL подключения к базе данных PostgreSQL.  
    Формат: `jdbc:postgresql://auth-db:5432/auth`  
    По умолчанию: `jdbc:postgresql://auth-db:5432/auth`
    
- **DATASOURCE_USERNAME**  
    Имя пользователя для подключения к базе данных.  
    По умолчанию: `auth`
    
- **DATASOURCE_PASSWORD**  
    Пароль пользователя базы данных.  
    По умолчанию: `auth`
    
- **SPRING_JPA_HIBERNATE_DDL_AUTO**  
    Управляет стратегией обновления схемы базы данных при старте приложения.  
    Возможные значения: `none`, `validate`, `update`, `create`, `create-drop`  
    По умолчанию: `update`
    

## Переменные приложения

- **SERVER_PORT**  
    Порт, на котором запускается Auth Server.  
    По умолчанию: `9000`
    
- **APP_ROLES**  
    Роли пользователей, которые автоматически создаются при старте.  
    Формат: строка с разделением ролей запятой, например `"USER,ADMIN"`  
    По умолчанию: `USER,ADMIN`
    
- **APP_SCOPES**  
    Области доступа (scopes) для авторизации, например `"READ,WRITE"`.  
    По умолчанию: `READ,WRITE`
    
- **APP_ADMIN_USERNAME**  
    Имя пользователя администратора, создаваемого автоматически.  
    По умолчанию: `admin`
    
- **APP_ADMIN_PASSWORD**  
    Пароль администратора.  
    По умолчанию: `adminpassword`
    

## Переменные для JWT (JSON Web Token)

- **JWT_EXPIRATION**  
    Время жизни JWT access токена в миллисекундах.  
    По умолчанию: `86400000` (24 часа)
    
- **JWT_REFRESH_EXPIRATION**  
    Время жизни JWT refresh токена в миллисекундах.  
    По умолчанию: `604800000` (7 дней)
    
- **JWT_SECRET**  
    Секретный ключ для подписи JWT токенов.  
    По умолчанию: длинная строка из нулей (рекомендуется заменить на уникальный и сложный ключ)
    

## Переменные для Eureka Discovery Client (если используется)

- **USE_EUREKA**  
    Включить или отключить клиент Eureka.  
    Значения: `true` или `false`  
    По умолчанию: `false`
    
- **EUREKA_SERVER_URL**  
    URL сервера Eureka для регистрации и поиска сервисов.  
    По умолчанию: `http://localhost:8761/eureka`
    
- **EUREKA_CLIENT_REGISTER**  
    Нужно ли регистрировать Auth Server в Eureka.  
    Значения: `true` или `false`  
    По умолчанию: `false`
    
- **EUREKA_INSTANCE_PREFER_IP_ADDRESS**  
    Использовать IP-адрес вместо hostname при регистрации в Eureka.  
    Значения: `true` или `false`  
    По умолчанию: `true`
    
- **SERVICE_NAME**  
    Имя сервиса для регистрации в Eureka.  
    По умолчанию: `auth-server`
    

## Переменные для Kafka (если используется)

- **USE_KAFKA**  
    Включить или отключить интеграцию с Kafka.  
    Значения: `true` или `false`  
    По умолчанию: `false`
    
- **KAFKA_BOOTSTRAP_SERVERS**  
    Адрес(а) Kafka брокеров.  
    Формат: `kafka:9092` или список через запятую  
    По умолчанию: `kafka:9092`
    
- **USER_REGISTER_TOPIC**  
    Топик Kafka для сообщений о регистрации пользователей.  
    По умолчанию: `user-registration`
    

## Пример секции environment для docker-compose.yml

``` docker-compose.yml
environment:  
	DATASOURCE_URL: jdbc:postgresql://auth-db:5432/auth  
	DATASOURCE_USERNAME: auth  
	DATASOURCE_PASSWORD: auth  
	SPRING_JPA_HIBERNATE_DDL_AUTO: update 
	
	SERVER_PORT: 9000   
	
	APP_ROLES: USER,ADMIN  
	APP_SCOPES: READ,WRITE  
	APP_ADMIN_USERNAME: admin  
	APP_ADMIN_PASSWORD: adminpassword  
	
	JWT_EXPIRATION: 86400000  
	JWT_REFRESH_EXPIRATION: 604800000  
	JWT_SECRET: your-secure-jwt-secret-key
	
	USE_EUREKA: true  
	EUREKA_SERVER_URL: http://localhost:8761/eureka  
	EUREKA_CLIENT_REGISTER: false  
	EUREKA_INSTANCE_PREFER_IP_ADDRESS: true  
	SERVICE_NAME: auth-server   
	
	USE_KAFKA: true  
	KAFKA_BOOTSTRAP_SERVERS: kafka:9092  
	USER_REGISTER_TOPIC: user-registration
```

---

## Использование API

### Регистрация пользователя

- **URL:** `/api/auth/register`
- **Метод:** POST
- **Тело запроса (JSON):**
  ``` json
  {
    "username": "exampleUser",
    "password": "examplePassword"
  }
  ```
- **Ответ:** UserAuthDto в формате JSON:
``` json
  {
    "username": "exampleUser",
    "role": "ROLE",
    "scope": "SCOPE1,SCOPE2,..."
  }
  ```

---

### Аутентификация (вход)

- **URL:** `/api/auth/login`
- **Метод:** POST
- **Тело запроса (JSON):**
  ``` json
  {
    "username": "exampleUser",
    "password": "examplePassword"
  }
  ```
- **Ответ:** JWT токен в формате JSON:
  ``` json
  {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

---
## Регистрация с выбором роли (кроме ADMIN)

- **URL:** `/api/auth/register/by?role={role}`
- **Метод:** POST
- **Параметры:**
    - `role` (обязательный) - роль пользователя (например, "MODERATOR")
- **Тело запроса (JSON):**
``` json
    {   
	    "username": "moderatorUser",  
	    "password": "moderatorPass" 
	}
```
- - **Ответ:** UserAuthDto в формате JSON:
``` json
  {
    "username": "moderatorUser",
    "role": "MODERATOR",
    "scope": "SCOPE1,SCOPE2,..."
  }
  ```
- **Ограничения:**  
    Роль не может быть "ADMIN" и должна существовать в системе

---
## Обновление токенов

- **URL:** `/api/auth/refresh`
- **Метод:** POST
- **Параметры:**
    - `refreshBoth` (опциональный) - если `true`, обновляет оба токена
- **Тело запроса (JSON):**
``` json
{   
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." 
}
```
- **Ответ:**
``` json
{   
    "accessToken": "new_access_token",
    "refreshToken": "new_refresh_token" 
}
```

---
## Отзыв refresh токена

- **URL:** `/api/auth/logout`
- **Метод:** POST
- **Тело запроса (JSON):**
``` json
{   
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." 
}
```
- **Ответ:** `"Logout successful"`

---
## Проверка истечения срока токена

- **URL:** `/api/auth/expired`
- **Метод:** POST
- **Тело запроса (JSON):**
``` json
{   
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." 
}
```
- **Ответ:** `true/false`

## 2. Административные эндпоинты (`/api/auth/admin`)

> **Требуется роль ADMIN и валидный JWT токен**

## Регистрация администратора

- **URL:** `/api/auth/admin/register`
- **Метод:** POST
- **Тело запроса (JSON):**
``` json
{   
	"username": "adminUser",  
	"password": "adminPass" 
}
```
- **Ответ:**
``` json
  {
    "username": "adminUser",
    "role": "ADMIN",
    "scope": "SCOPE1,SCOPE2,..."
  }
  ```

## Создание новой роли

- **URL:** `/api/auth/admin/role/new?role={role}`
- **Метод:** POST
- **Параметры:**
    - `role` (обязательный) - название новой роли
- **Ответ:** `"CREATED_ROLE_NAME"`

## Создание нового scope

- **URL:** `/api/auth/admin/scope/new?scope={scope}`
- **Метод:** POST
- **Параметры:**
    - `scope` (обязательный) - название нового scope
- **Ответ:** `"CREATED_SCOPE_NAME"`

## Проверка существования пользователя

- **URL:** `/api/auth/admin/exists/{username}`
- **Метод:** GET
- **Ответ:** `true/false`

## Получение информации о пользователе

- **URL:** `/api/auth/admin/get/user/{username}`
- **Метод:** GET
- **Ответ:**
``` json
{   
	"id": 1,
	"username": "targetUser",
	"role": "USER" 
}
```

---
## Схемы данных

## AuthRequest

``` json
{
	"username": "string",
	"password": "string" 
}
```

## AuthResponse

``` json
{   
	"accessToken": "string",  
	"refreshToken": "string" 
}
```

## RefreshTokenRequest:
``` json
{   
    "refreshToken": "string" 
}
```

### Защищенные ресурсы

Для доступа к защищенным эндпоинтам необходимо передавать JWT токен в заголовке `Authorization`:
``` 
Authorization: Bearer <...>
```

---

## Архитектурные детали

- **Spring Security** используется для настройки безопасности и фильтрации запросов.
- **JWT** применяется для создания и проверки токенов, что позволяет реализовать stateless аутентификацию.
- Сервисный слой обрабатывает бизнес-логику, включая создание пользователей и проверку учетных данных.
- Репозитории взаимодействуют с базой данных PostgreSQL.
- Конфигурация приложения вынесена в `application.yml` для удобства управления.

---

## Планы по развитию

- Интеграция с внешними OAuth2 провайдерами
- Улучшение логирования и мониторинга
- Развертывание в Kubernetes

---

## Контрибьютинг

Если хочешь помочь развитию проекта, пожалуйста, сделай форк, внеси изменения и отправь pull request. Буду рад вашим предложениям и исправлениям!

---

## Лицензия

Этот проект распространяется под лицензией MIT.

---

## Контакты

Если у тебя есть вопросы или предложения, можешь связаться со мной через GitHub: [BaiBeiCha](https://github.com/BaiBeiCha)

---

Если хочешь, могу помочь дополнительно с документацией по конкретным классам или функционалу - просто скажи!
