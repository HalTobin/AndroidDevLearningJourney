package com.chapeaumoineau.miavortoj.feature.quiz.model

data class Results(
    var duration: Int = 0,
    var correctAnswers: Int = 0,
    var wrongAnswers: Int = 0
) {
    fun getPercent(): Int {
        return ((correctAnswers.toFloat() / duration.toFloat()) * 100).toInt()
    }
    fun getEmote(): String {
        val percent = getPercent()

        var emote = "😱"
        if (percent >= 10) emote = "😵"
        if (percent >= 20) emote = "😔"
        if (percent >= 30) emote = "😔"
        if (percent >= 40) emote = "😔"
        if (percent >= 50) emote = "😶"
        if (percent >= 60) emote = "😐"
        if (percent >= 70) emote = "🙄"
        if (percent >= 80) emote = "🙄"
        if (percent >= 90) emote = "🤓"
        if (percent == 100) emote = "🥳"

        return emote
    }
}
