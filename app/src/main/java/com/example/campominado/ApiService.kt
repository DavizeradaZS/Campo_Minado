package com.example.campominado

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    // Rota para registar um novo jogador
    @POST("cadastrar_jogador.php")
    suspend fun cadastrarJogador(@Body jogador: Jogador): Response<Jogador>

    // Rota para obter a lista com o ranking dos jogadores
    @GET("obter_ranking.php")
    suspend fun obterRanking(): Response<List<Jogador>>

    // Rota para atualizar a pontuação no servidor após concluir uma etapa
    @PUT("atualizar_pontos.php")
    suspend fun atualizarPontos(@Body jogador: Jogador): Response<Jogador>

    // Inicializador do Retrofit embutido na própria interface
    companion object {
        // Substitua pelo domínio real onde colocou a pasta "api_campominado"
        // Tem de terminar sempre com a barra "/"
        private const val BASE_URL = "http://10.0.2.2:8081/api_campominado/"
        fun criar(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}