-- Шема + почетни податоци за InfinityFree (и кој било хост со веќе создадена база).
--
-- ВАЖНО: за разлика од schema.sql, ОВДЕ НЕМА `CREATE DATABASE` ниту `USE`.
-- На InfinityFree базата веќе постои со фиксно име (пр. if0_XXXXXXXX_business_directory)
-- и се избира во phpMyAdmin пред Import. Само внеси ја табелата + податоците.
--
-- Како: Control Panel -> phpMyAdmin -> избери ја базата -> картичка "Import" ->
--       избери ја оваа датотека -> "Go".

CREATE TABLE IF NOT EXISTS companies (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(255) NOT NULL,
  address    VARCHAR(255),
  latitude   DOUBLE NULL,
  longitude  DOUBLE NULL,
  email      VARCHAR(255),
  phone      VARCHAR(64),
  website    VARCHAR(255),
  categories VARCHAR(255)            -- категории одвоени со запирка, пр. "EDUCATION,SERVICES"
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Почетни (seed) податоци
INSERT INTO companies (name, address, latitude, longitude, email, phone, website, categories) VALUES
('AutoFix Сервис', 'ул. Партизанска 12, Скопје', 41.9981, 21.4254, 'info@autofix.mk', '+389 70 123 456', 'www.autofix.mk', 'SERVICES'),
('Кристал Чистење', 'бул. Илинден 45, Скопје', 42.0010, 21.4090, 'kontakt@kristal.mk', '+389 71 998 221', 'www.kristal-cisteneje.mk', 'SERVICES'),
('Сигурност Обезбедување', 'ул. Васил Главинов 8, Скопје', 41.9965, 21.4317, 'info@sigurnost.mk', '+389 2 312 4500', 'www.sigurnost.mk', 'SERVICES'),
('Cinema City', 'ТЦ Сити Мол, Скопје', 41.9870, 21.4140, 'info@cinemacity.mk', '+389 2 309 0000', 'www.cinemacity.mk', 'ENTERTAINMENT'),
('Club Rhythm', 'Кеј 13 Ноември 5, Скопје', 41.9960, 21.4380, 'booking@clubrhythm.mk', '+389 78 333 777', 'www.clubrhythm.mk', 'ENTERTAINMENT'),
('FunZone Забавен Парк', 'бул. Јане Сандански 110, Скопје', 41.9890, 21.4660, 'fun@funzone.mk', '+389 72 121 343', 'www.funzone.mk', 'ENTERTAINMENT'),
('МеталПро Фабрика', 'Индустриска зона Бунарџик бб', 42.0450, 21.4200, 'office@metalpro.mk', '+389 2 255 8000', 'www.metalpro.mk', 'INDUSTRY'),
('ЕнергоМак', 'ул. Лондонска 4, Скопје', 41.9930, 21.4280, 'info@energomak.mk', '+389 2 310 2020', 'www.energomak.mk', 'INDUSTRY'),
('Универзитет ФИНКИ', 'ул. Руѓер Бошковиќ 16, Скопје', 42.0040, 21.4090, 'kontakt@finki.ukim.mk', '+389 2 309 9100', 'www.finki.ukim.mk', 'EDUCATION'),
('Академија КодЛаб', 'ул. Македонија 11, Скопје', 41.9950, 21.4320, 'info@kodlab.mk', '+389 76 200 300', 'www.kodlab.mk', 'EDUCATION'),
('ИТ Едукативен Центар', 'ул. Никола Вапцаров 3, Скопје', 41.9920, 21.4250, 'info@it-edu.mk', '+389 70 808 909', 'www.it-edu.mk', 'EDUCATION,SERVICES');
