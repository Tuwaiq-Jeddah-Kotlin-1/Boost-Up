package com.mahila.motivationalQuotesApp.views.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentSigninBinding
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import com.mahila.motivationalQuotesApp.views.MainActivity
import com.mahila.motivationalQuotesApp.views.sharePreferencesValueOfLang
import java.util.*


class SigninFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Data binding
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.forgotPasswordTextView.setOnClickListener {
            /* parentFragmentManager.beginTransaction().replace(R.id.auth_fragment, ForgotPasswordFragment())
                 .commit()*/
            findNavController().navigate(R.id.action_signinFragment_to_forgotPasswordFragment)

        }

        binding.signUpTextView.setOnClickListener {
            findNavController().navigate(R.id.action_signinFragment_to_signupFragment)
            // parentFragmentManager.beginTransaction().replace(R.id.auth_fragment, SignupFragment()).addToBackStack(null).commit()
            //   .commitNow()

        }
        binding.signinButton.setOnClickListener {
            if (binding.emailEditText.editText?.text.toString().isBlank()
                || binding.passwordEditText.editText?.text.toString().isBlank()
            ) {
                Toast.makeText(requireContext(), "Login fields can't be empty", Toast.LENGTH_LONG)
                    .show()
            } else {
                userViewModel.signIn(
                    binding.emailEditText.editText?.text.toString().trim(),
                    binding.passwordEditText.editText?.text.toString()
                )

                  findNavController().navigate(R.id.action_signinFragment_to_quotesFragment)
/*                requireActivity().run {
                    startActivity(Intent(this, MainActivity::class.java))
                    //  finish()
                }*/
            }
        }
    }

     override fun onStart() {
        super.onStart()

        if (userViewModel.checksignInState()) {

            findNavController().navigate(R.id.action_signinFragment_to_quotesFragment)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}