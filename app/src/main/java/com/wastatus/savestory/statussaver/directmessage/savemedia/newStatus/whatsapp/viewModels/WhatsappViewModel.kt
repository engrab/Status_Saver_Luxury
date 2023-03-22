package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.viewModels

import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.pojos.SavedModel

class WhatsappViewModel() : ViewModel() {

    private val _whatsappList = MutableLiveData<List<SavedModel>>()
    val whatsappList = _whatsappList
    private val listOfItem = mutableListOf<SavedModel>()

    fun setWhatsappData(list: Array<DocumentFile?>?) {

        if (list != null && list.size > 0) {
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
            _whatsappList.postValue(listOfItem)
        }


    }


}

