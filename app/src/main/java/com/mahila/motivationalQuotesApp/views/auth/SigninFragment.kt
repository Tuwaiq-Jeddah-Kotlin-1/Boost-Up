package com.mahila.motivationalQuotesApp.views.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentSigninBinding
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import com.mahila.motivationalQuotesApp.views.SHARED_STAY_SIGNED_IN
import com.mahila.motivationalQuotesApp.views.sharedPre


class SigninFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // binding
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.forgotPasswordTextView.setOnClickListener {
            findNavController().navigate(R.id.action_signinFragment_to_forgotPasswordFragment)

        }
        binding.staySignedIn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPre.edit().putBoolean(SHARED_STAY_SIGNED_IN, true).apply()
            }
        }

        binding.signUpTextView.setOnClickListener {
            findNavController().navigate(R.id.action_signinFragment_to_signupFragment)

        }
        binding.signinButton.setOnClickListener {
            if (binding.passwordEditText.editText?.text.toString().isBlank()) {
                binding.passwordEditText.error="Input field cannot be empty"
            }
            if (binding.emailEditText.editText?.text.toString().isBlank()

            ) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.input_fields_cannot_be_empty), Toast.LENGTH_LONG
                )
                    .show()
            } else {
                userViewModel.signIn(
                    binding.emailEditText.editText?.text.toString().trim(),
                    binding.passwordEditText.editText?.text.toString(), view
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (sharedPre.getBoolean(SHARED_STAY_SIGNED_IN, false) &&
            userViewModel.checkSignInState()
        ) {
            findNavController().navigate(R.id.action_signinFragment_to_quotesFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}