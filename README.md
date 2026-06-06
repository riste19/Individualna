# Бизнис Директориум (Business Directory)

Индивидуална проектна задача по предметот **Развој на мобилни апликации** (е-Учење клуч: `RMA2026`).

Android апликација во форма на бизнис директориум — прикажува компании поделени по
категории (Сервиси, Забава, Индустрија, Едукација), овозможува внес на нови компании,
пребарување по назив, чување во **оддалечена MySQL база преку PHP веб сервиси**, и
**геолокациски известувања** кога корисникот е близу до некоја компанија.

## Автор
- Ристе Семенковски, 102741 (индивидуална проектна задача)

## Технологии
| Дел | Технологија |
|-----|-------------|
| Мобилна апликација | **Java**, Android (Android Studio), View систем + ViewBinding |
| Серверски дел (backend) | **PHP + MySQL** (REST веб сервиси) |
| Мрежна комуникација | Retrofit + OkHttp + Gson (HTTP/JSON) |
| Геолокација | Android `LocationManager` |

## Функционалности по нивоа

### Задача 1 — основно ниво
- Application Bar со сопствена икона и наслов на апликацијата.
- `TabLayout` со четири категории: **Сервиси, Забава, Индустрија, Едукација**.
- Навигација меѓу табови со **клик** или **swipe** (лево/десно).
- `ListView` со прилагоден (custom) адаптер — секој ред: **лого, назив, адреса, телефон, веб страна**.

### Задача 2 — средно ниво
- Икона **„+"** крајно десно во барот → екран за **внес на нова компанија**:
  назив, адреса, географска ширина/должина, е-маил, телефон, веб страна и
  **избор на повеќе категории (checkbox)**.
- Копче **Зачувај** — ги снима податоците во база.
- Поле за **пребарување по назив** во рамки на тековно избраната категорија.

### Задача 3 — високо ниво
- Податоците се чуваат во **оддалечена MySQL база**, достапна преку **сопствени PHP веб сервиси** (REST).
- Внес (Зачувај) праќа **HTTP POST** до серверот; листите и пребарувањето читаат преку **HTTP GET**.
- **Геолокација:** додека апликацијата е отворена, прикажува **Toast** кога корисникот е на **< 50 м** од локацијата на компанија.

## Структура на проектот
```
Individualna/
├─ app/                      # Android апликација (Java)
│  └─ src/main/java/com/example/individualna/
│     ├─ MainActivity.java          # барот, табови, геолокација
│     ├─ AddCompanyActivity.java    # екран за внес (POST)
│     ├─ CategoryFragment.java      # листа по категорија + пребарување (GET)
│     ├─ CategoryPagerAdapter.java  # ViewPager2 страници
│     ├─ CompanyAdapter.java        # custom ListView адаптер
│     ├─ model/ (Company, Category)
│     └─ data/ (CompanyRepository, api/ApiClient, CompanyApi, DTOs)
└─ server/                   # Backend (PHP + MySQL)
   ├─ schema.sql             # креирање база + табела + почетни податоци
   ├─ db.php                 # PDO врска кон MySQL
   ├─ companies.php          # веб сервис (GET/POST)
   └─ README.md
```

## Како да се стартува

### 1. Backend (PHP + MySQL преку XAMPP)
1. Инсталирај и стартувај **XAMPP** → вклучи **Apache** и **MySQL**.
2. Копирај ги `server/db.php` и `server/companies.php` во `C:\xampp\htdocs\business-directory\`.
3. Во **phpMyAdmin** (`http://localhost/phpmyadmin`) → **Import** → избери `server/schema.sql` → **Go**.
4. Провери: `http://localhost/business-directory/companies.php` враќа JSON листа.

> Деталите се во [`server/README.md`](server/README.md).

### 2. Android апликација
1. Отвори го проектот во **Android Studio**.
2. Стартувај емулатор (или поврзи уред со овозможено USB debugging).
3. Притисни **Run ▶**.
4. Емулаторот пристапува до серверот преку `http://10.0.2.2/business-directory/`
   (поставено во `app/src/main/java/com/example/individualna/data/api/ApiClient.java`).
   За вистински оддалечен сервер, смени ја `BASE_URL` со адресата на хостот.

## Веб сервиси (REST endpoints)
| Метод | Патека | Опис |
|-------|--------|------|
| GET | `companies.php` | Сите компании |
| GET | `companies.php?category=SERVICES` | По категорија |
| GET | `companies.php?category=SERVICES&q=auto` | По категорија + пребарување по назив |
| POST | `companies.php` | Внес на нова компанија (JSON тело) |

Дозволени категории: `SERVICES`, `ENTERTAINMENT`, `INDUSTRY`, `EDUCATION`.

## Користени библиотеки
- **Retrofit** `2.11.0` + **converter-gson** — HTTP клиент и JSON (де)сериализација
- **OkHttp logging-interceptor** `4.12.0` — логирање на мрежни повици
- **AndroidX**: AppCompat, Material Components, ViewPager2, Fragment, ConstraintLayout, Core
- **JUnit**, **Espresso** — тестови
- Серверски: **PHP PDO**, **MySQL/MariaDB** (преку XAMPP)

## Референци
- Android Developers документација (Activities, Fragments, ViewPager2, Location).
- Retrofit / OkHttp / Gson официјална документација.
- Туторијали наведени во спецификацијата на задачата (ListView со custom adapter, TabLayout + ListView, filter/search).
