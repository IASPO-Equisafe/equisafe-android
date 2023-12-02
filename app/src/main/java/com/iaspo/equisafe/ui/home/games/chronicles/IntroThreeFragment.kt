package com.iaspo.equisafe.ui.home.games.chronicles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.iaspo.equisafe.R
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.model.QuestionGame
import com.iaspo.equisafe.core.utils.gone
import com.iaspo.equisafe.core.utils.visible
import com.iaspo.equisafe.databinding.FragmentIntroThreeBinding
import com.iaspo.equisafe.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class IntroThreeFragment : Fragment() {

    private var _binding: FragmentIntroThreeBinding? = null
    private val binding get() =  _binding!!

    private val gamesChroniclesViewModel: HomeViewModel by viewModel()

    private val listQuestionGame: ArrayList<QuestionGame> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroThreeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getQuestion()

    }

    private fun getQuestion() {
        binding.btnNextIntroThree.setOnClickListener {
            gamesChroniclesViewModel.getChroniclesGame().observe(viewLifecycleOwner) {
                it?.let { response ->
                    when(response) {
                        is ApiResult.Loading -> {
                            binding.btnNextIntroThree.gone()
                            binding.progressGame.visible()
                        }

                        is ApiResult.Success -> {
                            response.data.forEach { questionsItem ->
                                listQuestionGame.add(
                                    QuestionGame(
                                        question = questionsItem.question,
                                        answer = questionsItem.answer
                                    )
                                )
                            }
                            lifecycleScope.launch(Dispatchers.IO){
                                delay(2000)
                                withContext(Dispatchers.Main) {
                                    binding.progressGame.gone()
                                    toQuestionGameChronicles()
                                }
                            }
                        }

                        is ApiResult.Error -> {
                            binding.progressGame.gone()
                            Toast.makeText(requireContext(), response.errorMessage, Toast.LENGTH_LONG).show()
                        }

                        is ApiResult.Empty -> {
                            binding.progressGame.gone()
                        }
                    }
                }
            }
        }
    }

    private fun toQuestionGameChronicles() {
        val questionOneFragment = QuestionOneFragment.newInstance(listQuestionGame)
        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(
                R.id.frame_container,
                questionOneFragment,
                QuestionOneFragment::class.java.simpleName
            )
            addToBackStack(null)
            commit()
        }
    }

}