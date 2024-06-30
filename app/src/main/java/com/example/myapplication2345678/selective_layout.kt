package com.example.myapplication2345678

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class selective_layout : AppCompatActivity() {

    private lateinit var pdf : LinearLayout
    private lateinit var collage : LinearLayout
    private lateinit var edit : LinearLayout
    private lateinit var compress : LinearLayout
    private lateinit var close : ImageView
    private lateinit var test : PhotoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selective_layout)

        val bounce_one = AnimationUtils.loadAnimation(this,R.anim.bounce)
        val bounce_two = AnimationUtils.loadAnimation(this,R.anim.bounce)
        val bounce_three = AnimationUtils.loadAnimation(this,R.anim.bounce)
        val bounce_four = AnimationUtils.loadAnimation(this,R.anim.bounce)
        val bounce_five = AnimationUtils.loadAnimation(this,R.anim.bounce)

        collage = findViewById(R.id.linearLayout)
        pdf = findViewById(R.id.linearLayout4)
        edit = findViewById(R.id.linearLayout2)
        compress = findViewById(R.id.linearLayout3)
        close = findViewById(R.id.close_main)
        test = findViewById(R.id.test)

        collage.setOnClickListener{
            collage.startAnimation(bounce_four)
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            startActivityForResult(intent,11013)
        }


        close.setOnClickListener{
            finish()
        }

        compress.setOnClickListener{
            compress.startAnimation(bounce_one)
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 11012)
        }

        edit.setOnClickListener{
            edit.startAnimation(bounce_two)
            val intent : Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        pdf.setOnClickListener{
            pdf.startAnimation(bounce_three)
            val intent : Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            startActivityForResult(intent,11011)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11011 && resultCode == RESULT_OK && data != null) {
            val clipData = data.clipData
            val imageUris: MutableList<Uri?> = ArrayList()
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val item = clipData.getItemAt(i)
                    imageUris.add(item.uri)
                }
            } else if (data.data != null) {
                imageUris.add(data.data)
            }
            createPdfFromImages(imageUris)
        }

        if (requestCode == 11012 && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri? = data.data
            imageUri?.let {
                compressImage(it)
            }
        }

        if (requestCode == 11013 &&  resultCode == RESULT_OK && data != null) {
            val clipData = data.clipData
            val imageUris: MutableList<Uri?> = ArrayList()
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val item = clipData.getItemAt(i)
                    imageUris.add(item.uri)
                }
            } else if (data.data != null) {
                imageUris.add(data.data)
            }

            if (imageUris.size == 4) {
                val bitmapProvider = getCList4(
                    this,
                    imageUris[0],
                    imageUris[1],
                    imageUris[2],
                    imageUris[3]
                )
                val bitmaps: List<Bitmap> = bitmapProvider.getCList()

                val intent = Intent(this,Collage_main::class.java)
                intent.putParcelableArrayListExtra("listC", ArrayList(imageUris))
                startActivity(intent)
               // test.setImageBitmap(bitmaps[0])
            }
        }
    }

    private fun createPdfFromImages(imageUris: MutableList<Uri?>) {
        val document = PdfDocument()

        val fileName = "pdf_${System.currentTimeMillis()}.pdf"
        var outputStream: OutputStream? = null

        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/Artify")
            }

            val uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
            if (uri != null) {
                outputStream = contentResolver.openOutputStream(uri)
            } else {
                throw IOException("Failed to create new MediaStore record.")
            }

            outputStream?.let {
                var pageNumber = 1
                for (uri in imageUris) {
                    uri?.let {
                        try {
                            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, pageNumber).create()
                            val page = document.startPage(pageInfo)
                            val canvas = page.canvas
                            canvas.drawBitmap(bitmap, 0f, 0f, null)
                            document.finishPage(page)
                            bitmap.recycle()
                            pageNumber++
                        } catch (e: Exception) {
                            Log.e("PDF Creation", "Error processing image URI: $uri", e)
                            Snackbar.make(findViewById<View>(android.R.id.content), "Error processing image URI: $uri", Snackbar.LENGTH_LONG).show()
                        }
                    } ?: run {
                        Log.e("PDF Creation", "Invalid image URI: $uri")
                        Snackbar.make(findViewById<View>(android.R.id.content), "Invalid image URI", Snackbar.LENGTH_LONG).show()
                    }
                }

                document.writeTo(it)
                Snackbar.make(findViewById<View>(android.R.id.content), "Pdf saved to Documents/Artify", Snackbar.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            Log.e("PDF Creation", "Error writing PDF", e)
            Snackbar.make(findViewById<View>(android.R.id.content), "Pdf is not Saved", Snackbar.LENGTH_LONG).show()
        } finally {
            try {
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            document.close()
        }
    }


    private fun compressImage(imageUri: Uri) {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        val byteArray = outputStream.toByteArray()
        val compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        save(compressedBitmap)
    }

    fun save(bitmap : Bitmap){
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Artify")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(directory, fileName)

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            stream.flush()
            stream.close()
            Snackbar.make(findViewById<View>(android.R.id.content),"Image saved to Storage/Picture/Artify", Snackbar.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Snackbar.make(findViewById<View>(android.R.id.content),"Image is not Saved", Snackbar.LENGTH_LONG).show()
        }
    }
}