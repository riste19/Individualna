<?php
// PDO врска кон MySQL базата.
//
// === ЛОКАЛЕН РАЗВОЈ: XAMPP (активна конфигурација) ===
// Стандардни XAMPP параметри: корисник `root`, без лозинка, локална база.
$DB_HOST = '127.0.0.1';
$DB_NAME = 'business_directory';
$DB_USER = 'root';
$DB_PASS = '';

// === ОДДАЛЕЧЕН ХОСТ (опционо) ===
// Ако сакаш да го качиш бекендот на PHP/MySQL хост, откоментирај ги и пополни ги
// овие наместо горните. Внимавај: лозинката НЕ треба да се комитува на GitHub.
// (Види ја белешката за обидот со InfinityFree во server/README.md.)
// $DB_HOST = 'sqlXXX.example-host.com';
// $DB_NAME = 'ime_na_baza';
// $DB_USER = 'korisnik';
// $DB_PASS = 'lozinka';

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
