package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.whatsapp.viewModels.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.wastatus.savestory.statussaver.directmessage.savemedia.R
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.activities.PreviewActivity
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.pojos.StatusModel
import kotlin.collections.ArrayList

class SavedAdapter(val context: Context) : RecyclerView.Adapter<SavedAdapter.ViewHolder>() {
    private val mediaList = ArrayList<StatusModel>()

    fun setAdapter(list: ArrayList<StatusModel>) {
        mediaList.clear()
        mediaList.addAll(list)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.download_item, parent, false)
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


        Glide.with(context).load(mediaData.path).apply(
            RequestOptions().optionalTransform(RoundedCorners(5))
        ).into(holder.imageView)

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

        init {
            imageView = itemView.findViewById(R.id.imageView)
            imagePlayer = itemView.findViewById(R.id.ivPlay)
        }

    }


}