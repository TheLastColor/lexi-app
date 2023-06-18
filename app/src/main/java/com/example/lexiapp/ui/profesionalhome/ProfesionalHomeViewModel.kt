package com.example.lexiapp.ui.profesionalhome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import com.example.lexiapp.domain.useCases.CodeQRUseCases
import com.example.lexiapp.domain.useCases.LinkUseCases
import com.example.lexiapp.domain.useCases.ResultGamesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ProfesionalHomeViewModel @Inject constructor(
    private val codeQRUseCases: CodeQRUseCases,
    private val linkUseCases: LinkUseCases,
    private val resultGamesUseCases: ResultGamesUseCases
) : ViewModel() {

    private var _avg = MutableLiveData<String>()
    val avg = _avg as LiveData<String>
    private var _hardLetters = MutableLiveData<List<Char>>()
    val hardLetters = _hardLetters as LiveData<List<Char>>
    private var _countWordsPlay = MutableLiveData<Int>()
    val countWordsPlay = _countWordsPlay as LiveData<Int>
    private var _listPatient = MutableLiveData<List<User>>()
    val listPatient: LiveData<List<User>> = _listPatient
    private var _listFilterPatient = MutableLiveData<List<User>>()
    val listFilterPatient: LiveData<List<User>> = _listFilterPatient
    private var _patientSelected = MutableLiveData<User?>()
    val patientSelected: LiveData<User?> = _patientSelected
    private var _resultAddPatient = MutableLiveData<FirebaseResult>()
    val resultAddPatient: LiveData<FirebaseResult> = _resultAddPatient
    private var _resultDeletePatient = MutableLiveData<FirebaseResult>()
    val resultDeletePatient: LiveData<FirebaseResult> = _resultDeletePatient

    init {
        getPatient()
    }

    fun getPatient() {
        viewModelScope.launch {
            linkUseCases.getListLinkPatientOfProfessional { list ->
                val helpList = users(list)
                _listPatient.value = helpList
                _listFilterPatient.value = _listPatient.value
            }
        }
    }

    private fun users(list: List<String>?): List<User> {
        val helpList = mutableListOf<User>()
        viewModelScope.launch {
            list?.forEach {
                val patient = linkUseCases.getUser(it)
                Log.v("VALIDATE_FILTER_USERS", "${patient.userName}//${patient.email}")
                helpList.add(patient)
            }
        }
        return helpList
    }

    fun getScanOptions() = codeQRUseCases.getScanOptions()

    fun filter(patientSearch: String?) {
        val filteredList = mutableListOf<User>()
        if (patientSearch != null) {
            _listPatient.value?.forEach {
                if (it.userName != null &&
                    (it.userName!!.contains(patientSearch) || it.email.contains(patientSearch))
                )
                    filteredList.add(it)
            }
        }
        _listFilterPatient.value = filteredList
        filteredList.forEach { Log.v("VALIDATE_FILTER_USERS", "${it.userName}//${it.email}") }
    }

    fun getPatientEmail(contents: String?): String? {
        if (contents == null) return null
        return codeQRUseCases.getEmailFromQR(contents)
    }

    fun setPatientSelected(patient: User) {
        _patientSelected.value = patient               //aca llamar para ver los resultados.
        viewModelScope.launch {
            resultGamesUseCases.getWhereIsTheLetterResults(patient.email).collect {
                _countWordsPlay.value = it.size
                setHardLetters(it)
                setErrorAvg(it)
            }

        }
    }

    private fun setErrorAvg(results: List<WhereIsTheLetterResult>) {
        val subList = results.filter { !it.success }
        _avg.value =
            ((subList.size.toDouble() / results.size * 10000.0).roundToInt() / 100.0).toString()
    }

    private fun setHardLetters(results: List<WhereIsTheLetterResult>) {
        val list = mutableMapOf<Char, Int>()
        val filteredList = results.filter { !it.success }
        for (result in filteredList) {
            if (!list.contains(result.mainLetter)) {
                list[result.mainLetter] = 1
            } else {
                list[result.mainLetter]!!.plus(1)
            }
        }
        val maxCount = list.values.toSet().max()
        val letterList = mutableListOf<Char>()
        for (letter in list.keys) {
            if (list[letter] == maxCount) {
                letterList.add(letter)
            }
        }
        _hardLetters.value = letterList
    }

    fun cleanPatient() {
        _patientSelected.value = null
    }

    fun unbindPatient(emailPatient: String) {
        viewModelScope.launch {
            linkUseCases.deletePatientFromProfessional(emailPatient).collect {
                _resultDeletePatient.value = it
                linkUseCases.unBindProfessionalFromPatient(emailPatient).collect {}
            }
        }
    }

    fun addPatientToProfessional(emailPatient: String) {
        viewModelScope.launch {
            try {
                linkUseCases.addPatientToProfessional(emailPatient).collect {
                    _resultAddPatient.value = it
                    linkUseCases.bindProfessionalToPatient(emailPatient).collect {}
                }
            } catch (e: Exception) {
            }
        }
    }

}
