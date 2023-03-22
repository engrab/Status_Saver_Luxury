package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.viewModels

import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.model.DataModel
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.pojos.SavedModel
import org.apache.commons.io.comparator.LastModifiedFileComparator
import java.io.File
import java.util.*

class SavedViewModel() : ViewModel() {

    private val _savedList = MutableLiveData<List<SavedModel>>()
    val savedList = _savedList
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
            _savedList.postValue(listOfItem)
        }


    }
    fun getMedia() {
        val file = Utils.downloadWhatsAppDir
        if (!file.isDirectory) {
            return
        }
        val listMediaFiles = dirListByAscendingDate(file)
        if (listMediaFiles != null) {
            listOfItem.clear()
            for (listMediaFile in listMediaFiles) {

                if (listMediaFile != null) {
                    listOfItem.add(SavedModel(listMediaFile.absolutePath, listMediaFile.name, true))
                }

            }
        }
        _savedList.postValue(listOfItem)
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

