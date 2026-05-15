package com.example.campominado

class Model {

    data class Celula(
        val linha: Int,
        val coluna: Int,
        var isBomba: Boolean = false,
        var isRevelada: Boolean = false,
        var isMarcada: Boolean = false, // Indica se o usuário colocou uma bandeira
        var bombasVizinhas: Int = 0
    )
}