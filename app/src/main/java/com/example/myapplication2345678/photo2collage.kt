package com.example.myapplication2345678

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.chrisbanes.photoview.PhotoView
import com.yalantis.ucrop.UCrop

class photo2collage : AppCompatActivity() {

    private lateinit var img1 : PhotoView
    private lateinit var img2 : PhotoView
    private lateinit var pick : ImageView
    private lateinit var saved : ImageView
    private lateinit var ss : ConstraintLayout
    private lateinit var save : Button

    companion object{
        private const val REQUEST_CODE_SELECT_IMAGES = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo2collage)


        img1 = findViewById(R.id.p1)
        img2 = findViewById(R.id.p2)
        pick = findViewById(R.id.pick_up)
        saved  = findViewById(R.id.saved_image)
        ss = findViewById(R.id.ss)
        save = findViewById(R.id.save)

        pick.setOnClickListener{
            val intent  = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            }
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), REQUEST_CODE_SELECT_IMAGES)
        }

        save.setOnClickListener{
            saved.setImageBitmap(getBitmap(ss))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_SELECT_IMAGES && resultCode == RESULT_OK){
            val uriList = data?.clipData
            val imageUriList = mutableListOf<Uri>()

            if(uriList != null){
                for (i in 0 until uriList.itemCount){
                    imageUriList.add(uriList.getItemAt(i).uri)
                }

                img1.setImageURI(imageUriList[0])
                img2.setImageURI(imageUriList[1])

            }
        }
    }

    fun getBitmap(view:View) : Bitmap{
        val bitmap = Bitmap.createBitmap(view.width,view.height, Bitmap.Config.ARGB_8888)
        val canvas  = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}