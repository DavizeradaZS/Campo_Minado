<?php
$host = "thyagoquintas.com.br"; 
$db_name = "engenharia_191"; 
$username = "engenharia_191"; 
$password = "loboguara";     

try {
    $conn = new PDO("mysql:host={$host};dbname={$db_name}", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch(PDOException $exception) {
    echo "Erro de ligação: " . $exception->getMessage();
    exit;
}
?>