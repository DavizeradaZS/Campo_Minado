package com.example.campominado

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.campominado.ApiService
import com.example.campominado.Jogador
import kotlinx.coroutines.launch

class CadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val etNome = findViewById<EditText>(R.id.etNome)
        val etIdade = findViewById<EditText>(R.id.etIdade)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnCadastrar = findViewById<Button>(R.id.btnCadastrar)

        val api = ApiService.criar()

        btnCadastrar.setOnClickListener {
            val nome = etNome.text.toString()
            val idadeTexto = etIdade.text.toString()
            val email = etEmail.text.toString()

            // Validação simples
            if (nome.isNotEmpty() && idadeTexto.isNotEmpty() && email.isNotEmpty()) {
                val idade = idadeTexto.toInt()
                val novoJogador = Jogador(nome = nome, idade = idade, email = email)

                // Chamada de rede usando Coroutines (não bloqueia o ecrã)
                lifecycleScope.launch {
                    try {
                        val resposta = api.cadastrarJogador(novoJogador)
                        if (resposta.isSuccessful) {
                            // Sucesso! Avançar para o Jogo
                            val intent = Intent(this@CadastroActivity, MainActivity::class.java)
                            // Passamos o nome do jogador para a próxima activity
                            intent.putExtra("NOME_JOGADOR", nome)
                            startActivity(intent)
                            finish() // Fecha o ecrã de cadastro para não voltar ao clicar no botão "Voltar"
                        } else {
                            Toast.makeText(this@CadastroActivity, "Erro no registo no servidor.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@CadastroActivity, "Erro de ligação: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}