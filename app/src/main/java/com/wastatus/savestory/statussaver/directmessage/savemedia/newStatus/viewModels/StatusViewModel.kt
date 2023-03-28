package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.viewModels

import android.app.Application
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.pojos.StatusModel
import kotlinx.coroutines.launch
import org.apache.commons.io.comparator.LastModifiedFileComparator
import java.io.File
import java.util.*

class StatusViewModel(application: Application) : AndroidViewModel(application) {

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
            getMedia()
        }
    }


    fun getMedia() {
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
            whatsappList.postValue(whatsappItemList)
        }


    }
}

