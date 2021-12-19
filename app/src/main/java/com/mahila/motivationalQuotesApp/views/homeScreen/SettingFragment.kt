package com.mahila.motivationalQuotesApp.views.homeScreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentSettingBinding
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import com.mahila.motivationalQuotesApp.views.auth.AuthenticationActivity


class SettingFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Data binding
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signOutLL.setOnClickListener {
            //dia-log
            userViewModel.signOut()
            //findNavController().navigate(R.id.action_settingFragment_to_AuthenticationActivity)
            requireActivity().run {
                startActivity(Intent(this, AuthenticationActivity::class.java))
                //finish()
            }

        }

        userViewModel.user.observe(viewLifecycleOwner, {
            binding.userNamTextView.text = it?.name

        })
        binding.editNameIcon.setOnClickListener {
            if (binding.editNameIcon.tag != "Modifiable") {
                binding.editNameIcon.tag = "Modifiable"
                binding.editNameIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_disk
                    )
                )
                binding.userNamTextView.visibility = View.GONE
                binding.userNamEditText.visibility = View.VISIBLE

            } else {
                when {
                    binding.userNamEditText.text.toString().isBlank() -> {
                        Toast.makeText(
                            requireContext(),
                            "Your name field cannot be Empty",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    binding.userNamEditText.text.toString() == binding.userNamTextView.text.toString() -> {
                        Toast.makeText(
                            requireContext(),
                            "Your name was not changed ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                        binding.userNamEditText.visibility = View.GONE
                        binding.userNamTextView.text = binding.userNamTextView.text.toString()
                        binding.userNamTextView.visibility = View.VISIBLE
                        binding.editNameIcon.tag = "Unmodifiable"
                        binding.editNameIcon.setImageDrawable(
                            AppCompatResources.getDrawable(
                                view.context,
                                R.drawable.ic_pencil
                            )
                        )
                        //update nam, vm called
                        userViewModel.resetUserName(binding.userNamEditText.text.toString())
                        Toast.makeText(
                            requireContext(),
                            "Changes saved successfully",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            //  findNavController().navigate(R.id.action_settingFragment_to_editProfileFragment)
        }

        binding.resetIcon.setOnClickListener {
            if (binding.resetIcon.tag != "Modifiable") {
                binding.resetIcon.tag = "Modifiable"
                binding.resetIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_disk
                    )
                )
                binding.resetPasswordTextView.visibility = View.GONE
                binding.resetPasswordEditText.visibility = View.VISIBLE
            } else {
                if (binding.resetPasswordEditText.text.toString().isBlank()) {
                    Toast.makeText(
                        requireContext(),
                        "Password field cannot be Empty",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    binding.resetIcon.tag = "Unmodifiable"
                    binding.resetIcon.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_pencil
                        )
                    )
                    binding.resetPasswordTextView.visibility = View.VISIBLE
                    binding.resetPasswordEditText.visibility = View.GONE
                    //update Password, vm called
                    //userViewModel.resetPassword(binding.resetPasswordEditText.text.toString())
                    Toast.makeText(
                        requireContext(),
                        "Changes saved successfully",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        binding.modeLL.setOnClickListener {
            if (binding.modeIcon.tag != "LIGHT") {
                binding.modeIcon.tag = "LIGHT"
                binding.modeIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_light_moon
                    )
//toDo
                )
                binding.modeTextView.text = "Light mode"
            } else {
                binding.modeIcon.tag = "DARK"
                binding.modeIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_moon
                    )
                )
                binding.modeTextView.text = "Dark mode"
//toDo
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
