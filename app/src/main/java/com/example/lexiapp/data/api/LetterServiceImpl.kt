package com.example.lexiapp.data.api

import android.content.SharedPreferences
import android.util.Log
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationService
import com.example.lexiapp.data.model.Game
import com.example.lexiapp.data.model.toCorrectWordDataResult
import com.example.lexiapp.data.model.toWhereIsGameResult
import com.example.lexiapp.data.network.FireStoreServiceImpl
import com.example.lexiapp.data.repository.BlackList
import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.model.gameResult.ResultGame
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.LetterService
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class LetterServiceImpl @Inject constructor(
    private val apiWordService: WordAssociationService,
    private val db: FireStoreServiceImpl,
    private val prefs: SharedPreferences
) : LetterService {

    private val userMail = prefs.getString("email", null)!!

    override suspend fun getWord(count: Int, length: Int, language: String) = flow {
        apiWordService.getWordToWhereIsTheLetterGame(count, length, language)
            .map { inputList -> inputList.filter { !BlackList.words.contains(it.uppercase()) } }
            .collect {
                emit(it[0].uppercase())
            }
    }

    override suspend fun saveResult(result: ResultGame) {
        when(result::class){
            WhereIsTheLetterResult::class -> {
                db.saveWhereIsTheLetterResult((result as WhereIsTheLetterResult).toWhereIsGameResult(), result.email)
            }
            CorrectWordGameResult::class -> {
                db.saveCorrectWordResult((result as CorrectWordGameResult).toCorrectWordDataResult(), result.email)
            }
        }
    }

}
