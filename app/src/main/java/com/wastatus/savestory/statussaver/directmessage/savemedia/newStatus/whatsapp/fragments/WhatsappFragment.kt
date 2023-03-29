package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.whatsapp.viewModels.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wastatus.savestory.statussaver.directmessage.savemedia.R
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.SharedPrefs
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.pojos.StatusModel
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.viewModels.StatusViewModel
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.whatsapp.viewModels.adapters.WhatsappAdapter
import java.io.File
import kotlin.collections.ArrayList


class WhatsappFragment : Fragment() {
    private val REQUEST_ACTION_OPEN_DOCUMENT_TREE = 1001

    private lateinit var rv:RecyclerView
    private lateinit var allowAccess:LinearLayout
    private lateinit var waAdapter: WhatsappAdapter
    val viewModel: StatusViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_whatsapp, container, false)
//        if (SharedPrefs.getWATree(activity) != "") {
//            loadData()
//        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv = view.findViewById(R.id.rv)
        allowAccess = view.findViewById(R.id.llAccess)
        waAdapter = WhatsappAdapter(requireContext())
        rv.layoutManager = GridLayoutManager(requireContext(), 3)
        rv.adapter = waAdapter
        viewModel.whatsappList.observe(viewLifecycleOwner){

            waAdapter.setAdapter(it as ArrayList<StatusModel>)
            allowAccess.visibility = View.GONE
        }

        view.findViewById<LinearLayout>(R.id.llAccess).setOnClickListener { v ->
            if (Utils.appInstalledOrNot(
                    requireContext(),
                    "com.whatsapp"
                )
            ) {
                val sm =
                    requireActivity().getSystemService(Context.STORAGE_SERVICE) as StorageManager
                val intent: Intent
                val statusDir: String = getWAFolder()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    intent = sm.primaryStorageVolume.createOpenDocumentTreeIntent()
                    var uri =
                        intent.getParcelableExtra<Uri>("android.provider.extra.INITIAL_URI")
                    var scheme = uri.toString()
                    scheme = scheme.replace("/root/", "/document/")
                    scheme += "%3A$statusDir"
                    uri = Uri.parse(scheme)
                    intent.putExtra("android.provider.extra.INITIAL_URI", uri)
                } else {
                    intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                    intent.putExtra("android.provider.extra.INITIAL_URI", statusDir)
                }
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                startActivityForResult(intent, REQUEST_ACTION_OPEN_DOCUMENT_TREE)
            } else {
                Toast.makeText(
                    activity,
                    "Please Install WhatsApp For Download Status!!!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }



    }
    fun loadData() {

            viewModel.getWhatsappMedia(getFromSdcard())
    }

    private fun getFromSdcard(): Array<DocumentFile?>? {
        val treeUri = SharedPrefs.getWATree(activity)
        val fromTreeUri =
            DocumentFile.fromTreeUri(requireContext().applicationContext, Uri.parse(treeUri))
        return if (fromTreeUri != null && fromTreeUri.exists() && fromTreeUri.isDirectory
            && fromTreeUri.canRead() && fromTreeUri.canWrite()
        ) {
            fromTreeUri.listFiles()
        } else {
            null
        }
    }
    fun getWAFolder(): String {
        return if (File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + ".Statuses"
            ).isDirectory
        ) {
            "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"
        } else {
            "WhatsApp%2FMedia%2F.Statuses"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ACTION_OPEN_DOCUMENT_TREE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            Log.e("onActivityResult: ", "" + data?.data)
            try {
                requireContext().contentResolver
                    .takePersistableUriPermission(
                        uri!!,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            SharedPrefs.setWATree(activity, uri.toString())
            loadData()
        }
    }


}