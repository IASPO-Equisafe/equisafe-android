package com.iaspo.equisafe.ui.home.games.chronicles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iaspo.equisafe.R
import com.iaspo.equisafe.databinding.FragmentIntroTwoBinding

class IntroTwoFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentIntroTwoBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNextIntroTwo.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_next_intro_two) {
            val introThreeFragment = IntroThreeFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(
                    R.id.frame_container,
                    introThreeFragment,
                    IntroThreeFragment::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
        }

    }
}