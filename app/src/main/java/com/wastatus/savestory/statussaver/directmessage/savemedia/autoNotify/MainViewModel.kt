package com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val day = MutableLiveData<Int>()
    init {
        day.value = 1
    }

    fun getPeriodWork() : LiveData<Int> {
        return day
    }


}