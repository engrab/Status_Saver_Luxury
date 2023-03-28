package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.whatsapp.viewModels.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.wastatus.savestory.statussaver.directmessage.savemedia.R
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.activities.PreviewActivity
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.SharedPrefs
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.pojos.StatusModel

class WhatsappAdapter(val context: Context) : RecyclerView.Adapter<WhatsappAdapter.ViewHolder>() {
    private val mediaList = ArrayList<StatusModel>()
    var path: String? = null

    fun setAdapter(list: ArrayList<StatusModel>) {
        mediaList.clear()
        mediaList.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.status_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mediaData = mediaList[position]
        if (!Utils.getBack(
                mediaData.name,
                "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)"
            ).isEmpty()
        ) {
            holder.imagePlayer.visibility = View.VISIBLE
        } else {
            holder.imagePlayer.visibility = View.GONE
        }
        if (mediaList[position].isSaved) {
            holder.downloadIV.setImageResource(R.drawable.ic_baseline_check_24)
        } else {
            holder.downloadIV.setImageResource(R.drawable.ic_baseline_download_24)
        }
        Glide.with(context).load(mediaData.path).apply(
            RequestOptions().optionalTransform(RoundedCorners(5))
        ).into(holder.imageView)
        holder.downloadIV.setOnClickListener { v: View? ->
            if (mediaList[position].isSaved) {
                Toast.makeText(context, "Media Already Downloaded", Toast.LENGTH_LONG).show()
            } else {
                if (SharedPrefs.getAutoSave(context)) {
                    Utils.saveWAData(context)
                    for (i in mediaList.indices) {
                        mediaList[i].isSaved = true
                    }
                } else {
                    Utils.copyFileInSavedDir(
                        context,
                        mediaData.path,
                        mediaData.name
                    )
                    Toast.makeText(context, "Media Download successfully!", Toast.LENGTH_LONG)
                        .show()
                    holder.downloadIV.setImageResource(R.drawable.ic_baseline_check_24)
                    mediaList[position].isSaved = true
                }
                notifyDataSetChanged()
                if (AdmobAdsManager.isAdmob) {
                    AdmobAdsManager.showInterAd(context as Activity?, null)
                }
            }
        }

        holder.imageView.setOnClickListener {
            val intent = Intent(context, PreviewActivity::class.java)
            intent.putParcelableArrayListExtra("images", mediaList)
            intent.putExtra("position", position)
            intent.putExtra("statusdownload", "status")
            intent.putExtra("folderpath", mediaList[position].name)
            intent.putExtra("pakage", "com.whatsapp")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return mediaList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagePlayer: ImageView
        val imageView: ImageView
        val downloadIV: ImageView

        init {
            imageView = itemView.findViewById(R.id.imageView)
            imagePlayer = itemView.findViewById(R.id.ivPlay)
            downloadIV = itemView.findViewById(R.id.downloadIV)
        }

    }


}