package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.viewModels

import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.pojos.StatusModel
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.utlis.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.io.comparator.LastModifiedFileComparator
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class StatusViewModel : ViewModel() {

    // status

    private var _savedItemList = mutableListOf<StatusModel>()
    val savedList = MutableLiveData<List<StatusModel>>()

    // whatsapp

    private var _whatsappItemList = mutableListOf<StatusModel>()
    val whatsappList = MutableLiveData<List<StatusModel>>()

    // whatsapp business

    private var _waBusinessItemList = mutableListOf<StatusModel>()
    val waBusinessList = MutableLiveData<List<StatusModel>>()



    fun getSavedMedia() {
        viewModelScope.launch(Dispatchers.IO) {

            val file = Utils.downloadWhatsAppDir
            if (!file.isDirectory) {
                return@launch
            }
            val listMediaFiles = dirListByAscendingDate(file)
            if (listMediaFiles != null) {
                _savedItemList.clear()
                for (listMediaFile in listMediaFiles) {

                    if (listMediaFile != null) {

                        _savedItemList.add(
                            StatusModel(
                                listMediaFile.absolutePath,
                                listMediaFile.name,
                                true
                            )
                        )
                    }

                }
            }

            savedList.postValue(_savedItemList)
        }
    }

    fun saveMedia(list: Array<DocumentFile?>?) {

        if (list != null && list.isNotEmpty()) {
            for (i in list.indices) {
                if (!list[i]?.uri.toString().contains(".nomedia")) {

                    _savedItemList.add(
                        StatusModel(
                            list.get(i)?.uri.toString(),
                            list.get(i)?.name, false
                        )
                    )
                }
            }

            savedList.postValue(_savedItemList)
        }


    }
    fun saveWhatsappItem(position:Int){

        _whatsappItemList.get(position).isSaved = true
        whatsappList.postValue(_whatsappItemList)

    }
    fun saveWhatsappBusinessItem(position:Int){

        _waBusinessItemList.get(position).isSaved = true
        waBusinessList.postValue(_waBusinessItemList)
    }


    fun getWhatsappBusinessMedia(list: Array<DocumentFile?>?) {

        viewModelScope.launch(Dispatchers.IO) {

            if (list != null && list.isNotEmpty()) {
                _waBusinessItemList.clear()
                for (i in list.indices) {
                    if (!list.get(i)?.uri.toString().contains(".nomedia")) {

                        _waBusinessItemList.add(
                            StatusModel(
                                list.get(i)?.uri.toString(),
                                list.get(i)?.name, false
                            )
                        )
                    }
                }
                compareWhatsappBusiness()
                waBusinessList.postValue(_waBusinessItemList)
            }
        }

    }


    fun getWhatsappMedia(list: Array<DocumentFile?>?) {

        viewModelScope.launch(Dispatchers.IO) {

            if (list != null && list.size > 0) {
                _whatsappItemList.clear()
                for (i in list.indices) {
                    if (!list.get(i)?.uri.toString().contains(".nomedia")) {

                        _whatsappItemList.add(
                            StatusModel(
                                list.get(i)?.uri.toString(),
                                list.get(i)?.name, false
                            )
                        )
                    }
                }
                compareWhatsapp()
                whatsappList.postValue(_whatsappItemList)
            }

        }
    }

    private fun compareWhatsapp() {

        if (_whatsappItemList.size > 0 && _savedItemList.size > 0) {

            for (i in _whatsappItemList.indices) {
                for (j in _savedItemList.indices) {
                    if (_whatsappItemList[i].name == _savedItemList[j].name) {
                        _whatsappItemList[i].isSaved =
                            true
                    }
                }
            }
        }


    }

    private fun compareWhatsappBusiness() {

        if (_waBusinessItemList.size > 0 && _savedItemList.size > 0) {

            for (i in _waBusinessItemList.indices) {
                for (j in _savedItemList.indices) {
                    if (_waBusinessItemList[i].name == _savedItemList[j].name) {
                        _waBusinessItemList[i].isSaved =
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

