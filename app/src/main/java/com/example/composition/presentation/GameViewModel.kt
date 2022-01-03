package com.example.composition.presentation

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel : ViewModel() {

    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var timer: CountDownTimer? = null
    private var _gameSettings: GameSettings? = null
    private val gameSettings: GameSettings
        get() = _gameSettings ?: throw RuntimeException("_gameSettings == null")
    private var _gameResult: GameResult? = null
    private val gameResult: GameResult
        get() = _gameResult ?: throw RuntimeException("_gameResult == null")

    private var scoreAnswers = DEFAULT_SCORE_ANSWERS
    private var countOfQuestions = DEFAULT_COUNT_OF_QUESTIONS
    private var winner = DEFAULT_WINNER

    private val _gameResultLiveData = MutableLiveData<GameResult>()
    val gameResultLiveData: LiveData<GameResult> = _gameResultLiveData
    private val _isFinishedLiveData = MutableLiveData<Unit>()
    val isFinishedLiveData: LiveData<Unit> = _isFinishedLiveData
    private val _timerLiveData = MutableLiveData<Int>()
    val timerLiveData: LiveData<Int> = _timerLiveData
    private val _questionLiveData = MutableLiveData<Question>()
    val questionLiveData: LiveData<Question> = _questionLiveData


    fun setSettingsAndStart(level: Level) {
        _gameSettings = getGameSettings(level)
        updateResult()
        startTimer()
    }

    private fun startTimer() {
        val timeInMillis = (gameSettings.gameTimeInSeconds * 1000).toLong()
        timer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timerLiveData.value = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                _isFinishedLiveData.value = Unit
            }
        }.start()
    }

    fun answerQuestion(numberOfAnswer: Int) {
        incrementScore(numberOfAnswer)
        isWinner()
        updateResult()
        updateQuestion()
    }

    private fun incrementScore(numberOfAnswer: Int) {
        _questionLiveData.value?.let { question ->
            val sum = question.sum
            val num = question.visibleNumber
            val answer = question.options[numberOfAnswer - 1]
            if (answer == sum - num) scoreAnswers++
        }
    }


    fun updateQuestion() {
        _questionLiveData.value = generateQuestion(gameSettings.maxSumValue)
        countOfQuestions++
    }

    private fun updateResult() {
        _gameResult = GameResult(
            winner = this.winner,
            scoreAnswers = this.scoreAnswers,
            countOfQuestions = this.countOfQuestions,
            gameSettings = this.gameSettings
        )
        _gameResultLiveData.value = gameResult
    }

    private fun isWinner() {
        if ((scoreAnswers >= gameSettings.requiredAnswers &&
                    (100 * scoreAnswers / countOfQuestions) >= gameSettings.requiredPercentage)
        ) {
            winner = true
            updateResult()
            _isFinishedLiveData.value = Unit
        }
    }

    private fun generateQuestion(maxSumValue: Int): Question {
        return generateQuestionUseCase(maxSumValue)
    }

    private fun getGameSettings(level: Level): GameSettings {
        return getGameSettingsUseCase(level)
    }

    companion object {
        const val ANSWER_NUMBER_1 = 1
        const val ANSWER_NUMBER_2 = 2
        const val ANSWER_NUMBER_3 = 3
        const val ANSWER_NUMBER_4 = 4
        const val ANSWER_NUMBER_5 = 5
        const val ANSWER_NUMBER_6 = 6

        private const val DEFAULT_SCORE_ANSWERS = 0
        private const val DEFAULT_COUNT_OF_QUESTIONS = 1
        private const val DEFAULT_WINNER = false
        private const val TAG = "VIEW_MODEL"
    }

}