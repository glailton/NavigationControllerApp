package br.com.grsoft.navigationcomponentapp.ui.registration.credential

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.grsoft.navigationcomponentapp.R
import br.com.grsoft.navigationcomponentapp.databinding.FragmentCredentialsBinding
import br.com.grsoft.navigationcomponentapp.extensions.dismissError
import br.com.grsoft.navigationcomponentapp.ui.login.LoginViewModel
import br.com.grsoft.navigationcomponentapp.ui.registration.RegistrationViewModel
import com.google.android.material.textfield.TextInputLayout

class CredentialsFragment : Fragment() {

    private var _binding: FragmentCredentialsBinding? = null
    private val binding get() = _binding!!
    val args: CredentialsFragmentArgs by navArgs()
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCredentialsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.textChooseCredentialsName.text = getString(R.string.choose_credentials_text_name, args.name)

        val invalidFields = initValidationFields()
        listenToRegistrationStateEvent(invalidFields)
        registerViewListeners()
        registerDeviceBackStack()
    }

    private fun initValidationFields() = mapOf(
        RegistrationViewModel.INPUT_USERNAME.first to binding.inputLayoutChooseCredentialsUsername,
        RegistrationViewModel.INPUT_PASSWORD.first to binding.inputLayoutChooseCredentialsPassword
    )

    private fun listenToRegistrationStateEvent(validationFields: Map<String, TextInputLayout>) {
        registrationViewModel.registrationStateEvent.observe(viewLifecycleOwner, Observer { registrationState ->
            when (registrationState) {
                is RegistrationViewModel.RegistrationState.RegistrationCompleted -> {
                    val token = registrationViewModel.authToken
                    val username = binding.inputChooseCredentialsUsername.text.toString()

                    loginViewModel.authenticateToken(token, username)
                    navController.popBackStack(R.id.profileFragment, false)
                }
                is RegistrationViewModel.RegistrationState.InvalidCredentials -> {
                    registrationState.fields.forEach { fieldError ->
                        validationFields[fieldError.first]?.error = getString(fieldError.second)
                    }
                }
            }
        })
    }

    private fun registerViewListeners() {
        binding.buttonChooseCredentialsNext.setOnClickListener {
            val username = binding.inputChooseCredentialsUsername.text.toString()
            val password = binding.inputChooseCredentialsPassword.text.toString()

            registrationViewModel.createCredentials(username, password)
        }

        binding.inputChooseCredentialsUsername.addTextChangedListener {
            binding.inputLayoutChooseCredentialsUsername.dismissError()
        }

        binding.inputChooseCredentialsPassword.addTextChangedListener {
            binding.inputLayoutChooseCredentialsPassword.dismissError()
        }
    }

    private fun registerDeviceBackStack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelRegistration()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        cancelRegistration()
        return super.onOptionsItemSelected(item)
    }

    private fun cancelRegistration() {
        registrationViewModel.userCancelledRegistration()
        navController.popBackStack(R.id.loginFragment, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}