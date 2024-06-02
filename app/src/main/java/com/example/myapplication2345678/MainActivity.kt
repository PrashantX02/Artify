package com.example.myapplication2345678

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.yalantis.ucrop.UCrop
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.GPUImageView
import java.io.File
import java.io.IOException
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var bottomSheetDialogFragment : BlankFragment ? = null

    lateinit var list : List<filter>

    lateinit var img : PhotoView
    lateinit var button: ImageView
    lateinit var crop : TextView
    companion object {
        var uri: Uri? = null
    }

    var pass : Int = 0
    var changed_dataset : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        img  = findViewById(R.id.img)
        button  = findViewById(R.id.button)


        crop = findViewById(R.id.crop)


        var bottomSheetDialogFragment: BlankFragment? = null

        button.setOnClickListener {
            val intent  = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,1101)
        }

        val edit : Button = findViewById(R.id.edit)

        edit.setOnClickListener{
            if(pass == 55){
                if (bottomSheetDialogFragment == null || changed_dataset) {
                    bottomSheetDialogFragment = BlankFragment(list, img,this)
                    bottomSheetDialogFragment?.show(
                        supportFragmentManager,
                        bottomSheetDialogFragment?.tag
                    )

                    changed_dataset = false
                } else {
                    if (bottomSheetDialogFragment?.isVisible == true) {
                        bottomSheetDialogFragment?.dismiss()
                    } else {
                        bottomSheetDialogFragment?.show(
                            supportFragmentManager,
                            bottomSheetDialogFragment?.tag
                        )
                    }
                }
            }
        }

        crop.setOnClickListener{
            val destinationUri = Uri.fromFile(File(cacheDir, "croppedImage.jpg"))

            val option = UCrop.Options().apply {
                setToolbarTitle("Aritfy Crop")
            }
            uri?.let { it1 ->
                UCrop.of(it1,destinationUri!!)
                    .withOptions(option)
                    .withMaxResultSize(1000, 1000)
                    .start(this)
            }
        }
    }

    private fun setResultImage(uri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            img.setImageBitmap(bitmap)
            MainActivity.uri = uri
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1101 && resultCode == RESULT_OK){
            uri = data?.data
            img.setImageURI(uri)
            pass = 55
        }else if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                setResultImage(resultUri)
            }
        }

        if(uri != null){
            val inputStream = uri?.let { contentResolver.openInputStream(it) }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            list = getFilterList(applicationContext).setFilter(bitmap)
            changed_dataset = true
        }
    }

}