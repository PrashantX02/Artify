package com.example.myapplication2345678

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.GPUImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class filterAdapter(val fragment: Fragment, val list: List<filter> , val image : ImageView) : RecyclerView.Adapter<filterAdapter.myholder>() {

    inner class myholder(itemView : View) : ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.image)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myholder {
        return myholder(LayoutInflater.from(fragment.context).inflate(R.layout.card,parent,false))
    }

    override fun onBindViewHolder(holder: myholder, position: Int) {
        holder.image.setImageBitmap(list[position].filterPreview)


        holder.image.setOnClickListener{
            val bitmap = list[position].filterPreview
            image.setImageBitmap(bitmap)

            val file = bitmapToFile(fragment.requireContext(),bitmap)
            val imageUri = Uri.fromFile(file)
            MainActivity.uri = imageUri
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun bitmapToFile(context: Context, bitmap: Bitmap): File {
        val file = File(context.cacheDir, "image.jpg")

        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
    }

}