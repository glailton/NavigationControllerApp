package br.com.grsoft.navigationcomponentapp.ui.registration.credential

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.grsoft.navigationcomponentapp.R
import br.com.grsoft.navigationcomponentapp.databinding.FragmentCredentialsBinding

class CredentialsFragment : Fragment() {

    private var _binding: FragmentCredentialsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCredentialsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}