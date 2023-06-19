package com.example.lexiapp.domain.service

import com.example.lexiapp.domain.model.WhereIsTheLetterResult
import kotlinx.coroutines.flow.Flow

interface ResultGamesService {

    suspend fun getWhereIsTheLetterResults(email: String): Flow<List<WhereIsTheLetterResult>>

}
