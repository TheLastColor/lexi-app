package com.example.lexiapp.data.repository.challengereading

import com.example.lexiapp.data.network.FireStoreService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChallengeReadingRepositoryImpl @Inject constructor(private val firestoreService: FireStoreService) {

    suspend fun getFirestoreOpenAICollectionDocumentReference(document: String) = flow {
        firestoreService.getOpenAICollectionDocumentReference(document).collect{
            emit(it)
        }
    }
}