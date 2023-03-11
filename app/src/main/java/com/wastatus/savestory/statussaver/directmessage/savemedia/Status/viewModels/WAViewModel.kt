package com.wastatus.savestory.statussaver.directmessage.savemedia.Status.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WAViewModel(val context: Context) : ViewModel() {

    val waList = MutableLiveData<WAPojo>()

    fun getData(context):LiveData<WAPojo>{


    }
}