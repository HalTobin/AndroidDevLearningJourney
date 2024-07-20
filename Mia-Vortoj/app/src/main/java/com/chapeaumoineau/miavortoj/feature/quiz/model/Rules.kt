package com.chapeaumoineau.miavortoj.feature.quiz.model

import android.content.Context
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset

data class Rules(
    //val dictionaryId: Int,
    var duration: Int,
    val categoryId: Int,
    val difficulty: Int,
    //val gameRulesId: Int,
    //val auto: Boolean
    ) {

    companion object {
        const val CATEGORY_ALL = 0

        const val RULES_ALL = 0
        const val SOURCE_TO_TARGET_TEXT = 1
        const val TARGET_TO_SOURCE_TEXT = 2
        const val TARGET_TO_TARGET_SOUND = 3
        const val TARGET_TO_SOURCE_SOUND = 4

        const val DIFFICULTY_EASY = 0
        const val DIFFICULTY_MEDIUM = 1
        const val DIFFICULTY_HARD = 2
        const val DIFFICULTY_ALL = 2

        fun getDefault(): Rules {
            return Rules(10, CATEGORY_ALL, DIFFICULTY_ALL,)
        }
    }
}

fun Rules.getAsJsonString(): String {

    return ""
}

fun Rules.save(context: Context) {
    val myPath = File(context.filesDir.toString())
    val gson = Gson()
    val mySerializedPlaylist = gson.toJson(this)
    if (!myPath.exists()) myPath.mkdirs()
    val myFile = File("$myPath/defaultRules.json")
    var myOutputStream: FileOutputStream? = null
    try {
        myOutputStream = FileOutputStream(myFile, false)
        myOutputStream.write(mySerializedPlaylist.toByteArray(Charset.defaultCharset()))
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            myOutputStream!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}