package com.example.lexiapp.ui.signup.professional

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.model.LoginResult
import com.example.lexiapp.domain.model.ProfessionalSignUp
import com.example.lexiapp.domain.useCases.ProfessionalSignUpUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfessionalSignUpViewModel @Inject constructor(
    private val professionalSignUpUseCases: ProfessionalSignUpUseCases
): ViewModel() {

    private var _showErrorDialog = MutableLiveData(false)
    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog
    private var _signUpSuccess = MutableLiveData(false)
    val signUpSuccess: LiveData<Boolean>
    get() = _signUpSuccess
    private var _isProfessionalAccountBeenVerified = MutableLiveData(false)
    val isProfessionalAccountBeenVerified: LiveData<Boolean>
    get() = _isProfessionalAccountBeenVerified

    fun signUpWithEmail(user: ProfessionalSignUp) {
        viewModelScope.launch(Dispatchers.IO) {
            when(professionalSignUpUseCases(user)) {
                LoginResult.Error -> {
                    withContext(Dispatchers.Main){
                        _showErrorDialog.value = true
                    }
                }
                LoginResult.Success -> {
                    professionalSignUpUseCases.saveProfessional(user)
                    withContext(Dispatchers.Main){
                        _signUpSuccess.value = true
                    }
                }
            }
        }
    }

    fun isProfessionalAccountBeenVerified(){
        viewModelScope.launch(Dispatchers.IO){
            val state = professionalSignUpUseCases.getLastProfessionalStatePreference()
            if(state == 1){
                withContext(Dispatchers.Main){
                    _isProfessionalAccountBeenVerified.value = true
                }
            }
        }
    }

}