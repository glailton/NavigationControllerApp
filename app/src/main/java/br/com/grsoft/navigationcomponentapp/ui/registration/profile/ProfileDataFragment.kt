package br.com.grsoft.navigationcomponentapp.ui.registration.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import br.com.grsoft.navigationcomponentapp.R
import br.com.grsoft.navigationcomponentapp.databinding.FragmentProfileDataBinding
import br.com.grsoft.navigationcomponentapp.extensions.dismissError
import br.com.grsoft.navigationcomponentapp.extensions.navigateWithAnimations
import br.com.grsoft.navigationcomponentapp.ui.registration.RegistrationViewModel
import com.google.android.material.textfield.TextInputLayout

class ProfileDataFragment : Fragment() {

    private val registrationViewModel: RegistrationViewModel by activityViewModels()
    private var _binding: FragmentProfileDataBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val validationFields = initValidationFields()
        listenToRegistrationStateEvent(validationFields)
        registerViewListeners()
        registerDeviceBackStackCallback()
    }

    private fun registerViewListeners() {
        binding.buttonProfileDataNext.setOnClickListener {
            val name = binding.inputProfileDataName.text.toString()
            val profile = binding.inputProfileDataBio.text.toString()

            registrationViewModel.collectProfileData(name, profile)
        }

        binding.inputProfileDataName.addTextChangedListener {
            binding.inputLayoutProfileDataName.dismissError()
        }

        binding.inputProfileDataBio.addTextChangedListener {
            binding.inputLayoutProfileDataBio.dismissError()
        }
    }

    private fun registerDeviceBackStackCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelRegistration()
        }
    }

    private fun cancelRegistration() {
        registrationViewModel.userCancelledRegistration()
        navController.popBackStack(R.id.loginFragment, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initValidationFields() = mapOf(
        RegistrationViewModel.INPUT_NAME.first to binding.inputLayoutProfileDataName,
        RegistrationViewModel.INPUT_BIO.first to binding.inputLayoutProfileDataBio
    )

    private fun listenToRegistrationStateEvent(validationFields: Map<String, TextInputLayout>) {
        registrationViewModel.registrationStateEvent.observe(viewLifecycleOwner, Observer { registrationState ->
            when (registrationState) {
                is RegistrationViewModel.RegistrationState.CollectCredentials -> {
                    val name = binding.inputProfileDataName.text.toString()
                    val directions = ProfileDataFragmentDirections.actionProfileDataFragmentToCredentialFragment(name)

                    navController.navigateWithAnimations(directions)
                }
                is RegistrationViewModel.RegistrationState.InvalidProfileData -> {
                    registrationState.fields.forEach { fieldError ->
                        validationFields[fieldError.first]?.error = getString(fieldError.second)
                    }
                }
            }
        })
    }

}