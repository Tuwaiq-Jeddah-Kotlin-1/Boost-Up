package com.mahila.motivationalQuotesApp.views.homeScreen

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentSettingBinding
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import com.mahila.motivationalQuotesApp.views.SHARED_KEY
import com.mahila.motivationalQuotesApp.views.sharePreferencesValue
import com.mahila.motivationalQuotesApp.views.sharedDarkModeFlag


class SettingFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    //locale
    private lateinit var firstLocaleCode: String
    private lateinit var secondLocaleCode: String
    private lateinit var currentSystemLocaleCode: String

    /* lateinit var sharedDarkModeFlag : SharedPreferences
     private val SHARED_KEY = "DARK_MODE"*/
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
        currentSystemLocaleCode =
            ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0).language
        setUpModeButton(view)

        binding.signOutLL.setOnClickListener {
            confirmSignOut()
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
                            getString(R.string.your_name_field),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    binding.userNamEditText.text.toString() == binding.userNamTextView.text.toString() -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.not_changed),
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
                            getString(R.string.changes_saved),
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }
        }


        binding.modeLL.setOnClickListener {

            sharePreferencesValue = sharedDarkModeFlag.getString(SHARED_KEY, "Auto")

            when (sharePreferencesValue) {
                "Auto" -> {
                    sharedDarkModeFlag.edit().clear().apply()
                    sharedDarkModeFlag.edit().putString(SHARED_KEY, "DARK").apply()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    setUpModeButton(view)
                }
                "DARK" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedDarkModeFlag.edit().clear().apply()
                    sharedDarkModeFlag.edit().putString(SHARED_KEY, "LIGHT")
                        .apply()
                    setUpModeButton(view)

                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    sharedDarkModeFlag.edit().clear().apply()
                    sharedDarkModeFlag.edit().putString(SHARED_KEY, "Auto")
                        .apply()
                    setUpModeButton(view)

                }
            }
        }
    }

    // Show AlertDialog for Confirming signOut the task
    private fun confirmSignOut() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            userViewModel.signOut()
            Toast.makeText(
                requireContext(),
                "Successfully Sign Out.",
                Toast.LENGTH_SHORT
            ).show()
            /*requireActivity().run {
                startActivity(Intent(this, AuthenticationActivity::class.java))
                //finish()
            }*/
            findNavController().navigate(R.id.action_settingFragment_to_signupFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Sign Out of App?")
        builder.setMessage("Are you sure that you would like to sign out?")
        builder.create().show()
    }

    private fun setUpModeButton(view: View) {
        when (sharePreferencesValue) {
            "Auto" -> {
                binding.modeTextView.text = getString(R.string.light_mode)
                binding.modeIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_light_moon
                    )
                )
            }
            "DARK" -> {
                binding.modeIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_moon
                    )
                )
                binding.modeTextView.text = getString(R.string.auto)
            }
            else -> {
                binding.modeIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_moon
                    )
                )
                binding.modeTextView.text = getString(R.string.dark_mode)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
