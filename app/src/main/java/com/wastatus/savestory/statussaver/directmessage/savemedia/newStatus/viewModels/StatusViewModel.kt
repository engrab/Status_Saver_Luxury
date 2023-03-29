package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.viewModels

import android.content.Context
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.pojos.StatusModel
import kotlinx.coroutines.launch
import org.apache.commons.io.comparator.LastModifiedFileComparator
import java.io.File
import java.util.*

class StatusViewModel : ViewModel() {

    // status
    val savedList = MutableLiveData<List<StatusModel>>()
    private val savedItemList = mutableListOf<StatusModel>()

    // whatsapp

    val whatsappList = MutableLiveData<List<StatusModel>>()
     private val whatsappItemList = mutableListOf<StatusModel>()

    // whatsapp business

    val waBusinessList = MutableLiveData<List<StatusModel>>()
    private val waBusinessItemList = mutableListOf<StatusModel>()

    init {
        viewModelScope.launch {
            getSavedMedia()
        }
    }




    fun getSavedMedia() {
        val file = Utils.downloadWhatsAppDir
        if (!file.isDirectory) {
            return
        }
        val listMediaFiles = dirListByAscendingDate(file)
        if (listMediaFiles != null) {
            savedItemList.clear()
            for (listMediaFile in listMediaFiles) {

                if (listMediaFile != null) {
                    savedItemList.add(StatusModel(listMediaFile.absolutePath, listMediaFile.name, true))
                }

            }
        }

        savedList.postValue(savedItemList)
    }

    fun saveMedia(list: Array<DocumentFile?>?) {

        if (list != null && list.isNotEmpty()) {
            for (i in list.indices) {
                if (!list[i]?.uri.toString().contains(".nomedia")) {

                    savedItemList.add(
                        StatusModel(
                            list.get(i)?.uri.toString(),
                            list.get(i)?.name, false
                        )
                    )
                }
            }

            savedList.postValue(savedItemList)
        }


    }



    fun getWhatsappBusinessMedia(list: Array<DocumentFile?>?) {

        if (list != null && list.isNotEmpty()) {
            for (i in list.indices) {
                if (!list.get(i)?.uri.toString().contains(".nomedia")) {

                    waBusinessItemList.add(
                        StatusModel(
                            list.get(i)?.uri.toString(),
                            list.get(i)?.name, false
                        )
                    )
                }
            }
            compareWhatsappBusiness()
            waBusinessList.postValue(waBusinessItemList)
        }


    }


    fun getWhatsappMedia(list: Array<DocumentFile?>?) {

        if (list != null && list.size > 0) {
            for (i in list.indices) {
                if (!list.get(i)?.uri.toString().contains(".nomedia")) {

                    whatsappItemList.add(
                        StatusModel(
                            list.get(i)?.uri.toString(),
                            list.get(i)?.name, false
                        )
                    )
                }
            }
            compareWhatsapp()
            whatsappList.postValue(whatsappItemList)
        }


    }

    private fun compareWhatsapp( ) {

        if (whatsappItemList.size > 0 && savedItemList.size>0){

            for (i in whatsappItemList.indices) {
                for (j in savedItemList.indices) {
                    if (whatsappItemList[i].name == savedItemList[j].name) {
                        whatsappItemList[i].isSaved =
                            true
                    }
                }
            }
        }


    }

    private fun compareWhatsappBusiness( ) {

        if (waBusinessItemList.size > 0 && savedItemList.size>0){

            for (i in waBusinessItemList.indices) {
                for (j in savedItemList.indices) {
                    if (waBusinessItemList[i].name == savedItemList[j].name) {
                        waBusinessItemList[i].isSaved =
                            true
                    }
                }
            }
        }


    }

    fun dirListByAscendingDate(folder: File): Array<File?>? {
        if (!folder.isDirectory) {
            return null
        }
        val sortedByDate = folder.listFiles()
        if (sortedByDate == null || sortedByDate.size <= 1) {
            return sortedByDate
        }
        Arrays.sort(sortedByDate, LastModifiedFileComparator.LASTMODIFIED_REVERSE)
        return sortedByDate
    }
}

