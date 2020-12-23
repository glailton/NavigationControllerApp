package br.com.grsoft.navigationcomponentapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.grsoft.navigationcomponentapp.R
import br.com.grsoft.navigationcomponentapp.databinding.FragmentProfileBinding
import br.com.grsoft.navigationcomponentapp.ui.login.LoginViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.authenticationStateEvent.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState) {
                is LoginViewModel.AuthenticationState.Autheticated -> {
                    binding.textViewProgileUsername.text = getString(R.string.profile_text_username, loginViewModel.username)
                }
                is LoginViewModel.AuthenticationState.Unautheticated -> {
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}