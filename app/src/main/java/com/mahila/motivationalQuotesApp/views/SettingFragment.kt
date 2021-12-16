package com.mahila.motivationalQuotesApp.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentSettingBinding
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel


class SettingFragment : Fragment() {
    private val args by navArgs<SettingFragmentArgs>()

    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Data binding
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
//        binding.args1 = args
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
        binding.resetPasswordLL.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_editProfileFragment)
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
