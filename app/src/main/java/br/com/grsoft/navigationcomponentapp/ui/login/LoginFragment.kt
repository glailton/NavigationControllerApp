package br.com.grsoft.navigationcomponentapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.grsoft.navigationcomponentapp.R
import br.com.grsoft.navigationcomponentapp.databinding.LoginFragmentBinding
import br.com.grsoft.navigationcomponentapp.extensions.dismissError
import br.com.grsoft.navigationcomponentapp.extensions.navigateWithAnimations
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.authenticationStateEvent.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState) {
                is LoginViewModel.AuthenticationState.InvalidAuthentication -> {
                    val validationFields: Map<String, TextInputLayout> = initValidationFields()
                    authenticationState.fields.forEach { fieldError ->
                        validationFields[fieldError.first]?.error = getString(fieldError.second)
                    }
                }
                is LoginViewModel.AuthenticationState.Authenticated -> {
                    findNavController().popBackStack()
                }
            }
        })

        binding.buttonLoginSignIn.setOnClickListener {
            val username = binding.inputLoginUsername.text.toString()
            val password = binding.inputLoginPassword.text.toString()

            viewModel.authenticate(username, password)
        }

        binding.buttonLoginSignUp.setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_loginFragment_to_registrationNavigation)
        }

        binding.inputLoginUsername.addTextChangedListener {
            binding.inputLayoutLoginUsername.dismissError()
        }

        binding.inputLoginPassword.addTextChangedListener {
            binding.inputLayoutLoginPassword.dismissError()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelAuthentication()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        cancelAuthentication()
        return true
    }

    private fun cancelAuthentication() {
        viewModel.refuseAuthentication()
        findNavController().popBackStack(R.id.startFragment, false)
    }

    private fun initValidationFields() = mapOf(
            LoginViewModel.INPUT_USERNAME.first to binding.inputLayoutLoginUsername,
            LoginViewModel.INPUT_PASSWORD.first to binding.inputLayoutLoginPassword
    )

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}