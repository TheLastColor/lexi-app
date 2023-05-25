package com.example.lexiapp.ui.games.letsread

import androidx.lifecycle.*
import com.example.lexiapp.data.repository.texttoread.TextToReadMocks
import com.example.lexiapp.domain.model.TextToRead
import com.example.lexiapp.domain.useCases.LetsReadUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextViewModel @Inject constructor(
    private val letsReadUseCases: LetsReadUseCases
) : ViewModel() {
    private var _listText = MutableStateFlow(emptyList<TextToRead>())
    val listText: LiveData<List<TextToRead>> = _listText.asLiveData()
    private var _textSelected = MutableStateFlow<TextToRead?>(null)
    val textSelected: LiveData<TextToRead?> = _textSelected.asLiveData()

    init {
        _listText.value = emptyList()
        getText()
    }

    private fun getText() {
        viewModelScope.launch(Dispatchers.IO) {
            letsReadUseCases.getTextsToRead()
                .collect {
                    _listText.value = it
                }
        }

    }

}