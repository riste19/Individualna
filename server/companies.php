<?php
// Веб сервис за компаниите.
//   GET  companies.php?category=SERVICES&q=auto  -> листа (JSON)
//   POST companies.php   (JSON тело)             -> внес на нова компанија
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

require __DIR__ . '/db.php';

$method = $_SERVER['REQUEST_METHOD'];
$RAW_INPUT = file_get_contents('php://input');

if ($method === 'OPTIONS') {
    http_response_code(204);
    exit;
}

/** Претвора ред од базата во објект за JSON (категориите како низа). */
function row_to_company(array $r): array {
    $cats = ($r['categories'] !== null && $r['categories'] !== '')
        ? explode(',', $r['categories'])
        : [];
    return [
        'id'        => (int) $r['id'],
        'name'      => $r['name'],
        'address'   => $r['address'] ?? '',
        'latitude'  => $r['latitude'] !== null ? (float) $r['latitude'] : null,
        'longitude' => $r['longitude'] !== null ? (float) $r['longitude'] : null,
        'email'     => $r['email'] ?? '',
        'phone'     => $r['phone'] ?? '',
        'website'   => $r['website'] ?? '',
        'categories' => $cats,
    ];
}

if ($method === 'GET') {
    $category = isset($_GET['category']) ? strtoupper(trim($_GET['category'])) : '';
    $q        = isset($_GET['q']) ? trim($_GET['q']) : '';

    $sql    = 'SELECT * FROM companies';
    $conds  = [];
    $params = [];

    if ($q !== '') {
        $conds[] = 'name LIKE :q';
        $params[':q'] = '%' . $q . '%';
    }
    if ($category !== '') {
        // categories е низа одвоена со запирка -> FIND_IN_SET за филтрирање по категорија
        $conds[] = 'FIND_IN_SET(:cat, categories)';
        $params[':cat'] = $category;
    }
    if (!empty($conds)) {
        $sql .= ' WHERE ' . implode(' AND ', $conds);
    }
    $sql .= ' ORDER BY name ASC';

    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $rows = $stmt->fetchAll();

    echo json_encode(array_map('row_to_company', $rows), JSON_UNESCAPED_UNICODE);
    exit;
}

if ($method === 'POST') {
    $body = json_decode($RAW_INPUT, true);
    if (!is_array($body)) {
        http_response_code(400);
        echo json_encode(['error' => 'Невалиден JSON.'], JSON_UNESCAPED_UNICODE);
        exit;
    }

    $name       = isset($body['name']) ? trim($body['name']) : '';
    $categories = (isset($body['categories']) && is_array($body['categories'])) ? $body['categories'] : [];

    if ($name === '') {
        http_response_code(400);
        echo json_encode(['error' => 'Полето "name" е задолжително.'], JSON_UNESCAPED_UNICODE);
        exit;
    }
    if (count($categories) === 0) {
        http_response_code(400);
        echo json_encode(['error' => 'Изберете барем една категорија.'], JSON_UNESCAPED_UNICODE);
        exit;
    }

    $catStr = implode(',', array_map('strtoupper', $categories));

    $stmt = $pdo->prepare(
        'INSERT INTO companies (name, address, latitude, longitude, email, phone, website, categories)
         VALUES (:name, :address, :lat, :lon, :email, :phone, :website, :categories)'
    );
    $stmt->execute([
        ':name'       => $name,
        ':address'    => $body['address'] ?? '',
        ':lat'        => isset($body['latitude'])  && $body['latitude']  !== null ? $body['latitude']  : null,
        ':lon'        => isset($body['longitude']) && $body['longitude'] !== null ? $body['longitude'] : null,
        ':email'      => $body['email'] ?? '',
        ':phone'      => $body['phone'] ?? '',
        ':website'    => $body['website'] ?? '',
        ':categories' => $catStr,
    ]);

    $id = (int) $pdo->lastInsertId();
    $stmt = $pdo->prepare('SELECT * FROM companies WHERE id = :id');
    $stmt->execute([':id' => $id]);
    $row = $stmt->fetch();

    http_response_code(201);
    echo json_encode(row_to_company($row), JSON_UNESCAPED_UNICODE);
    exit;
}

http_response_code(405);
echo json_encode(['error' => 'Методот не е дозволен.'], JSON_UNESCAPED_UNICODE);
