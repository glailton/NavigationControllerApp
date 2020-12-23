package br.com.grsoft.navigationcomponentapp.ui.registration.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.grsoft.navigationcomponentapp.databinding.FragmentProfileDataBinding
import br.com.grsoft.navigationcomponentapp.ui.registration.RegistrationViewModel

class ProfileDataFragment : Fragment() {

    private val registrationViewModel: RegistrationViewModel by activityViewModels()
    private var _binding: FragmentProfileDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registrationViewModel.registrationStateEvent.observe(viewLifecycleOwner, Observer { registrationState ->
            when(registrationState) {
                is RegistrationViewModel.RegistrationState.CollectCredentials -> {
                    val name = binding.inputProfileDataName.text.toString()
                    val directions = ProfileDataFragmentDirections.actionProfileDataFragmentToCredentialFragment(name)

                    findNavController().navigate(directions)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}