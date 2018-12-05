package fr.epita.android.hellogames

import java.util.*

class GameDetails (val id : Int?,
                   val name : String?,
                   val type : String?,
                   val players : Int?,
                   val year : Int?,
                   val url : String?,
                   val description_fr : String?,
                   val description_en: String?) {
    override fun toString(): String {
        return "GameDetails(id=$id, name=$name, type=$type, players=$players, year=$year, url=$url, description_fr=$description_fr, description_en=$description_en)"
    }
}