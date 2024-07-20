package com.chapeaumoineau.miavortoj.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteLanguage(
    val iso: String,
    val timestamp: Long,
    @PrimaryKey val id: Int? = null
) {

    companion object {
        fun getIsos(list: List<FavoriteLanguage>): ArrayList<String> {
            var myIsos = arrayListOf<String>()
            for (favorite in list) {
                myIsos.add(favorite.iso)
            }
            return myIsos
        }
    }
}