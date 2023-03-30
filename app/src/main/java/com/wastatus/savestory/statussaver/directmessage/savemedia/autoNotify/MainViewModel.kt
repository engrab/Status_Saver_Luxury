package com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val hours = MutableLiveData<Int>()
    init {
        hours.value = 24
    }

    fun getPeriodWork() : LiveData<Int> {
        return hours
    }


}