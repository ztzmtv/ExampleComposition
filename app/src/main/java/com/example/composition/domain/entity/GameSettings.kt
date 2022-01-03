package com.example.composition.domain.entity

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class GameSettings(
    val maxSumValue: Int,
    val requiredAnswers: Int,
    val requiredPercentage: Int,
    val gameTimeInSeconds: Int
) : Parcelable
