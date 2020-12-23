package br.com.grsoft.navigationcomponentapp.ui.start

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.grsoft.navigationcomponentapp.R
import br.com.grsoft.navigationcomponentapp.databinding.FragmentStartBinding
import br.com.grsoft.navigationcomponentapp.extensions.navigateWithAnimations

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_startFragment_to_profileFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}