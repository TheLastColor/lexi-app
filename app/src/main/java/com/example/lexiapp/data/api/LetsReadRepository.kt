package com.example.lexiapp.data.api

import com.example.lexiapp.data.repository.texttoread.TextToReadMocks
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LetsReadRepository @Inject constructor() {
    fun getTexts() = flow {
        emit(TextToReadMocks.getAllTextToReadMocks())
    }

}
