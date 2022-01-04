package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {
    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    private lateinit var gameResult: GameResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scoreAnswers = gameResult.scoreAnswers
        val countOfQuestions = gameResult.countOfQuestions
        val scorePercentage = (100 * scoreAnswers / countOfQuestions)
        val requiredAnswers = gameResult.gameSettings.requiredAnswers
        val requiredPercentage = gameResult.gameSettings.requiredPercentage

        with(binding) {
            tvRequiredAnswers.text =
                getString(R.string.required_score, requiredAnswers.toString())
            tvScoreAnswers.text =
                getString(R.string.score_answers, scoreAnswers.toString())
            tvRequiredPercentage.text =
                getString(R.string.required_percentage, requiredPercentage.toString())
            tvScorePercentage.text =
                getString(R.string.score_percentage, scorePercentage.toString())
            emojiResult.setImageResource(emojiIfWinner())
        }

        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun emojiIfWinner(): Int {
        return when (gameResult.winner) {
            true -> R.drawable.ic_smile
            false -> R.drawable.ic_sad
        }

    }

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            gameResult = it
        }
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_GAME_RESULT = "game_result"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}