package fr.epita.android.hellogames

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.ImageView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {


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
    val mapGame = HashMap<ImageView, Int?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fun onClick(gameid: Int?) : Intent
            {
                return Intent(this@MainActivity, DescriptionAct::class.java).apply {
                    putExtra("gameid", gameid) }
            }

        ImageHautGauche.setOnClickListener { startActivity(onClick(mapGame[ImageHautGauche])) }
        ImageBasGauche.setOnClickListener { startActivity(onClick(mapGame[ImageBasGauche])) }
        ImageHautDroite.setOnClickListener { startActivity(onClick(mapGame[ImageHautDroite])) }
        ImageBasDroite.setOnClickListener { startActivity(onClick(mapGame[ImageBasDroite])) }


        val dataGame = arrayListOf<Game>()

        val baseURL = "https://androidlessonsapi.herokuapp.com/api/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()

        val service = retrofit.create(WebServiceInterface::class.java)


        val callList = object : Callback<List<Game>> {
            override fun onFailure(call: Call<List<Game>>?, t: Throwable?) {
                Log.d("TAG", "Webservice call failed")
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onResponse(
                call: Call<List<Game>>?,
                response: Response<List<Game>>?
            ) {
                if (response != null) {
                    if (response.code() == 200) {
                        val responseData = response.body()
                        if (responseData != null) {
                            dataGame.addAll(responseData)
                            Log.d("TAG", "WebService success : " + dataGame.toString())
                            for (i in 1..4) {
                                val rand = (0..dataGame.size-1).random()
                                val game = dataGame[rand]
                                val drawable = resources.getDrawable(getImageId(game.id), null)
                                if (i == 1){
                                    ImageHautGauche.setImageDrawable(drawable)
                                    mapGame.set(ImageHautGauche, game.id)
                                }
                                else if (i == 2)
                                {
                                    ImageBasGauche.setImageDrawable(drawable)
                                    mapGame.set(ImageBasGauche, game.id)
                                }
                                else if (i == 3) {
                                    ImageHautDroite.setImageDrawable(drawable)
                                    mapGame.set(ImageHautDroite, game.id)
                                }
                                else {
                                    ImageBasDroite.setImageDrawable(drawable);
                                    mapGame.set(ImageBasDroite, game.id)
                                }
                                dataGame.remove(game)
                            }
                        }
                    }
                }
            }
        }
        service.getGameList().enqueue(callList)
    }
}
