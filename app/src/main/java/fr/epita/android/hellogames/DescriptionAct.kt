package fr.epita.android.hellogames

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_description.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Intent
import android.net.Uri


class DescriptionAct : AppCompatActivity() {
    fun getImageId(id: Int): Int =
        when (id) {
            1 -> R.drawable.tictactoe
            2 -> R.drawable.hangman
            3 -> R.drawable.sudoku
            4 -> R.drawable.battleship
            5 -> R.drawable.minesweeper
            6 -> R.drawable.gameoflife
            7 -> R.drawable.memory
            8 -> R.drawable.simon
            9 -> R.drawable.slidingpuzzle
            10 -> R.drawable.mastermind
            else -> R.drawable.bug
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)


        val baseURL = "https://androidlessonsapi.herokuapp.com/api/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()

        val service = retrofit.create(WebServiceInterface::class.java)

        var dataDetails : GameDetails
        val gameid = intent.getIntExtra("gameid", 11)

        var callDetails = object : Callback<GameDetails> {
            override fun onFailure(call: Call<GameDetails>?, t: Throwable?) {
                Log.d("TAG", "Webservice call failed")
            }

            override fun onResponse(
                call: Call<GameDetails>?,
                response: Response<GameDetails>?
            ) {
                Log.d("TAG", "WebService success : ")
                if (response != null) {
                    if (response.code() == 200) {
                        var responseData = response.body()
                        if (responseData != null) {
                            dataDetails = responseData

                            var drawable = resources.getDrawable(getImageId(gameid))
                            JeuxImage.setImageDrawable(drawable)

                            NameV.text = dataDetails.name
                            TypeV.text = dataDetails.type
                            NBplayersV.text = dataDetails.players.toString()
                            YearV.text = dataDetails.year.toString()

                            JeuxDescription.text = dataDetails.description_en

                            button.setOnClickListener {
                                var uri = Uri.parse("${dataDetails.url}") // missing 'http://' will cause crashed
                                var intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent) }
                        }
                    }
                }
            }
        }
        Log.d("TAG", "gameid = "+gameid)
        service.getGameDetails(gameid).enqueue(callDetails)
    }
}

