package com.mahila.motivationalQuotesApp.views.homeScreen

import android.content.res.Configuration
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
import com.mahila.motivationalQuotesApp.views.*
import java.util.*


class SettingFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentSystemLocaleCode: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //  binding
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpModeButton(view)
        setUpLangButton()
        currentSystemLocaleCode =
            ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0).language

        binding.changeLangLL.setOnClickListener {
            changeLang()
        }

        binding.signOutLL.setOnClickListener {
            confirmSignOut()
        }
        setUpUserData()

        binding.editNameIcon.setOnClickListener {
            editName(view)
        }


        binding.modeLL.setOnClickListener {
            changeMode(view)
        }
    }

    private fun setUpUserData() {
        userViewModel.user.observe(viewLifecycleOwner) {
            binding.userNamTextView.text = it?.name

        }
    }


    private fun editName(view: View?) {
        if (binding.editNameIcon.tag != "Modifiable") {
            binding.editNameIcon.tag = "Modifiable"
            if (view != null) {
                binding.editNameIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_disk
                    )
                )
            }
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
                    if (view != null) {
                        binding.editNameIcon.setImageDrawable(
                            AppCompatResources.getDrawable(
                                view.context,
                                R.drawable.ic_pencil
                            )
                        )
                    }
                    //update nam, vm called
                    userViewModel.resetUserName(binding.userNamEditText.text.toString())
                    binding.userNamTextView.text = binding.userNamEditText.text
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.changes_saved),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    // Show AlertDialog for Confirming signOut the task
    private fun confirmSignOut() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            userViewModel.signOut()
            Toast.makeText(
                requireContext(),
                getString(R.string.successfully_sign_out),
                Toast.LENGTH_SHORT
            ).show()
            sharedPre.edit().putBoolean(SHARED_STAY_SIGNED_IN, false).clear().apply()

            findNavController().navigate(R.id.action_settingFragment_to_signupFragment)
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setTitle(getString(R.string.out_of_app))
        builder.setMessage(getString(R.string.are_you_sure))
        builder.create().show()
    }

    private fun setUpLangButton() {
        when (sharePreferencesValueOfLang) {

            "ar" -> {
                binding.langEditText.text = getString(R.string.en)
            }
            "en" -> {

                binding.langEditText.text = getString(R.string.ar)
            }
            else -> binding.langEditText.text =
                if (reverseCurrentLang() == "en") getString(R.string.en) else getString(R.string.ar)
        }
    }

    private fun changeLang() {
        when (sharePreferencesValueOfLang) {
            "ar" -> {
                applyLocalized("en")
                sharedPre.edit().putString(SHARED_LANG_KEY, "en").apply()
                setUpLangButton()
            }
            "en" -> {
                applyLocalized("ar")
                sharedPre.edit().putString(SHARED_LANG_KEY, "ar")
                    .apply()
                setUpLangButton()
            }
            else -> {
                applyLocalized("Auto")
                sharedPre.edit().putString(SHARED_LANG_KEY, reverseCurrentLang())
                    .apply()
                setUpLangButton()

            }
        }
    }

    private fun applyLocalized(_langCode: String) {
        var langCode = _langCode
        if (langCode == "Auto") {
            langCode = reverseCurrentLang()
        }
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        activity?.recreate()
    }

    private fun reverseCurrentLang(): String {
        return if (ConfigurationCompat.getLocales(
                Resources.getSystem().configuration
            ).get(0).language == "en"
        ) {
            "ar"
        } else "en"
    }

    private fun setUpModeButton(view: View) {
        when (sharedPre.getString(SHARED_MODE_KEY, "Auto")) {
            "LIGHT" -> {
                binding.modeTextView.text = getString(R.string.dark_mode)
                binding.modeIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_moon
                    )
                )
            }
            "DARK" -> {
                binding.modeIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_sun
                    )
                )
                binding.modeTextView.text = getString(R.string.light_mode)
            }
            else -> {

                when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        binding.modeTextView.text = getString(R.string.dark_mode)
                        binding.modeIcon.setImageDrawable(
                            AppCompatResources.getDrawable(
                                view.context,
                                R.drawable.ic_moon
                            )
                        )
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        binding.modeIcon.setImageDrawable(
                            AppCompatResources.getDrawable(
                                view.context,
                                R.drawable.ic_sun
                            )
                        )
                        binding.modeTextView.text = getString(R.string.light_mode)
                    }
                }


            }
        }
    }

    private fun changeMode(view: View) {
        when (sharedPre.getString(SHARED_MODE_KEY, "Auto")) {
            "LIGHT" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPre.edit().putString(SHARED_MODE_KEY, "DARK").apply()
                setUpModeButton(view)
            }
            "DARK" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPre.edit().putString(SHARED_MODE_KEY, "LIGHT")
                    .apply()
                setUpModeButton(view)
            }
            else -> {
                if (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                    == Configuration.UI_MODE_NIGHT_YES
                ) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPre.edit().putString(SHARED_MODE_KEY, "LIGHT")
                        .apply()
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPre.edit().putString(SHARED_MODE_KEY, "DARK")
                        .apply()
                }
                setUpModeButton(view)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!sharedPre.getBoolean(SHARED_STAY_SIGNED_IN, false) ||
            !userViewModel.checkSignInState()
        ) {
            findNavController().navigate(R.id.action_settingFragment_to_signupFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
