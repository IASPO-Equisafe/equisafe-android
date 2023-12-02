package com.iaspo.equisafe.ui.home.games.chronicles

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iaspo.equisafe.MainActivity
import com.iaspo.equisafe.R
import com.iaspo.equisafe.databinding.FragmentResultGameChroniclesBinding

class ResultGameChroniclesFragment : Fragment() {

    private var _binding: FragmentResultGameChroniclesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultGameChroniclesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val correctAnswer = it.getInt(EXTRA_RESULT)
            binding.tvCorrect.text = correctAnswer.toString()
            binding.tvIncorrect.text = (5 - correctAnswer).toString()

            val correctAnswerPercent = "${correctAnswer * 20} %"
            binding.tvResultPercent.text = getString(R.string.correct_answer, correctAnswerPercent)
        }
        setupAction()
    }

    private fun setupAction() {
        binding.btnHomeChronical.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    companion object {
       private const val EXTRA_RESULT = "extra result"

       fun newInstance(correctAnswer: Int): ResultGameChroniclesFragment {
           val fragment = ResultGameChroniclesFragment()
           val args = Bundle()
           args.putInt(EXTRA_RESULT, correctAnswer)
           fragment.arguments = args
           return fragment
       }
   }
}