package com.example.myapplication2345678

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.github.chrisbanes.photoview.PhotoView
import com.yalantis.ucrop.UCrop
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var bottomSheetDialogFragment : BlankFragment ? = null

    lateinit var list : List<filter>
    lateinit var button: ImageView
    lateinit var adjust : CircleImageView
    lateinit var crop : TextView
    lateinit var edit : CircleImageView
    lateinit var draw : CircleImageView
    lateinit var sticker : CircleImageView
    lateinit var dview : DragView
    lateinit var tick : ImageView

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
        sticker = findViewById(R.id.sticker)
        draw = findViewById(R.id.draw)
        dview = findViewById(R.id.dView)
        tick = findViewById(R.id.tick)
        dview.visibility  = View.GONE
        tick.visibility = View.GONE

//        dview.addText("Hello, World!", 200f, 200f, Color.BLUE)
//        val bitmap = BitmapFactory.decodeResource(resources,R.drawable.er);
//        val sbitmap = Bitmap.createScaledBitmap(bitmap,bitmap.width/4,bitmap.height/4,true)
//        dview.addSticker(sbitmap,100f,100f)

        sticker.setOnClickListener{
            dview.visibility = View.VISIBLE
            tick.visibility = View.VISIBLE
            val stickerFragment = sticker(dview)
            stickerFragment.show(supportFragmentManager,stickerFragment?.tag)
        }


        tick.setOnClickListener{
            val bitmap : Bitmap = mergeImageViewAndDragView(img,dview)
            img.setImageBitmap(bitmap)
            uri = converters.bitmapToUri(applicationContext, bitmap)
            img.setImageURI(uri)
            dview.visibility = View.GONE
            tick.visibility = View.GONE
            list = getFilterList(applicationContext).setFilter(bitmap)
            updateBottomFilters()
        }


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
//        val col : Button = findViewById(R.id.collage)
//        col.setOnClickListener{
//            val intent = Intent(this,photo2collage::class.java)
//            startActivity(intent)
//        }

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

        adjust = findViewById<CircleImageView?>(R.id.adjust)

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
            AdjustFragment.updateColorMatrix(applicationContext)
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


    fun mergeImageViewAndDragView(imageView: PhotoView, dragView: DragView): Bitmap {
        val width = maxOf(imageView.width, dragView.width)
        val height = maxOf(imageView.height, dragView.height)

        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)

        val drawable = imageView.drawable
        if (drawable is BitmapDrawable) {
            val matrix = imageView.imageMatrix
            val drawableBitmap = drawable.bitmap
            val transformedBitmap = Bitmap.createBitmap(drawableBitmap.width, drawableBitmap.height, Bitmap.Config.ARGB_8888)
            val transformedCanvas = Canvas(transformedBitmap)
            transformedCanvas.drawBitmap(drawableBitmap, matrix, null)

            canvas.drawBitmap(transformedBitmap, 0f, 0f, null)
        }

        dragView.draw(canvas)

        return resultBitmap
    }

}