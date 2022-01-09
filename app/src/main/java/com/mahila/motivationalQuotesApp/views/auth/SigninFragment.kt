package com.mahila.motivationalQuotesApp.views.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentSigninBinding
import com.mahila.motivationalQuotesApp.utils.ValidationUtil
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

            if (binding.emailEditText.editText?.text.toString().isBlank()
            ) {
                binding.emailEditText.error = getString(R.string.input_fields_cannot_be_empty)

            } else if (!ValidationUtil.isValidEmail(
                    binding.emailEditText.editText?.text.toString().trim())
            ) {
                binding.emailEditText.error = getString(R.string.invalid_email)

            } else if (binding.passwordEditText.editText?.text.toString().isBlank()) {
                binding.passwordEditText.error = getString(R.string.input_fields_cannot_be_empty)
            } else {
                userViewModel.signIn(
                    binding.emailEditText.editText?.text.toString().trim(),
                    binding.passwordEditText.editText?.text.toString(), view
                )
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}