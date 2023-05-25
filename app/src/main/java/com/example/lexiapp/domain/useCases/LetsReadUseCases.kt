package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.api.LetsReadRepository
import com.example.lexiapp.data.repository.texttoread.TextToReadMocks
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LetsReadUseCases @Inject constructor(
    private val repository: LetsReadRepository
) {
    fun getTextsToRead() = flow {
        repository.getTexts()
            .collect { emit(it) }
    }

}
