package fr.epita.android.hellogames

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServiceInterface {
    @GET("game/details")
    fun getGameDetails(@Query("game_id") game_id: Int) : Call<GameDetails>

    @GET ("game/list")
    fun getGameList() : Call<List<Game>>

}