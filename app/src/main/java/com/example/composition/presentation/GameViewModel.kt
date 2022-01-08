package com.example.composition.presentation

import android.app.Application
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

class GameViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {

    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var _gameSettings: GameSettings? = null
    private val gameSettings: GameSettings
        get() = _gameSettings ?: throw RuntimeException("_gameSettings == null")
    private var _gameResult: GameResult? = null
    private val gameResult: GameResult
        get() = _gameResult ?: throw RuntimeException("_gameResult == null")

    private var scoreAnswers = DEFAULT_SCORE_ANSWERS
    private var countOfQuestions = DEFAULT_COUNT_OF_QUESTIONS
    private var winner = DEFAULT_WINNER

    private var timer: CountDownTimer? = null
    private val _gameResultLiveData = MutableLiveData<GameResult>()
    val gameResultLiveData: LiveData<GameResult> = _gameResultLiveData
    private val _isFinishedLiveData = MutableLiveData<Unit>()
    val isFinishedLiveData: LiveData<Unit> = _isFinishedLiveData
    private val _timerLiveData = MutableLiveData<String>()
    val timerLiveData: LiveData<String> = _timerLiveData
    private val _questionLiveData = MutableLiveData<Question>()
    val questionLiveData: LiveData<Question> = _questionLiveData

    init {
        _gameSettings = getGameSettingsUseCase(level)
        updateQuestion()
        updateResult()
        startTimer()
    }


    fun answerQuestion(numberOfAnswer: Int) {
        incrementScore(numberOfAnswer)
        isWinner()
        updateResult()
        updateQuestion()
    }

    fun updateQuestion() {
        _questionLiveData.value = generateQuestionUseCase(gameSettings.maxSumValue)
        countOfQuestions++
    }

    private fun incrementScore(answer: Int) {
        _questionLiveData.value?.let { question ->
            val sum = question.sum
            val num = question.visibleNumber
            if (answer == sum - num) scoreAnswers++
        }
    }

    private fun startTimer() {
        val timeInMillis = (gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS)
        timer = object : CountDownTimer(timeInMillis, MILLIS_IN_SECONDS) {
            override fun onTick(millisUntilFinished: Long) {
                _timerLiveData.value = (millisUntilFinished / MILLIS_IN_SECONDS).toInt().toString()
            }

            override fun onFinish() {
                _isFinishedLiveData.value = Unit
            }
        }
        timer?.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    private fun updateResult() {
        _gameResult = GameResult(winner, scoreAnswers, countOfQuestions, gameSettings)
        _gameResultLiveData.value = gameResult
    }

    private fun isWinner() {
        if ((scoreAnswers >= gameSettings.minCountOfRightAnswers &&
                    (100 * scoreAnswers / countOfQuestions) >= gameSettings.minPercentOfRightAnswers)
        ) {
            winner = true
            updateResult()
            _isFinishedLiveData.value = Unit
        }
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val DEFAULT_SCORE_ANSWERS = 0
        private const val DEFAULT_COUNT_OF_QUESTIONS = 0
        private const val DEFAULT_WINNER = false
    }

}