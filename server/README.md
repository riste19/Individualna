# Business Directory — Backend (PHP + MySQL)

Серверски дел (веб сервис) за бизнис директориумот, изработен во **PHP** со **MySQL** база.

## Поставување со XAMPP

1. Инсталирај **XAMPP** и стартувај **Apache** и **MySQL** од XAMPP Control Panel.
2. Копирај ги `db.php` и `companies.php` во:
   `C:\xampp\htdocs\business-directory\`
3. Креирај ја базата: отвори **phpMyAdmin** (`http://localhost/phpmyadmin`) →
   таб **Import** → избери `schema.sql` → **Go**.
   (Алтернативно од конзола: `mysql -u root < schema.sql`.)
4. Тест во прелистувач: `http://localhost/business-directory/companies.php`
   треба да врати JSON листа со компаниите.

Android **емулаторот** пристапува до овој сервер преку `http://10.0.2.2/business-directory/`
(тоа е веќе поставено во `ApiClient.java`).

## Endpoints (веб сервиси)

| Метод | Патека | Опис |
|-------|--------|------|
| GET | `companies.php` | Сите компании |
| GET | `companies.php?category=SERVICES` | По категорија |
| GET | `companies.php?category=SERVICES&q=auto` | По категорија + пребарување по назив |
| POST | `companies.php` | Внес на нова компанија (JSON тело) |

Категории: `SERVICES`, `ENTERTAINMENT`, `INDUSTRY`, `EDUCATION`.
Компанија со повеќе категории се чува како `"EDUCATION,SERVICES"`.

### Пример POST тело
```json
{
  "name": "Нова Компанија",
  "address": "ул. Пример 1, Скопје",
  "latitude": 41.9981,
  "longitude": 21.4254,
  "email": "info@example.mk",
  "phone": "+389 70 000 000",
  "website": "www.example.mk",
  "categories": ["SERVICES", "EDUCATION"]
}
```

## Конфигурација на базата
Кориснички податоци за базата се во `db.php` (стандардно XAMPP: `root` без лозинка).

## Деплој на оддалечен сервер (обид — не успеа со бесплатни хостови)
Бекендот е наменет да се пушта **локално преку XAMPP** (горе). Обидите за бесплатно
онлајн хостирање не успеаја:

- **InfinityFree** — PHP и MySQL се качија и работат во прелистувач, но бесплатниот
  план има JavaScript заштита против ботови (`aes.js`) што враќа HTML „checking your
  browser" страница на клиенти што не извршуваат JavaScript. Android апликацијата
  (OkHttp/Retrofit) затоа добива HTML наместо JSON и API повиците не успеваат.
  Потврдено со `curl -A okhttp` → се враќа HTML, не JSON. Не може да се исклучи.
- **000webhost** и слични веќе немаат бесплатен план.

Артефакти од обидот (оставени за референца): `DEPLOY_INFINITYFREE.md` (чекори) и
`schema_infinityfree.sql` (шема без `CREATE DATABASE`/`USE`, за хост со веќе создадена
база). Ако се користи платен/друг хост без таква заштита, доволно е да се пополни
`db.php` и да се смени `BASE_URL` во `app/.../data/api/ApiClient.java`.
