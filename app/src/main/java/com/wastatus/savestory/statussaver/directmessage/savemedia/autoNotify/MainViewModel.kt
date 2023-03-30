package com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val days = MutableLiveData<Int>()
    init {
        days.value = 1
    }

    fun getPeriodWork() : LiveData<Int> {
        return days
    }


}