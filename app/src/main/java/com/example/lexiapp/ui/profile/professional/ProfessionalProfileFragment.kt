package com.example.lexiapp.ui.profile.professional

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lexiapp.databinding.FragmentProfessionalProfileBinding
import com.example.lexiapp.domain.useCases.ProfileUseCases
import com.example.lexiapp.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfessionalProfileFragment : Fragment() {
    private var _binding: FragmentProfessionalProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var profileUseCases: ProfileUseCases

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfessionalProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfessionalData()
        setListeners()
    }

    private fun setProfessionalData(){
        val user = profileUseCases.getProfile()
        binding.tvProfessionalName.text = user.userName
        binding.tvProfessionalEmail.text = user.email
    }

    private fun setListeners() {
        closeProfile()
        signOff()
    }

    private fun closeProfile() {
        binding.icClose.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .remove(this)
                .commit()
        }
    }

    private fun signOff(){
        binding.btnLogOut.setOnClickListener {
            profileUseCases.closeSesion()
            requireActivity().finish()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}