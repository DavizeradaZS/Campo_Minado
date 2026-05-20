package com.example.campominado

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: JogoViewModel by viewModels()
    private lateinit var adapter: TabuleiroAdapter
    private lateinit var tvStatus: TextView

    // Novas variáveis para os componentes visuais
    private lateinit var tvNomeJogador: TextView
    private lateinit var tvQtdBombas: TextView

    // Variável para guardar o nome do jogador atual
    private var nomeAtual: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        tvNomeJogador = findViewById(R.id.tvNomeJogador)
        tvQtdBombas = findViewById(R.id.tvQtdBombas)
        val rvTabuleiro = findViewById<RecyclerView>(R.id.rvTabuleiro)
        val btnReiniciar = findViewById<Button>(R.id.btnReiniciar)

        // Recupera o nome do jogador passado pelo Intent
        nomeAtual = intent.getStringExtra("NOME_JOGADOR") ?: "Anônimo"
        tvNomeJogador.text = "Jogador: $nomeAtual"

        // No futuro, se a quantidade de bombas for dinâmica, você pode pegar direto do ViewModel
        tvQtdBombas.text = "Bombas: 4"

        rvTabuleiro.layoutManager = GridLayoutManager(this, viewModel.colunas)

        adapter = TabuleiroAdapter(
            celulas = obterCelulasEmLista(),
            onClick = { linha, coluna ->
                if (tvStatus.text == "Campo Minado") {
                    viewModel.processarClique(linha, coluna)
                    atualizarTela()
                }
            },
            onLongClick = { linha, coluna ->
                if (tvStatus.text == "Campo Minado") {
                    viewModel.alternarBandeira(linha, coluna)
                    atualizarTela()
                }
            }
        )
        rvTabuleiro.adapter = adapter

        btnReiniciar.setOnClickListener {
            viewModel.iniciarNovoJogo()
            tvStatus.text = "Campo Minado"
            atualizarTela()
        }

        val btnVerRanking = findViewById<Button>(R.id.btnVerRanking)

        btnVerRanking.setOnClickListener {
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun obterCelulasEmLista(): List<Model.Celula> {
        val lista = mutableListOf<Model.Celula>()
        for (l in 0 until viewModel.linhas) {
            for (c in 0 until viewModel.colunas) {
                lista.add(viewModel.tabuleiro[l][c])
            }
        }
        return lista
    }

    private fun atualizarTela() {
        val listaAtualizada = obterCelulasEmLista()
        adapter.atualizarTabuleiro(listaAtualizada)

        val perdeu = listaAtualizada.any { it.isBomba && it.isRevelada }
        if (perdeu) {
            tvStatus.text = "💥 Você Perdeu! 💥"
            return
        }

        if (viewModel.verificarVitoria()) {
            tvStatus.text = "🏆 Você Venceu! 🏆"
            enviarPontuacao()
        }
    }

    // NOVA FUNÇÃO: Envia os pontos para o servidor
    private fun enviarPontuacao() {
        val api = ApiService.criar()

        // Cria um objeto Jogador apenas com os dados necessários para atualizar os pontos
        // Como o PHP precisa saber quem é, enviamos o nome e os pontos ganhos nesta etapa (ex: 10 pontos)
        val jogadorAtualizacao = Jogador(nome = nomeAtual, idade = 0, email = "", pontos = 10)

        lifecycleScope.launch {
            try {
                val resposta = api.atualizarPontos(jogadorAtualizacao)
                if (resposta.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Pontos salvos no ranking!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Erro ao salvar pontuação.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Sem conexão para salvar pontos.", Toast.LENGTH_SHORT).show()
            }
        }


    }
}