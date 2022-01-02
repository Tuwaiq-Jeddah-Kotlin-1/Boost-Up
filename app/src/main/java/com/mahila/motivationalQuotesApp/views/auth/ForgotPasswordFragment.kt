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
import com.mahila.motivationalQuotesApp.databinding.FragmentForgotPasswordBinding
import com.mahila.motivationalQuotesApp.util.ValidationUtil
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel


class ForgotPasswordFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // binding
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sendButton.setOnClickListener {
            if (binding.emailEditText.editText?.text.toString().isBlank()
            ) {
                Toast.makeText(requireContext(), getString(R.string.email_field), Toast.LENGTH_LONG)
                    .show()
            } else if (!ValidationUtil.isValidEmail(
                    binding.emailEditText.editText?.text.toString().trim()
                )
            ) {
                Toast.makeText(requireContext(), getString(R.string.invalid_email)
                    , Toast.LENGTH_LONG).show()
            } else {
                userViewModel.forgotPassword(
                    binding.emailEditText.editText?.text.toString().trim()
                )
                findNavController().navigate(R.id.action_forgotPasswordFragment_to_signinFragment)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}