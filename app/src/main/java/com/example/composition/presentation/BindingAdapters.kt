package com.example.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult

@BindingAdapter("requiredAnswers")
fun bindingRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_score),
        count
    )
}

@BindingAdapter("countOfRightAnswers")
fun bindingCountOfRightAnswers(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.getString(R.string.right_answers),
        gameResult.countOfRightAnswers,
        gameResult.gameSettings.minCountOfRightAnswers
    )
}

@BindingAdapter("requiredPercentage")
fun bindingRequiredPercentage(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage),
        count
    )
}

@BindingAdapter("scorePercentage")
fun bindingScorePercentage(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.getString(R.string.score_percentage),
        100 * gameResult.countOfRightAnswers / gameResult.countOfQuestions
    )
}

//    emojiResult.setImageResource(emojiIfWinner())
@BindingAdapter("emojiResult")
fun bindingScorePercentage(imageView: ImageView, winner: Boolean) {
    val image = when (winner) {
        true -> R.drawable.ic_smile
        false -> R.drawable.ic_sad
    }
    imageView.setImageResource(image)
}
