package com.example.lexiapp.ui.games.letsread

import androidx.lifecycle.*
import com.example.lexiapp.data.repository.texttoread.TextToReadMocks
import com.example.lexiapp.domain.model.TextToRead
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextViewModel @Inject constructor() : ViewModel() {
    private val _listText = MutableLiveData<List<TextToRead>>()
    val listText: LiveData<List<TextToRead>> = _listText
    private val _textSelected = MutableLiveData<TextToRead>()
    val textSelected: LiveData<TextToRead> = _textSelected

    init {
        _listText.value = emptyList()
        _listText.value= getText()
        /*viewModelScope.launch {
            _listText.value = getText()
        }*/
    }

    private fun getText() = TextToReadMocks.getAllTextToReadMocks()

}