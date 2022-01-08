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
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        gameResultObserve()
        isFinishedObserve()
    }


    private fun isFinishedObserve() {
        viewModel.isFinishedLiveData.observe(viewLifecycleOwner, {
            findNavController().navigate(
                GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult)
            )
        })
    }

    private fun gameResultObserve() {
        viewModel.gameResultLiveData.observe(viewLifecycleOwner, {
            gameResult = it
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