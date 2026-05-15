package com.example.campominado

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// O Adapter recebe a lista de células e duas funções para lidar com os cliques (curto e longo)
class TabuleiroAdapter(
    private var celulas: List<Model.Celula>,
    private val onClick: (linha: Int, coluna: Int) -> Unit,
    private val onLongClick: (linha: Int, coluna: Int) -> Unit
) : RecyclerView.Adapter<TabuleiroAdapter.CelulaViewHolder>() {

    // Essa classe interna acha as referências do seu arquivo XML (o TextView que criamos)
    class CelulaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textoCelula: TextView = view.findViewById(R.id.textoCelula)
    }

    // Aqui o Android "infla" (transforma o XML em tela real) cada quadradinho
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CelulaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_celula, parent, false)
        return CelulaViewHolder(view)
    }

    // Essa é a função mais importante: ela "pinta" o quadrado de acordo com as regras
    override fun onBindViewHolder(holder: CelulaViewHolder, position: Int) {
        val celula = celulas[position]

        // Estado padrão (Fechado)
        holder.textoCelula.text = ""
        holder.itemView.setBackgroundColor(Color.parseColor("#DDDDDD")) // Cinza escuro

        if (celula.isRevelada) {
            // Estado Aberto
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF")) // Branco (caminho livre)

            if (celula.isBomba) {
                holder.textoCelula.text = "💣"
                holder.itemView.setBackgroundColor(Color.parseColor("#FFCCCC")) // Fundo vermelho para a bomba
            } else if (celula.bombasVizinhas > 0) {
                holder.textoCelula.text = celula.bombasVizinhas.toString()
                // Dica: No futuro, você pode colocar um IF aqui para mudar a cor do texto dependendo do número!
            }
        } else if (celula.isMarcada) {
            // Estado com Bandeira
            holder.textoCelula.text = "🚩"
        }

        // Dispara a função de clique normal (Abrir a célula)
        holder.itemView.setOnClickListener {
            onClick(celula.linha, celula.coluna)
        }

        // Dispara a função de clique longo (Colocar/Tirar Bandeira)
        holder.itemView.setOnLongClickListener {
            onLongClick(celula.linha, celula.coluna)
            true // Retorna true para avisar o Android que o clique longo já resolveu a ação
        }
    }

    // Diz ao Android quantos quadrados existem no total
    override fun getItemCount(): Int = celulas.size

    // Usaremos esta função para avisar a tela quando a matriz mudar
    fun atualizarTabuleiro(novasCelulas: List<Model.Celula>) {
        celulas = novasCelulas
        notifyDataSetChanged()
    }
}