package com.example.campominado

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campominado.ApiService
import kotlinx.coroutines.launch

class RankingActivity : AppCompatActivity() {

    private lateinit var adapter: RankingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        val rvRanking = findViewById<RecyclerView>(R.id.rvRanking)
        val btnVoltarRanking = findViewById<Button>(R.id.btnVoltarRanking)

        // Configura o RecyclerView como uma lista vertical linear padrão
        rvRanking.layoutManager = LinearLayoutManager(this)

        // Inicializa o adapter com uma lista vazia
        adapter = RankingAdapter(emptyList())
        rvRanking.adapter = adapter

        // Procede ao carregamento dos dados do servidor PHP
        carregarRanking()

        // Fecha este ecrã e volta para o ecrã anterior (MainActivity)
        btnVoltarRanking.setOnClickListener {
            finish()
        }
    }

    private fun carregarRanking() {
        val api = ApiService.criar()

        lifecycleScope.launch {
            try {
                val resposta = api.obterRanking()
                if (resposta.isSuccessful && resposta.body() != null) {
                    // Atualiza o adapter com a lista ordenada vinda do servidor
                    adapter.atualizarDados(resposta.body()!!)
                } else {
                    Toast.makeText(this@RankingActivity, "Erro ao obter o ranking do servidor.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@RankingActivity, "Erro de ligação: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}