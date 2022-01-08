package com.example.composition.presentation

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.Question

interface OnOptionClickListener {
    fun onOptionClick(option: Int)
}

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
        divZero(gameResult.countOfRightAnswers, gameResult.countOfQuestions)
    )
}

private fun divZero(a: Int, b: Int): Int {
    return if (b == 0) {
        0
    } else (100 * a / b.toDouble()).toInt()
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

@BindingAdapter("questionSum")
fun bindingQuestionSum(textView: TextView, question: Question) {
    textView.text = question.sum.toString()
}

@BindingAdapter("questionVisibleNumber")
fun bindingQuestionVisibleNumber(textView: TextView, question: Question) {
    textView.text = question.visibleNumber.toString()
}

@BindingAdapter("answerProgressText")
fun bindingAnswerProgressText(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.getString(R.string.progress_answers),
        gameResult.countOfRightAnswers.toString(),
        gameResult.gameSettings.minCountOfRightAnswers.toString()
    )
}

@BindingAdapter("progressBar")
fun bindingQuestionVisibleNumber(progressBar: ProgressBar, gameResult: GameResult) {
    progressBar.max = gameResult.gameSettings.maxSumValue
    progressBar.progress = gameResult.countOfRightAnswers
    progressBar.secondaryProgress = gameResult.countOfQuestions
}

@BindingAdapter("onOptionClickListener")
fun bindingOnOptionClickListener(textView: TextView, clickListener: OnOptionClickListener) {
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}