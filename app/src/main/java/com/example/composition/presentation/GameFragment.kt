package com.example.composition.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.composition.R
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.Level

class GameFragment : Fragment() {
    private lateinit var level: Level
    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")
    private lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        viewModel.setSettingsAndStart(level)
        viewModel.updateQuestion()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
                .commit()
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
            val scoreAnswers = it.scoreAnswers
            Log.d(NAME, scoreAnswers.toString())
            val requiredAnswers = gameResult.gameSettings.requiredAnswers
            binding.tvAnswersProgress.text = getString(
                R.string.progress_answers,
                scoreAnswers.toString(),
                requiredAnswers.toString()
            )
        })
    }

    private fun setOptionClickListeners(textView: TextView, answer: Int) {
        textView.setOnClickListener {
            viewModel.answerQuestion(answer)
        }

    }

    private fun parseArgs() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val NAME = "GameFragment"
        private const val KEY_LEVEL = "level"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}