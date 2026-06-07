# Поставување на бекендот на InfinityFree

Чекор-по-чекор за качување на PHP/MySQL бекендот на бесплатен јавен сервер
(InfinityFree) и поврзување на Android апликацијата со него.

InfinityFree е бесплатен, дава PHP + MySQL + бесплатен HTTPS поддомен + phpMyAdmin.
Не бара кредитна картичка.

---

## 1. Направи сметка и хостинг
1. Оди на <https://infinityfree.com> → **Sign Up** (или најави се).
2. Создади **нов хостинг акаунт** (Create Account). Избери бесплатен поддомен,
   на пр. `mojabaza.infinityfreeapp.com`, или закачи свој домен.
3. Запиши го **поддоменот** — тоа е јавната адреса на сајтот.

> Активирањето на сметката/SSL може да потрае неколку минути до ~час.

## 2. Создади MySQL база
1. Влези во **Control Panel** за акаунтот.
2. Отвори **MySQL Databases**.
3. Создади база со име `business_directory`. Системот ќе ѝ додаде префикс,
   па вистинското име ќе биде нешто како `if0_XXXXXXXX_business_directory`.
4. Запиши ги **четирите вредности** (ќе ти требаат за `db.php`):
   - **Host** — пр. `sqlXXX.infinityfree.com`
   - **Database name** — пр. `if0_XXXXXXXX_business_directory`
   - **Username** — пр. `if0_XXXXXXXX`
   - **Password** — лозинката од панелот

## 3. Внеси ја шемата преку phpMyAdmin
1. Во Control Panel отвори **phpMyAdmin** за новата база.
2. Во левата листа избери ја базата (`if0_..._business_directory`).
3. Картичка **Import** → **Choose File** → избери
   `server/schema_infinityfree.sql` од овој проект → **Go**.
4. Треба да се создаде табелата `companies` со 11 почетни записи.

> Користи `schema_infinityfree.sql`, НЕ `schema.sql`. Старата содржи
> `CREATE DATABASE`/`USE` што InfinityFree ги забранува.

## 4. Пополни ги податоците во `db.php`
Отвори `server/db.php` и замени ги четирите вредности од чекор 2:
```php
$DB_HOST = 'sqlXXX.infinityfree.com';
$DB_NAME = 'if0_XXXXXXXX_business_directory';
$DB_USER = 'if0_XXXXXXXX';
$DB_PASS = 'твојата-лозинка';
```

## 5. Качи ги PHP датотеките
Преку **Online File Manager** (во Control Panel) или преку FTP
(host: `ftpupload.net`, корисник/лозинка од панелот, на пр. со FileZilla):
1. Влези во папката **`htdocs/`** (коренот на сајтот).
2. Качи ги:
   - `server/db.php`
   - `server/companies.php`

   (Качи ги директно во `htdocs/`, не во потпапка — освен ако намерно сакаш
   патека како `htdocs/business-directory/`.)

## 6. Тест во прелистувач
Отвори во браузер:
```
https://<твој-поддомен>.infinityfreeapp.com/companies.php
```
Очекувано: **JSON низа** со 11-те компании. Пробај и филтри:
- `.../companies.php?category=EDUCATION`
- `.../companies.php?q=auto`

## 7. Поврзи ја апликацијата
1. Отвори `app/src/main/java/com/example/individualna/data/api/ApiClient.java`.
2. Постави `BASE_URL` на адресата од чекор 6 (коренот, со `/` на крај):
   ```java
   private static final String BASE_URL = "https://<твој-поддомен>.infinityfreeapp.com/";
   ```
3. Преведи (rebuild) и пушти ја апликацијата.

---

## Решавање проблеми
- **Браузерот покажува JSON, но апликацијата не вчитува ништо / добива HTML.**
  InfinityFree понекогаш сервира JavaScript „проверка на прелистувач" на не-браузер
  клиенти (OkHttp/Retrofit). Ако се случи тоа, премести го бекендот на хост без таа
  заштита — на пр. **000webhost**, или PHP+MySQL на **Railway**/**Render**. Кодот
  останува ист; се менуваат само `db.php` и `BASE_URL`.
- **`companies.php` дава 500 / „Неуспешна врска со базата".** Провери ги четирите
  вредности во `db.php` и дека шемата е увезена во точната база.
- **404 на `companies.php`.** Датотеките не се во `htdocs/`, или си во погрешна
  потпапка — усогласи ја патеката со `BASE_URL`.
- **Не сакаш да е јавна лозинката.** `db.php` со вистинските податоци не треба да се
  комитува во јавно репо — држи ги тие вредности само на серверот.
