package com.example.composition.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class GameResult(
    var winner: Boolean,
    var scoreAnswers: Int,
    var countOfQuestions: Int,
    var gameSettings: GameSettings
) : Parcelable