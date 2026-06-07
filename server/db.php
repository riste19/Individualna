<?php
// PDO врска кон MySQL базата.
//
// === ПРОДУКЦИЈА: InfinityFree ===
// Пополни ги овие четири вредности од контролниот панел на InfinityFree
// (Control Panel -> MySQL Databases). Изгледаат вака:
//   host:  sqlXXX.infinityfree.com
//   name:  if0_XXXXXXXX_business_directory   (фиксен префикс, не може да се смени)
//   user:  if0_XXXXXXXX
//   pass:  (прикажана во панелот)
$DB_HOST = 'sqlXXX.infinityfree.com';            // <-- замени
$DB_NAME = 'if0_XXXXXXXX_business_directory';     // <-- замени
$DB_USER = 'if0_XXXXXXXX';                        // <-- замени
$DB_PASS = 'ВАШАТА_ЛОЗИНКА';                       // <-- замени

// === ЛОКАЛЕН РАЗВОЈ: XAMPP ===
// За тестирање на локална машина откоментирај ги овие наместо горните.
// $DB_HOST = '127.0.0.1';
// $DB_NAME = 'business_directory';
// $DB_USER = 'root';
// $DB_PASS = '';

try {
    $pdo = new PDO(
        "mysql:host=$DB_HOST;dbname=$DB_NAME;charset=utf8mb4",
        $DB_USER,
        $DB_PASS,
        [
            PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
            PDO::ATTR_EMULATE_PREPARES   => false,
        ]
    );
} catch (PDOException $e) {
    http_response_code(500);
    header('Content-Type: application/json; charset=utf-8');
    echo json_encode(['error' => 'Неуспешна врска со базата: ' . $e->getMessage()]);
    exit;
}
