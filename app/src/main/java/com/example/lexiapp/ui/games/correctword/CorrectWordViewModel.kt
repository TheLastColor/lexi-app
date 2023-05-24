package com.example.lexiapp.ui.games.correctword

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.LetterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CorrectWordViewModel(
    private val letterRepository: LetterRepository
) : ViewModel() {

    private var _selectedPosition = MutableStateFlow<Int?>(null)
    val selectedPosition = _selectedPosition.asStateFlow()

    private var _basicWord = MutableStateFlow("TESTEANDO")
    val basicWord = _basicWord.asStateFlow()

    private var _correctPosition = MutableStateFlow(2)
    val correctPosition = _correctPosition.asStateFlow()

    private var _correctAnswerSubmitted = MutableStateFlow(false)
    val correctAnswerSubmitted = _correctAnswerSubmitted.asStateFlow()

    private var _incorrectAnswerSubmitted = MutableStateFlow(false)
    val incorrectAnswerSubmitted = _incorrectAnswerSubmitted.asStateFlow()

    init {
        generateWord()
    }

    fun onPositionSelected(position: Int) {
        _selectedPosition.value = position
    }

    fun onPositionDeselected() {
        _selectedPosition.value = null
    }

    fun onSubmitAnswer() {
        if (selectedPosition.value == correctPosition.value) {
            _correctAnswerSubmitted.value = true
        } else {
            _incorrectAnswerSubmitted.value = true
        }
    }

    fun generateWord() {
        viewModelScope.launch(Dispatchers.IO) {
            letterRepository.getWord()
                .collect {
                    Log.v("data_in_view_model", "response word: $it")
                    _basicWord.value = it
                    Log.v(
                        "asignate_data_to_variable",
                        "response _basicWord: ${_basicWord.value} and basicWord: ${basicWord.value}"
                    )
                }
        }
    }

    class Factory(private val repo: LetterRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CorrectWordViewModel(repo) as T
        }
    }
}