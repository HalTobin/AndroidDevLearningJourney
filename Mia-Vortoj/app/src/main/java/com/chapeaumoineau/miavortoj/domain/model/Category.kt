package com.chapeaumoineau.miavortoj.domain.model

import com.chapeaumoineau.miavortoj.R

data class Category(
    val title: String,
    val icon: Int,
    val text: Int,
    val id: Int,
    var translation: String = ""
    ) {

    /*fun setTranslation(newTranslation: String) {
        translation = newTranslation
    }*/

    companion object {
        val defaultCategories = listOf(
            Category( "Other", R.drawable.theme_other, R.string.theme_other, 1),
            Category("Numbers", R.drawable.theme_number, R.string.theme_number,2),
            Category("House", R.drawable.theme_house, R.string.theme_house,3),
            Category("Foods & Drinks", R.drawable.theme_food_drink, R.string.theme_food_drink,4),
            Category("Greetings", R.drawable.theme_greetings, R.string.theme_greetings,5),
            Category("Family", R.drawable.theme_family, R.string.theme_family,6),
            //Category("Religion", R.drawable.theme_religion, R.string.theme_religion,7),
            Category("Body", R.drawable.theme_body, R.string.theme_body,8),
            Category("Clothes", R.drawable.theme_clothes, R.string.theme_clothes,9),
            Category("Weather", R.drawable.theme_weather, R.string.theme_weather,10),
            Category("Date & Time", R.drawable.theme_date_time, R.string.theme_date_time,11),
            Category("Animal", R.drawable.theme_animal, R.string.theme_animal,12),
            Category("Space", R.drawable.theme_space, R.string.theme_space,13),
            Category("Nature", R.drawable.theme_nature, R.string.theme_nature,14),
            Category("Music", R.drawable.theme_music, R.string.theme_music,15),
            Category("Art", R.drawable.theme_art, R.string.theme_art,16),
            Category("Color", R.drawable.theme_color, R.string.theme_color,17),
            Category("Adjective", R.drawable.theme_adjective, R.string.theme_adjective,18),
            Category("Verb", R.drawable.theme_verb, R.string.theme_verb,19),
            Category("Communication", R.drawable.theme_communication, R.string.theme_communication,20),
            Category("Health", R.drawable.theme_health, R.string.theme_health,21),
            Category("Movies", R.drawable.theme_movie, R.string.theme_movie,22),
            Category("Work", R.drawable.theme_work, R.string.theme_work,23),
            Category("Sport", R.drawable.theme_sport, R.string.theme_sport,24),
            Category("Country & Language", R.drawable.theme_country_language, R.string.theme_country_language,25),
            Category("Transport", R.drawable.theme_transport, R.string.theme_transport,26),
            Category("Object", R.drawable.theme_object, R.string.theme_object,27),
            Category("Place", R.drawable.theme_place, R.string.theme_place,28),
            Category("Sciences", R.drawable.theme_science, R.string.theme_science,29)
        )

        fun getDefaultCategory(): Category {
            return defaultCategories[0]
        }

        fun getCategoryById(id: Int): Category {
            var selectCategory: Category = defaultCategories[0]
            var keepLooking = true
            var index = 0

            while(keepLooking) {
                if(defaultCategories[index].id == id) {
                    keepLooking = false
                    selectCategory = defaultCategories[index]
                }
                if(index >= Language.languagesList.size -1) keepLooking = false
                else index++
            }

            return selectCategory
        }
    }
}