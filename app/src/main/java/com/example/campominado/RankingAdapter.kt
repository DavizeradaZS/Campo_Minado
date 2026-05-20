package com.example.campominado

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campominado.Jogador

class RankingAdapter(private var jogadores: List<Jogador>) : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    class RankingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPosicao: TextView = view.findViewById(R.id.tvPosicao)
        val tvNomeRanking: TextView = view.findViewById(R.id.tvNomeRanking)
        val tvPontosRanking: TextView = view.findViewById(R.id.tvPontosRanking)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val jogador = jogadores[position]

        holder.tvPosicao.text = "${position + 1}º"
        holder.tvNomeRanking.text = jogador.nome
        holder.tvPontosRanking.text = "${jogador.pontos} pts"
    }

    override fun getItemCount(): Int = jogadores.size

    // Função para atualizar a lista quando os dados chegarem da API
    fun atualizarDados(novaLista: List<Jogador>) {
        jogadores = novaLista
        notifyDataSetChanged()
    }
}