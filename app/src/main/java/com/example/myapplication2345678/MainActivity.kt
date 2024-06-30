package com.example.myapplication2345678

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.ImageFormat
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.progressindicator.IndeterminateDrawable.createCircularDrawable
import com.google.android.material.snackbar.Snackbar
import com.yalantis.ucrop.UCrop
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.LinkedList
import java.util.Queue
import java.util.Stack

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
    lateinit var undo : ImageView
    lateinit var redo : ImageView
    lateinit var save : TextView
    lateinit var toHide : TextView
    lateinit var container : ConstraintLayout


    companion object {
        lateinit var tick : ImageView
        lateinit var close : ImageView

        var uri: Uri? = null
        lateinit var img : PhotoView

        var changed_dataset : Boolean = false

        // Imp // 22/5/24 Impl U/R stack
        var stack : Stack<Bitmap> = Stack()
        // Imp // 22/5/24 Impl U/R stack
        var stackR: Stack<Bitmap> = Stack()

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

        fun mergeImageViewAndPhotoView(imageView: PhotoView, adjplane: PhotoView): Bitmap {
            val width = maxOf(imageView.width, adjplane.width)
            val height = maxOf(imageView.height, adjplane.height)

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

            adjplane.draw(canvas)

            return resultBitmap
        }
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
        close = findViewById(R.id.close)
        undo = findViewById(R.id.Undo)
        redo = findViewById(R.id.Redo)
        save = findViewById(R.id.save)
        toHide = findViewById(R.id.textView)
        container = findViewById(R.id.container_1)

        dview.visibility  = View.GONE
        tick.visibility = View.GONE
        close.visibility = View.GONE





        val bounce_sticker = AnimationUtils.loadAnimation(this,R.anim.bounce)
        val bounce_undo = AnimationUtils.loadAnimation(this,R.anim.bounce)
        val bounce_redo = AnimationUtils.loadAnimation(this,R.anim.bounce)
        val bounce_adjust = AnimationUtils.loadAnimation(this,R.anim.bounce)
        val bounce_draw = AnimationUtils.loadAnimation(this,R.anim.bounce)
        val bounce_crop = AnimationUtils.loadAnimation(this,R.anim.bounce)
        val bounce_edit = AnimationUtils.loadAnimation(this,R.anim.bounce)
        val bounce_add = AnimationUtils.loadAnimation(this,R.anim.bounce)
        val bounce_save = AnimationUtils.loadAnimation(this,R.anim.bounce)

        save.setOnClickListener {
            save.startAnimation(bounce_save)
            save(converters.imageViewToBitmap2(img))
        }


        sticker.setOnClickListener{
            sticker.startAnimation(bounce_sticker)
            dview.visibility = View.VISIBLE
            val stickerFragment = sticker(dview)
            stickerFragment.show(supportFragmentManager,stickerFragment?.tag)
        }

        undo.setOnClickListener{
            undo.startAnimation(bounce_undo)
            if(!stack.isEmpty()) {
                val bitmap = stack.pop()
                stackR.push(converters.imageViewToBitmap2(img))
                img.setImageBitmap(bitmap)

                uri = converters.bitmapToUri(this,bitmap)
                list = getFilterList(applicationContext).setFilter(bitmap)
                updateBottomFilters()
            }
        }

        redo.setOnClickListener{
            redo.startAnimation(bounce_redo)
            if(!stackR.isEmpty()){
                val bitmap = stackR.pop()
                stack.push(converters.imageViewToBitmap2(img))

                img.setImageBitmap(bitmap)
                uri = converters.bitmapToUri(this,bitmap)
                list = getFilterList(applicationContext).setFilter(bitmap)
                updateBottomFilters()
            }
        }


        tick.setOnClickListener{
            stack.add(converters.imageViewToBitmap2(img))

            val bitmap : Bitmap = Draw.getBitMapOfView(container)
            img.setImageBitmap(bitmap)
            uri = converters.bitmapToUri(applicationContext, bitmap)
            img.setImageURI(uri)

            saveSetReset()

            dview.visibility = View.GONE
            tick.visibility = View.GONE
            close.visibility = View.GONE

            list = getFilterList(applicationContext).setFilter(bitmap)
            updateBottomFilters()
        }

        close.setOnClickListener{
            dview.clear()
            dview.visibility = View.GONE
            tick.visibility = View.GONE
            close.visibility = View.GONE
//            list = getFilterList(applicationContext).setFilter(bitmap)
//            updateBottomFilters()
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
            draw.startAnimation(bounce_draw)
            val intent = Intent(this,Draw ::class.java)
            intent.putExtra("uriofimg", uri.toString())
            startActivity(intent)

        }



        button  = findViewById(R.id.button)


        crop = findViewById(R.id.crop)


        var bottomSheetDialogFragment: BlankFragment? = null

        button.setOnClickListener {
            button.startAnimation(bounce_add)
            val intent  = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,1101)
        }

        edit = findViewById(R.id.edit)

        edit.setOnClickListener{
            edit.startAnimation(bounce_edit)
            if(pass == 55){
                stack.push(uri?.let { it1 -> converters.uriToBitmap(this, it1) })
                setBottomFilters()
            }
        }
