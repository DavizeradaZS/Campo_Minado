<?php
header("Content-Type: application/json; charset=UTF-8");
include_once 'conexao.php';

// Pega o JSON enviado pelo Android (Retrofit)
$dados = json_decode(file_get_contents("php://input"));
if(!empty($dados->nome) && !empty($dados->idade) && !empty($dados->email)) {
    // Tenta inserir o jogador, se o nome já existir, ele não zera os pontos, apenas ignora
    $query = "INSERT IGNORE INTO jogadores (nome, idade, email, pontos) VALUES (:nome, :idade, :email, 0)";
    $stmt = $conn->prepare($query);

    $stmt->bindParam(":nome", $dados->nome);
    $stmt->bindParam(":idade", $dados->idade);
    $stmt->bindParam(":email", $dados->email);

    if($stmt->execute()) {
        http_response_code(201);
        // Created
        echo json_encode(["mensagem" => "Jogador cadastrado/identificado com sucesso."]);
    } else {
        http_response_code(503);
        // Service Unavailable
        echo json_encode(["mensagem" => "Não foi possível cadastrar o jogador."]);
    }
} else {
    http_response_code(400); // Bad Request
    echo json_encode(["mensagem" => "Dados incompletos."]);
}
?>