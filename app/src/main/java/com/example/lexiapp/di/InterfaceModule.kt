package com.example.lexiapp.di

import android.content.SharedPreferences
import com.example.lexiapp.data.network.DifferenceServiceImpl
import com.example.lexiapp.data.network.LetterServiceImpl
import com.example.lexiapp.data.network.SpeechToTextServiceImpl
import com.example.lexiapp.data.api.difference_text.DifferenceGateway
import com.example.lexiapp.data.api.notifications.FirebaseNotificationClient
import com.example.lexiapp.data.api.openai_audio.SpeechToTextGateway
import com.example.lexiapp.data.api.openaicompletions.OpenAICompletionsGateway
import com.example.lexiapp.data.network.OpenAICompletionsServiceImpl
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationGateway
import com.example.lexiapp.data.network.*
import com.example.lexiapp.data.network.ChallengeReadingServiceImpl
import com.example.lexiapp.domain.service.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object InterfaceModule {

    @Provides
    fun getLetterRepository(
        apiWordService: WordAssociationGateway,
        db: FireStoreServiceImpl,
        prefs: SharedPreferences
    ): LetterService {
        return LetterServiceImpl(apiWordService, db, prefs)
    }

    @Provides
    fun getOpenAICompletionsRepository(
        openAICompletionsService: OpenAICompletionsGateway
    ): OpenAICompletionsService {
        return OpenAICompletionsServiceImpl(openAICompletionsService)
    }

    @Provides
    fun getChallengeReadingRepository(
        firestoreService: FireStoreServiceImpl
    ): ChallengeReadingService {
        return ChallengeReadingServiceImpl(firestoreService)
    }

    @Provides
    fun getAuthenticationService(
        firebase: FirebaseClient
    ): AuthenticationService {
        return AuthenticationServiceImpl(firebase)
    }

    @Provides
    fun getFirestoreService(
        firebase: FirebaseClient
    ): FireStoreService {
        return FireStoreServiceImpl(firebase)
    }

    @Provides
    fun getSpeechToTextService(
        apiService: SpeechToTextGateway,
        firestoreService: FireStoreServiceImpl
    ): SpeechToTextService {
        return SpeechToTextServiceImpl(apiService, firestoreService)
    }

    @Provides
    fun getDifferenceService(
        apiDifferenceGateway: DifferenceGateway,
        db: FireStoreService
    ): DifferenceService {
        return DifferenceServiceImpl(apiDifferenceGateway, db)
    }

    @Provides
    fun getObjectiveService(
        firestoreService: FireStoreServiceImpl
    ): ObjectivesService {
        return ObjectivesServiceImpl(firestoreService)
    }

    @Provides
    fun getResultGameService(db: FireStoreService): ResultGamesService {
        return ResultGamesServiceImpl(db)
    }

    @Provides
    fun getFirebaseNotificationService(
        firebaseCloudMessagingClient: FirebaseNotificationClient
    ): FirebaseNotificationService {
        return FirebaseNotificationServiceImpl(firebaseCloudMessagingClient)
    }
}