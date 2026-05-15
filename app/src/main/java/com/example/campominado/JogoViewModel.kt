package com.example.campominado

import androidx.lifecycle.ViewModel

class JogoViewModel : ViewModel() {

    // Configurações iniciais da Fase 1
    var linhas = 4
    var colunas = 3
    var totalBombas = 4

    // Variável para checar se é o primeiro clique da partida
    var primeiroCliqueFeito = false

    // A nossa matriz principal
    lateinit var tabuleiro: Array<Array<Model.Celula>>

    init {
        iniciarNovoJogo()
    }

    fun iniciarNovoJogo() {
        primeiroCliqueFeito = false

        // Inicializa a matriz com células vazias
        tabuleiro = Array(linhas) { l ->
            Array(colunas) { c ->
                Model.Celula(linha = l, coluna = c)
            }
        }
    }

    fun processarClique(linha: Int, coluna: Int) {
        val celulaClicada = tabuleiro[linha][coluna]

        if (celulaClicada.isRevelada || celulaClicada.isMarcada) return

        if (!primeiroCliqueFeito) {
            gerarBombas(linha, coluna)
            calcularVizinhos()
            primeiroCliqueFeito = true
        }

        // Lógica de Derrota atualizada
        if (celulaClicada.isBomba) {
            revelarTodasAsBombas()
            // DICA: Mais tarde, você criará um aviso na tela (Toast ou Dialog) de "Você Perdeu!"
            return
        }

        abrirEspaco(linha, coluna)

        // Lógica de Vitória atualizada
        if (verificarVitoria()) {
            // DICA: Mais tarde, você chamará a função para gerar a Fase 2 aqui!
            println("O jogador venceu a fase!")
        }
    }

    private fun gerarBombas(linhaClicada: Int, colunaClicada: Int) {
        val totalCelulas = linhas * colunas
        val posicoesPossiveis = mutableListOf<Int>()

        // 1. Cria uma lista representando todos os quadrados (de 0 até 11, por exemplo)
        for (i in 0 until totalCelulas) {
            posicoesPossiveis.add(i)
        }

        // 2. Transforma a coordenada 2D do clique em um índice 1D e remove da lista
        val indexClicado = (linhaClicada * colunas) + colunaClicada
        posicoesPossiveis.remove(indexClicado)

        // 3. Embaralha a lista (O Kotlin já tem o .shuffle() nativo, bem prático!)
        posicoesPossiveis.shuffle()

        // 4. Pega os primeiros itens da lista embaralhada para plantar as bombas
        for (i in 0 until totalBombas) {
            val posicaoSorteada = posicoesPossiveis[i]

            // Converte o índice 1D de volta para coordenadas 2D (linha e coluna)
            val l = posicaoSorteada / colunas
            val c = posicaoSorteada % colunas

            // Planta a bomba
            tabuleiro[l][c].isBomba = true
        }
    }

    private fun calcularVizinhos() {
        for (l in 0 until linhas) {
            for (c in 0 until colunas) {
                // Só calculamos se a célula atual NÃO for uma bomba
                if (!tabuleiro[l][c].isBomba) {
                    var bombasAoRedor = 0

                    // Esse loop duplo (-1 a 1) cria um "quadrado" 3x3 ao redor da célula atual
                    for (i in -1..1) {
                        for (j in -1..1) {
                            val vizinhoLinha = l + i
                            val vizinhoColuna = c + j

                            // Verifica se o vizinho está dentro do mapa e se é uma bomba
                            if (isCoordenadaValida(vizinhoLinha, vizinhoColuna)) {
                                if (tabuleiro[vizinhoLinha][vizinhoColuna].isBomba) {
                                    bombasAoRedor++
                                }
                            }
                        }
                    }
                    // Salva o resultado na nossa classe modelo
                    tabuleiro[l][c].bombasVizinhas = bombasAoRedor
                }
            }
        }
    }

    // Função auxiliar para evitar o erro "IndexOutOfBounds" (tentar ler fora da matriz)
    private fun isCoordenadaValida(l: Int, c: Int): Boolean {
        return l >= 0 && l < linhas && c >= 0 && c < colunas
    }

    private fun abrirEspaco(l: Int, c: Int) {
        // 1. Condição de parada de segurança (fora do mapa)
        if (!isCoordenadaValida(l, c)) return

        val celula = tabuleiro[l][c]

        // 2. Condição de parada da lógica (já está aberta ou tem bandeira)
        if (celula.isRevelada || celula.isMarcada) return

        // 3. Ação: Revelamos a célula atual!
        celula.isRevelada = true

        // 4. A Recursividade: Se for um espaço vazio (0 bombas vizinhas), mandamos abrir os vizinhos
        if (celula.bombasVizinhas == 0 && !celula.isBomba) {
            for (i in -1..1) {
                for (j in -1..1) {
                    abrirEspaco(l + i, c + j) // A função chama ela mesma para o vizinho!
                }
            }
        }
    }


    fun alternarBandeira(linha: Int, coluna: Int) {
        val celula = tabuleiro[linha][coluna]

        // Só podemos colocar ou tirar bandeira se a célula estiver fechada
        if (!celula.isRevelada) {
            celula.isMarcada = !celula.isMarcada
        }
    }


    fun verificarVitoria(): Boolean {
        var celulasAbertas = 0
        val celulasSeguras = (linhas * colunas) - totalBombas

        for (l in 0 until linhas) {
            for (c in 0 until colunas) {
                // Conta apenas as células reveladas que não são bombas
                if (tabuleiro[l][c].isRevelada && !tabuleiro[l][c].isBomba) {
                    celulasAbertas++
                }
            }
        }

        return celulasAbertas == celulasSeguras
    }

    private fun revelarTodasAsBombas() {
        for (l in 0 until linhas) {
            for (c in 0 until colunas) {
                if (tabuleiro[l][c].isBomba) {
                    tabuleiro[l][c].isRevelada = true
                }
            }
        }
    }

}