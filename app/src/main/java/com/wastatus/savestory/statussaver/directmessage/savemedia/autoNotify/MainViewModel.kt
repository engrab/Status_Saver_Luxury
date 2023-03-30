package com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val time = MutableLiveData<Int>()
    init {
        time.value = 1
    }

    fun setPeriodWork() : LiveData<Int> {
        return time
    }


}