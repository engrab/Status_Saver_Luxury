package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.viewModels

import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.pojos.SavedModel

class WABusinessViewModel() : ViewModel() {

    private val _waBusinessList = MutableLiveData<List<SavedModel>>()
    val waBusinessList = _waBusinessList
    private val listOfItem = mutableListOf<SavedModel>()

    fun setWhatsappData(list: Array<DocumentFile?>?) {

        if (list != null && list.isNotEmpty()) {
            for (i in list.indices) {
                if (!list.get(i)?.uri.toString().contains(".nomedia")) {

                    listOfItem.add(
                        SavedModel(
                            list.get(i)?.uri.toString(),
                            list.get(i)?.name, false
                        )
                    )
                }
            }
            _waBusinessList.postValue(listOfItem)
        }


    }


}

