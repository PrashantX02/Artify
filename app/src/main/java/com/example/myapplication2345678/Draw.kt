package com.example.myapplication2345678

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.ActivityChooserView.InnerLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.chrisbanes.photoview.PhotoView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Draw : AppCompatActivity() {

    private lateinit var draw : DrawingView
    private lateinit var img : PhotoView
    private lateinit var save : Button
    private lateinit var view : ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)

        view = findViewById(R.id.view)

        val uri = intent.getStringExtra("uriofimg")

        draw = findViewById(R.id.drawing_view)
        draw.brushColour(Color.BLUE)
        draw.brushSize(20f)


        img = findViewById(R.id.img)
        img.setImageURI(Uri.parse(uri))


        save = findViewById(R.id.save)

        save.setOnClickListener{
            MainActivity.uri = bitmapToUri(getBitMapOfView(view))
            MainActivity.img.setImageURI(MainActivity.uri)
            MainActivity.changed_dataset = true

            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun getBitMapOfView(view:View) : Bitmap{
        val bitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun bitmapToUri(bitmap: Bitmap): Uri? {
        var uri: Uri? = null
        var fos: FileOutputStream? = null
        return try {
            val file = File(this.cacheDir, "temp_image.png")
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()

            uri = Uri.fromFile(file)
            uri
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            fos?.close()
        }
    }
}