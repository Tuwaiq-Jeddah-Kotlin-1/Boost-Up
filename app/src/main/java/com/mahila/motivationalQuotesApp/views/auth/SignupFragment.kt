package com.mahila.motivationalQuotesApp.views.auth


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
import com.mahila.motivationalQuotesApp.databinding.FragmentSignupBinding
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import com.mahila.motivationalQuotesApp.views.MainActivity


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
            /*parentFragmentManager.beginTransaction()
                .replace(R.id.auth_fragment, SigninFragment())
                .addToBackStack(null).commit()*/
        }

        binding.signupButton.setOnClickListener {
            if (binding.emailEditText.editText?.text.toString().isBlank() ||
                binding.passwordEditText.editText?.text.toString().isBlank()
                || binding.confirmPasswordEditText.editText?.text.toString().isBlank()
            ) {
                Toast.makeText(requireContext(), "Input Fields cannot be Empty", Toast.LENGTH_LONG)
                    .show()
            } else if (binding.passwordEditText.editText?.text.toString() !=
                binding.confirmPasswordEditText.editText?.text.toString()) {
                Toast.makeText(requireContext(), getString(R.string.not_match), Toast.LENGTH_LONG).show()
            } else {
                userViewModel.signUp(
                    binding.userNameEditText.editText?.text.toString(),
                    binding.emailEditText.editText?.text.toString().trim(),
                    binding.passwordEditText.editText?.text.toString()
                )
                  findNavController().navigate(R.id.action_signupFragment_to_signinFragment)
                /*requireActivity().run {
                    startActivity(Intent(this, MainActivity::class.java))*/
                   // finish()
               // }
            }


        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}