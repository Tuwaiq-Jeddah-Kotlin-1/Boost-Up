package com.mahila.motivationalQuotesApp.view

import android.content.Intent
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
import com.mahila.motivationalQuotesApp.viewModel.UserViewModel


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


        binding.signUpTextView.setOnClickListener {
         //   findNavController().navigate(R.id.action_signinFragment_to_signupFragment)
            parentFragmentManager.beginTransaction().replace(R.id.auth_fragment,SignupFragment())
                .commitNow()

        }
        binding.signinButton.setOnClickListener {
            if (binding.emailEditText.text.toString().isBlank()
                || binding.passwordEditText.text.toString().isBlank()
            ) {
                Toast.makeText(requireContext(), "Login fields can't be empty", Toast.LENGTH_LONG)
                    .show()
            } else {
                userViewModel.signIn(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
            //   findNavController().navigate(R.id.action_signinFragment_to_mainActivity)
                requireActivity().run{
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}