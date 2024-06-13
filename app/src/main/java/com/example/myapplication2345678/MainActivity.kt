package com.example.myapplication2345678

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ColorMatrix
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
import com.squareup.picasso.Picasso
import com.yalantis.ucrop.UCrop
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.GPUImageView
import java.io.File
import java.io.IOException
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var bottomSheetDialogFragment : BlankFragment ? = null

    lateinit var list : List<filter>
    lateinit var button: ImageView
    lateinit var adjust : Button
    lateinit var crop : TextView
    lateinit var edit : Button
    lateinit var draw : Button
    lateinit var dview : DragView

    companion object {
        var uri: Uri? = null
        lateinit var img : PhotoView
        var changed_dataset : Boolean = false
    }

    var pass : Int = 0
    var adj : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        img  = findViewById(R.id.img)
        draw = findViewById(R.id.draw)
        dview = findViewById(R.id.dview)

        //dview.addText("Hello, World!", 200f, 200f,Color.BLUE)
        val bitmap = BitmapFactory.decodeResource(resources,R.drawable.er);
        val sbitmap = Bitmap.createScaledBitmap(bitmap,bitmap.width/4,bitmap.height/4,true)
        dview.addSticker(sbitmap,100f,100f)

        if(changed_dataset) {
            pass = 55

            val inputStream = uri?.let { contentResolver.openInputStream(it) }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            list = getFilterList(applicationContext).setFilter(bitmap)

            img.setImageURI(uri)
            updateBottomFilters()

            changed_dataset = false
        }

        draw.setOnClickListener{
            val intent = Intent(this,Draw ::class.java)
            intent.putExtra("uriofimg", uri.toString())
            startActivity(intent)
        }



        button  = findViewById(R.id.button)


        crop = findViewById(R.id.crop)


        var bottomSheetDialogFragment: BlankFragment? = null

        button.setOnClickListener {
            val intent  = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,1101)
        }

        edit = findViewById(R.id.edit)

        edit.setOnClickListener{
            if(pass == 55){
                setBottomFilters()
            }
        }
        val col : Button = findViewById(R.id.collage)
        col.setOnClickListener{
            val intent = Intent(this,photo2collage::class.java)
            startActivity(intent)
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

        adjust = findViewById<Button?>(R.id.adjust)

        adjust.setOnClickListener {
            val adjust_blankfragment = AdjustFragment()
            adjust_blankfragment?.show(supportFragmentManager,adjust_blankfragment?.tag)
            adj  = 1
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
        if(adj == 1) {
            AdjustFragment.brightnessSeekBar.progress = 50
            AdjustFragment.saturationSeekBar.progress = 50
            AdjustFragment.shadowSeekBar.progress = 0
            AdjustFragment.contrastSeekBar.progress = 0
            AdjustFragment.updateColorMatrix()
        }
    }


    fun updateBottomFilters(){
        bottomSheetDialogFragment = BlankFragment(list, img,this)
    }
    fun setBottomFilters(){
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