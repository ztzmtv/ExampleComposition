package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult

class GameFragment : Fragment() {
    private val args by navArgs<GameFragmentArgs>()
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")
    private val viewModelFactory by lazy {
        GameViewModelFactory(args.level, requireActivity().application)
    }
    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }
    private lateinit var gameResult: GameResult

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.updateQuestion()
        timerObserve()
        questionObserve()
        setOptionClickListeners()
        gameResultObserve()
        isFinishedObserve()
    }


    private fun timerObserve() {
        viewModel.timerLiveData.observe(viewLifecycleOwner, {
            binding.tvTimer.text = it.toString()
        })
    }

    private fun isFinishedObserve() {
        viewModel.isFinishedLiveData.observe(viewLifecycleOwner, {
            findNavController().navigate(
                GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult)
            )
        })
    }

    private fun setOptionClickListeners() {
        with(binding) {
            with(GameViewModel) {
                setOptionClickListeners(tvOption1, ANSWER_NUMBER_1)
                setOptionClickListeners(tvOption2, ANSWER_NUMBER_2)
                setOptionClickListeners(tvOption3, ANSWER_NUMBER_3)
                setOptionClickListeners(tvOption4, ANSWER_NUMBER_4)
                setOptionClickListeners(tvOption5, ANSWER_NUMBER_5)
                setOptionClickListeners(tvOption6, ANSWER_NUMBER_6)
            }
        }
    }

    private fun questionObserve() {
        viewModel.questionLiveData.observe(viewLifecycleOwner, {
            with(binding) {
                tvSum.text = it.sum.toString()
                tvLeftNumber.text = it.visibleNumber.toString()
                tvOption1.text = it.options[0].toString()
                tvOption2.text = it.options[1].toString()
                tvOption3.text = it.options[2].toString()
                tvOption4.text = it.options[3].toString()
                tvOption5.text = it.options[4].toString()
                tvOption6.text = it.options[5].toString()
            }
        })
    }

    private fun gameResultObserve() {
        viewModel.gameResultLiveData.observe(viewLifecycleOwner, {
            gameResult = it
            val requiredAnswers = gameResult.gameSettings.minCountOfRightAnswers
            binding.tvAnswersProgress.text = getString(
                R.string.progress_answers,
                it.countOfRightAnswers.toString(),
                requiredAnswers.toString()
            )
            with(binding.progressBar) {
                max = gameResult.gameSettings.maxSumValue
                progress = gameResult.countOfRightAnswers
                secondaryProgress = gameResult.countOfQuestions - 1
            }
        })
    }

    private fun setOptionClickListeners(textView: TextView, answer: Int) {
        textView.setOnClickListener {
            viewModel.answerQuestion(answer)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}