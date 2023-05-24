package com.example.lexiapp.ui.games.correctword

import android.animation.ObjectAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.lexiapp.R
import com.example.lexiapp.data.word_asociation_api.WordAssociationClient
import com.example.lexiapp.data.word_asociation_api.WordAssociationService
import com.example.lexiapp.databinding.ActivityCorrectWordBinding
import com.example.lexiapp.domain.LetterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CorrectWordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCorrectWordBinding
    private lateinit var vM: CorrectWordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCorrectWordBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setVM()
        progressBarOn()
        setListeners()
        waitForValues()
    }

    private fun DesactivateButton() {
        binding.wordOne.isEnabled = false
        binding.wordTwo.isEnabled = false
        binding.wordThree.isEnabled = false
        binding.wordFour.isEnabled = false
    }

    private fun activateButton() {
        binding.wordOne.isEnabled = true
        binding.wordTwo.isEnabled = true
        binding.wordThree.isEnabled = true
        binding.wordFour.isEnabled = true
    }

    private fun resetGame() {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
        constraintLayout.setBackgroundColor(Color.WHITE)
        binding.btnOtherWord.visibility = View.GONE
        activateButton()
        vM.generateWord()
        progressBarOn()
        setListeners()
        waitForValues()
    }

    private fun setListeners() {
        binding.wordOne.setOnClickListener { checkAnswer(binding.wordOne) }
        binding.wordTwo.setOnClickListener { checkAnswer(binding.wordTwo) }
        binding.wordThree.setOnClickListener { checkAnswer(binding.wordThree) }
        binding.wordFour.setOnClickListener { checkAnswer(binding.wordFour) }
        binding.btnOtherWord.setOnClickListener {
            resetGame()
        }
    }

    private fun checkAnswer(selectedButton: Button) {
        val correctButtonText = binding.txtVariableWord.text.toString()
        if (selectedButton.text == correctButtonText) {
            animateCorrectButton(selectedButton)
            binding.btnOtherWord.visibility = View.VISIBLE
        } else {
            animateIncorrectButton(selectedButton)
        }
    }

    private fun animateCorrectButton(button: Button) {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
        DesactivateButton()
        makeText(binding.txtSelectWord.context, "Felicidades, Elegiste la palabra correcta", LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            constraintLayout.setBackgroundColor(Color.GREEN)
            button.setBackgroundColor(Color.WHITE)
        }, 3000)
    }

    private fun animateIncorrectButton(button: Button) {
        val translationXAnimation = ObjectAnimator.ofFloat(
            button, "translationX",
            0f, -20f, 20f, -20f, 20f, 0f
        )
        translationXAnimation.duration = 500
        translationXAnimation.start()
    }

    private fun waitForValues() {
        lifecycleScope.launch {
            delay(1500)
            withContext(Dispatchers.Main) {
                setValues()
            }
            progressBarOff()
        }
    }

    private fun progressBarOn() {
        lifecycleScope.launch {
            binding.progressBar3.visibility = View.VISIBLE
            binding.wordOne.visibility = View.GONE
            binding.wordTwo.visibility = View.GONE
            binding.wordThree.visibility = View.GONE
            binding.wordFour.visibility = View.GONE
            binding.txtWordToPlay.visibility = View.GONE
        }
    }

    private fun progressBarOff() {
        binding.progressBar3.visibility = View.GONE
        binding.wordOne.visibility = View.VISIBLE
        binding.wordTwo.visibility = View.VISIBLE
        binding.wordThree.visibility = View.VISIBLE
        binding.wordFour.visibility = View.VISIBLE
        binding.txtWordToPlay.visibility = View.VISIBLE
    }

    fun shuffleString(input: String, numCharsToShuffle: Int): String {
        val charArray = input.toCharArray()
        val indicesToShuffle = (0 until charArray.size).shuffled().take(numCharsToShuffle)
        val shuffledCharArray = charArray.mapIndexed { index, char ->
            if (index in indicesToShuffle) {
                charArray[(index + 1) % charArray.size]
            } else {
                char
            }
        }.toCharArray()
        return String(shuffledCharArray)
    }


    private fun setValues() {
        val word = vM.basicWord.value
        val shuffledWord1 = shuffleString(word, 2)
        val shuffledWord2 = shuffleString(word, 1)
        val shuffledWord3 = shuffleString(word, 2)

        val words = arrayOf(word, shuffledWord1, shuffledWord2, shuffledWord3)
        val shuffledArray = words.toList().shuffled().toTypedArray()

        binding.txtVariableWord.text = word
        binding.wordOne.text = shuffledArray[0]
        binding.wordTwo.text = shuffledArray[1]
        binding.wordThree.text = shuffledArray[2]
        binding.wordFour.text = shuffledArray[3]

    }

    private fun setVM() {
        val service = Retrofit.Builder()
            .baseUrl("https://random-word-api.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WordAssociationService::class.java)
        val client = WordAssociationClient(service)
        val repo = LetterRepository(client)
        val factory = CorrectWordViewModel.Factory(repo) // Factory
        vM = ViewModelProvider(this, factory)[CorrectWordViewModel::class.java]

    }



}
