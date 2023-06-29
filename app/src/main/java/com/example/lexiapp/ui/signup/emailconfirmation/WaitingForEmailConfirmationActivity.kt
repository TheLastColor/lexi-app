package com.example.lexiapp.ui.signup.emailconfirmation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.lexiapp.databinding.ActivityWaitingEmailConfirmationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WaitingForEmailConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWaitingEmailConfirmationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaitingEmailConfirmationBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }

}