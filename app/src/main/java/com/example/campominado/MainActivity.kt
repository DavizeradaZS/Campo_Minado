package com.example.campominado

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    // Instancia o ViewModel para sobreviver às rotações de tela
    private val viewModel: JogoViewModel by viewModels()
    private lateinit var adapter: TabuleiroAdapter
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        val rvTabuleiro = findViewById<RecyclerView>(R.id.rvTabuleiro)
        val btnReiniciar = findViewById<Button>(R.id.btnReiniciar)

        // Configura o RecyclerView como uma grade (Grid) usando o número de colunas do ViewModel
        rvTabuleiro.layoutManager = GridLayoutManager(this, viewModel.colunas)

        // Inicializa o Adapter e define o que acontece quando o jogador clica na tela
        adapter = TabuleiroAdapter(
            celulas = obterCelulasEmLista(),
            onClick = { linha, coluna ->
                // Só processa o clique se o jogo não acabou
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

        // Botão para resetar e tentar de novo
        btnReiniciar.setOnClickListener {
            viewModel.iniciarNovoJogo()
            tvStatus.text = "Campo Minado"
            atualizarTela()
        }
    }

    // O Adapter precisa de uma lista 1D. Essa função transforma a matriz 2D em lista.
    private fun obterCelulasEmLista(): List<Model.Celula> {
        val lista = mutableListOf<Model.Celula>()
        for (l in 0 until viewModel.linhas) {
            for (c in 0 until viewModel.colunas) {
                lista.add(viewModel.tabuleiro[l][c])
            }
        }
        return lista
    }

    // Verifica se o jogo acabou e atualiza os botões do mapa
    private fun atualizarTela() {
        val listaAtualizada = obterCelulasEmLista()
        adapter.atualizarTabuleiro(listaAtualizada)

        // Verifica a Derrota (se existe alguma bomba que está revelada)
        val perdeu = listaAtualizada.any { it.isBomba && it.isRevelada }
        if (perdeu) {
            tvStatus.text = "💥 Você Perdeu! 💥"
            return
        }

        // Verifica a Vitória
        if (viewModel.verificarVitoria()) {
            tvStatus.text = "🏆 Você Venceu! 🏆"
            // Aqui, no futuro, você pode mudar viewModel.linhas e colunas e iniciar a Fase 2!
        }
    }
}