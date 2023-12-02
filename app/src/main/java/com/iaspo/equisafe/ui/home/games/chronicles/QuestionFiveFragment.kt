package com.iaspo.equisafe.ui.home.games.chronicles

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.iaspo.equisafe.R
import com.iaspo.equisafe.core.domain.model.QuestionGame
import com.iaspo.equisafe.core.utils.gone
import com.iaspo.equisafe.core.utils.visible
import com.iaspo.equisafe.databinding.FragmentQuestionFiveBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class QuestionFiveFragment : Fragment() {

    private var _binding: FragmentQuestionFiveBinding? = null
    private val binding get() = _binding!!

    private var listQuestionGame: ArrayList<QuestionGame> = arrayListOf()
    private lateinit var answerQuestion: String

    private var correctAnswer: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionFiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var questionList: ArrayList<QuestionGame>
        arguments?.let {
            if (Build.VERSION.SDK_INT >= 33) {
                questionList = it.getParcelableArrayList(EXTRA_QUESTION, QuestionGame::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                questionList = it.getParcelableArrayList(EXTRA_QUESTION)!!
            }

            val correct = it.getInt(EXTRA_RESULT)

            Log.d("Question5", questionList.toString())
            Log.d("Correct Answer", correct.toString())

            listQuestionGame = questionList
            correctAnswer = correct

            val questionFive = questionList[4].question
            binding.questionFiveChronicles.text = questionFive
            answerQuestion = questionList[4].answer.lowercase(Locale.ROOT)
        }

        setupAction()
    }

    private fun setupAction() {
        binding.btnNextQuestionFive.setOnClickListener {
            val resultGameChronicles = ResultGameChroniclesFragment.newInstance(correctAnswer)
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(
                    R.id.frame_container,
                    resultGameChronicles,
                    ResultGameChroniclesFragment::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
        }

        binding.btnCheckQuestionFive.setOnClickListener {
            val answerUser = binding.inputAnswerFive.text.toString().trim().lowercase(Locale.ROOT)

            if (answerUser == answerQuestion) {
                binding.btnCheckQuestionFive.gone()
                val alphaBg = ObjectAnimator.ofFloat(binding.bgPerfect, View.ALPHA,  0.4f).apply {
                    duration = 1000
                }

                val alphaTv = ObjectAnimator.ofFloat(binding.tvPerfect, View.ALPHA,  1f).apply {
                    duration = 1000
                }


                val animatorSet = AnimatorSet()
                animatorSet.playTogether(alphaTv, alphaBg)
                animatorSet.start()


                lifecycleScope.launch(Dispatchers.IO){
                    delay(3000)
                    withContext(Dispatchers.Main) {
                        goNext()
                    }
                }
                binding.bgPerfect.visible()
                binding.tvPerfect.visible()
            } else {
                val alphaBg = ObjectAnimator.ofFloat(binding.bgPerfect, View.ALPHA,  0.4f).apply {
                    duration = 1000
                }


                binding.tvPerfect.text = getString(R.string.incorrect)
                binding.tvPerfect.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTextRed))

                val alphaTv = ObjectAnimator.ofFloat(binding.tvPerfect, View.ALPHA,  1f).apply {
                    duration = 1000
                }


                val animatorSet = AnimatorSet()
                animatorSet.playTogether(alphaTv, alphaBg)
                animatorSet.start()

                lifecycleScope.launch(Dispatchers.IO){
                    delay(3000)
                    withContext(Dispatchers.Main) {
                        goNext(isCorrect = false)
                    }
                }
                binding.bgPerfect.visible()
                binding.tvPerfect.visible()
            }
        }
    }

    private fun goNext(isCorrect: Boolean = true) {
        val alphaBg = ObjectAnimator.ofFloat(binding.bgPerfect, View.ALPHA,  0f).apply {
            duration = 1000
        }

        val alphaTv = ObjectAnimator.ofFloat(binding.tvPerfect, View.ALPHA,  0f).apply {
            duration = 1000
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(alphaTv, alphaBg)
        animatorSet.start()

        binding.btnCheckQuestionFive.gone()
        binding.bgPerfect.gone()
        binding.tvPerfect.gone()
        binding.layoutCorrect.visible()

        if (!isCorrect) {
            binding.tvCondition.text = getString(R.string.nice_try)
            binding.tvCondition.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTextRed))

            binding.ivCondiiton.setImageResource(R.drawable.icon_circle_wrong)
        } else {
            correctAnswer += 1
        }
    }


    companion object {
        private const val EXTRA_QUESTION = "extra_question"
        private const val EXTRA_RESULT = "extra_result"

        fun newInstance(questionListGame: ArrayList<QuestionGame>, correctAnswer: Int): QuestionFiveFragment {
            val fragment = QuestionFiveFragment()
            val args = Bundle()
            args.putSerializable(EXTRA_QUESTION, questionListGame)
            args.putInt(EXTRA_RESULT, correctAnswer)
            fragment.arguments = args
            return fragment
        }
    }

}