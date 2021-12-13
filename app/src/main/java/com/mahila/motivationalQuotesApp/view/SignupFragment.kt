package com.mahila.motivationalQuotesApp.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentSignupBinding
import com.mahila.motivationalQuotesApp.viewModel.UserViewModel


class SignupFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // Data binding
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInTextView.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_signinFragment)
           // findNavController().navigate(R.id.action_signupFragment_to_AuthenticationActivity)
           // parentFragmentManager.beginTransaction().replace(R.id.container,Sign inFragment()).addToBackStack(null).commit()
        }


        binding.signupButton.setOnClickListener {
            if (binding.emailEditText.text.toString().isBlank() ||
                binding.passwordEditText.text.toString().isBlank()
                || binding.confirmPasswordEditText.text.toString().isBlank()
            ) {
                Toast.makeText(requireContext(), "Input Fields cannot be Empty", Toast.LENGTH_LONG)
                    .show()
            } else if (binding.passwordEditText.text.toString() != binding.confirmPasswordEditText.text.toString()) {
                Toast.makeText(requireContext(), "Passwords don't match", Toast.LENGTH_LONG).show()
            } else {
                userViewModel.signUp(binding.userNameEditText.text.toString()
                    ,binding.emailEditText.text.toString()
                    ,binding.passwordEditText.text.toString())
                findNavController().navigate(R.id.action_signupFragment_to_mainActivity)
            }
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}