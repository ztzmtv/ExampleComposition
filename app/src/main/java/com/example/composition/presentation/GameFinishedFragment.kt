package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding

class GameFinishedFragment : Fragment() {
    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")
    private val args by navArgs<GameFinishedFragmentArgs>()

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

        val scoreAnswers = args.gameResult.scoreAnswers
        val countOfQuestions = args.gameResult.countOfQuestions
        val scorePercentage = (100 * scoreAnswers / countOfQuestions)
        val requiredAnswers = args.gameResult.gameSettings.requiredAnswers
        val requiredPercentage = args.gameResult.gameSettings.requiredPercentage

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
        return when (args.gameResult.winner) {
            true -> R.drawable.ic_smile
            false -> R.drawable.ic_sad
        }

    }

    private fun retryGame() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}