package com.iaspo.equisafe.ui.learning

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.iaspo.equisafe.core.adapter.LearningAdapter
import com.iaspo.equisafe.core.adapter.LoadingStateAdapter
import com.iaspo.equisafe.databinding.FragmentLearningBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LearningFragment : Fragment() {

    private var _binding: FragmentLearningBinding? = null
    private val binding get() = _binding!!

    private val learningViewModel: LearningViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        Log.d("Lifecycle","onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle","onResume")
    }

    private fun initView() {
        Log.d("LearningFragment", "initView: ")
        val adapter = LearningAdapter()
        binding.rvMaterial.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMaterial.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        learningViewModel.getVideoLearning("").observe(viewLifecycleOwner){
            adapter.submitData(lifecycle, it)
        }
    }
}