//        val col : Button = findViewById(R.id.collage)
//        col.setOnClickListener{
//            val intent = Intent(this,photo2collage::class.java)
//            startActivity(intent)
//        }

        crop.setOnClickListener{
            crop.startAnimation(bounce_crop)
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

        adjust = findViewById(R.id.adjust)


        adjust.setOnClickListener {
            adjust.startAnimation(bounce_adjust)
            val originalBitmap = (img.drawable as BitmapDrawable).bitmap
            stack.push(originalBitmap)
            val adjust_blankfragment = AdjustFragment()
            adjust_blankfragment?.show(supportFragmentManager,adjust_blankfragment?.tag)
            adj  = 1
        }
    }


     fun setResultImage(uri: Uri) {
        try {
            val originalBitmap = (img.drawable as BitmapDrawable).bitmap
            stack.push(originalBitmap)

            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            img.setImageBitmap(bitmap)

            MainActivity.uri = uri
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1101 && resultCode == RESULT_OK){
            uri = data?.data
            img.setImageURI(uri)

//            val bitmap = uri?.let { converters.uriToBitmap(this, it) }
//            if(bitmap!=null){
//                img.layoutParams.width = bitmap.width
//                img.layoutParams.height = bitmap.width
//                img.requestLayout()
//            }

            toHide.visibility = View.GONE
            stack.clear()

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





    fun saveSetReset(){
        val bitmap : Bitmap = mergeImageViewAndDragView(img,dview)
        uri = converters.bitmapToUri(this,bitmap)
        img.setImageBitmap(bitmap)
        dview.clear()
    }
    fun mergeViews(photoView: PhotoView, dragView: DragView, drawView: DragView): Bitmap {
        // Determine the dimensions for the resulting bitmap
        val width = maxOf(photoView.width, dragView.width, dragView.width)
        val height = maxOf(photoView.height, dragView.height, dragView.height)

        // Create a bitmap to hold the result
        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)

        // Draw the PhotoView onto the canvas
        val photoDrawable = photoView.drawable
        if (photoDrawable is BitmapDrawable) {
            val matrix = photoView.imageMatrix
            val drawableBitmap = photoDrawable.bitmap
            val transformedBitmap = Bitmap.createBitmap(drawableBitmap.width, drawableBitmap.height, Bitmap.Config.ARGB_8888)
            val transformedCanvas = Canvas(transformedBitmap)
            transformedCanvas.drawBitmap(drawableBitmap, matrix, null)

            canvas.drawBitmap(transformedBitmap, 0f, 0f, null)
        }

        // Draw the first DragView onto the canvas
        dragView.draw(canvas)

        // Draw the second DragView onto the canvas
        drawView.draw(canvas)

        return resultBitmap
    }


    fun save(bitmap : Bitmap){
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Artify")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        // File name and path
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(directory, fileName)

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            Snackbar.make(findViewById<View>(android.R.id.content),"Image saved to Storage/Picture/Artify", Snackbar.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Snackbar.make(findViewById<View>(android.R.id.content),"Image is not Saved", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}