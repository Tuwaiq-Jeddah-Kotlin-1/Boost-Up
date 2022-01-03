package com.mahila.motivationalQuotesApp.views.auth


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentSignupBinding
import com.mahila.motivationalQuotesApp.util.ValidationUtil
import com.mahila.motivationalQuotesApp.util.ValidationUtil.isValidPassword
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel


class SignupFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInTextView.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_signinFragment)

        }
        binding.passwordEditText1.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.passwordEditText.helperText = passwordValidationFeedBackMsg()
            }
        }
        binding.signupButton.setOnClickListener {
            if (binding.userNameEditText.editText?.text.toString().isBlank()
            ) {
                binding.userNameEditText.error = getString(R.string.input_fields_cannot_be_empty)

            } else if (binding.emailEditText.editText?.text.toString().isBlank()
            ) {
                    binding.emailEditText.error = getString(R.string.input_fields_cannot_be_empty)

            } else if (!ValidationUtil.isValidEmail(
                    binding.emailEditText.editText?.text.toString().trim()
                )
            ) {
                binding.emailEditText.error = getString(R.string.invalid_email)

            } else if (binding.passwordEditText.editText?.text.toString().isBlank()) {
                binding.passwordEditText.error = getString(R.string.input_fields_cannot_be_empty)
            } else if (!isValidPassword(binding.passwordEditText.editText?.text.toString())) {
                binding.passwordEditText.error = getString(R.string.invalid_password)

            } else if (binding.confirmPasswordEditText.editText?.text.toString().isBlank()
            ) {
                binding.confirmPasswordEditText.error = getString(R.string.input_fields_cannot_be_empty)

            } else if (binding.passwordEditText.editText?.text.toString() !=
                binding.confirmPasswordEditText.editText?.text.toString()
            ) {
                binding.confirmPasswordEditText.error = getString(R.string.not_match)
            } else {
                userViewModel.signUp(
                    binding.userNameEditText.editText?.text.toString(),
                    binding.emailEditText.editText?.text.toString().trim(),
                    binding.passwordEditText.editText?.text.toString()
                )
                findNavController().navigate(R.id.action_signupFragment_to_signinFragment)

            }

        }

    }

    private fun passwordValidationFeedBackMsg(): String? {
        val password = binding.passwordEditText.editText?.text.toString()
        if (password.length < 8) {
            return getString(R.string.minimum_password)
        }
        if (!password.matches(".*[A-Z].*".toRegex())) {
            return getString(R.string.capital_letter)
        }
        if (!password.matches(".*[0-9].*".toRegex())) {
            return getString(R.string.number)
        }
        if (!password.matches(".*[-+!@#\$%^&*,?].*".toRegex())) {
            return getString(R.string.special_character)
        }//
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}