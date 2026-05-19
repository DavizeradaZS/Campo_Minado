package com.example.campominado

import com.google.gson.annotations.SerializedName

data class Jogador(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("nome") val nome: String,
    @SerializedName("idade") val idade: Int,
    @SerializedName("email") val email: String,
    @SerializedName("pontos") val pontos: Int = 0
)