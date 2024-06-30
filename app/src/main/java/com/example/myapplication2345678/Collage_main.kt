package com.example.myapplication2345678

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.zip.Inflater

class Collage_main : AppCompatActivity() {

    lateinit var save : TextView

    companion object  {
        lateinit var c1 : ConstraintLayout
        lateinit var container : ConstraintLayout

        fun UpdateCollage(layout: Int, imageUris: List<Uri>,context: Context) {
            container.removeAllViews()

            val inflater: LayoutInflater? = LayoutInflater.from(context)
            val viewOfLayout = inflater?.inflate(layout, container, false) as ConstraintLayout
            viewOfLayout.id = View.generateViewId()
            container.addView(viewOfLayout)

            val im1 = viewOfLayout.findViewById<PhotoView>(R.id.photoView)
            val im2 = viewOfLayout.findViewById<PhotoView>(R.id.photoView2)
            val im3 = viewOfLayout.findViewById<PhotoView>(R.id.photoView3)
            val im4 = viewOfLayout.findViewById<PhotoView>(R.id.photoView4)

            im1.setImageURI(imageUris[0])
            im2.setImageURI(imageUris[1])
            im3.setImageURI(imageUris[2])
            im4.setImageURI(imageUris[3])
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collage_main)

        c1 = findViewById(R.id.importer)
        container = findViewById(R.id.constraintLayout4)
        save = findViewById(R.id.save_collage)

        save.setOnClickListener {
            val bitmap : Bitmap = getCList4.convertViewToBitmap(container)
            save(bitmap)
        }

        val imageUris: ArrayList<Uri>? = intent.getParcelableArrayListExtra("listC")


       if(imageUris!=null){

           UpdateCollage(R.layout.c4one,imageUris,this)

           val dataList = listOf(R.drawable.v4c1,R.drawable.v4c2,R.drawable.v4c3,R.drawable.v4c4) // Example: Load different images based on your data

           val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
           recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
           recyclerView.adapter = collageAdapter(this, dataList,imageUris)
       }else{
           Toast.makeText(this,"null",Toast.LENGTH_LONG).show()
       }
    }

    fun save(bitmap : Bitmap){
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Artify")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        // File name and path
        val fileName = "image_${System.currentTimeMillis()}.png"
        val file = File(directory, fileName)

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()

            container.visibility = View.GONE
            Snackbar.make(findViewById<View>(android.R.id.content),"Image saved to Storage/Picture/Artify", Snackbar.LENGTH_LONG).show()

            val intent = Intent(this,selective_layout::class.java)
            CoroutineScope(Dispatchers.Main).launch {
                delay(1500)
                startActivity(intent)
                finish()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Snackbar.make(findViewById<View>(android.R.id.content),"Image is not Saved", Snackbar.LENGTH_LONG).show()
        }
    }

}