package com.example.myapplication2345678

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList

class Draw : AppCompatActivity() {

    private lateinit var draw : DrawingView
    private lateinit var img : PhotoView
    private lateinit var save : TextView
    private lateinit var view : ConstraintLayout
    private lateinit var brushSizeSeekBar: SeekBar
    private lateinit var selected : CircleImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)

        view = findViewById(R.id.view)
        selected = findViewById(R.id.selected)

        brushSizeSeekBar = findViewById(R.id.seekBar)

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

        brushSizeSeekBar.progressDrawable.setColorFilter(ContextCompat.getColor(this,R.color.buttonbgc),PorterDuff.Mode.SRC_IN)
        brushSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                draw.brushSize(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }


        })

        val list = getColorList()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        val adapter = ColorAdapter(this,list,draw,selected)
        recyclerView.adapter = adapter




    }

    fun getColorList() : List<color_data>{
        val list = ArrayList<color_data>()

        list.add(color_data(ContextCompat.getColor(this, R.color.red), "Red"))
        list.add(color_data(ContextCompat.getColor(this, R.color.green), "Green"))
        list.add(color_data(ContextCompat.getColor(this, R.color.blue), "Blue"))
        list.add(color_data(ContextCompat.getColor(this, R.color.yellow), "Yellow"))
        list.add(color_data(ContextCompat.getColor(this, R.color.purple), "Purple"))
        list.add(color_data(ContextCompat.getColor(this, R.color.orange), "Orange"))
        list.add(color_data(ContextCompat.getColor(this, R.color.pink), "Pink"))
        list.add(color_data(ContextCompat.getColor(this, R.color.cyan), "Cyan"))
        list.add(color_data(ContextCompat.getColor(this, R.color.teal), "Teal"))
        list.add(color_data(ContextCompat.getColor(this, R.color.brown), "Brown"))
        list.add(color_data(ContextCompat.getColor(this, R.color.gray), "Gray"))
        list.add(color_data(ContextCompat.getColor(this, R.color.light_blue), "Light Blue"))
        list.add(color_data(ContextCompat.getColor(this, R.color.lime), "Lime"))
        list.add(color_data(ContextCompat.getColor(this, R.color.amber), "Amber"))
        list.add(color_data(ContextCompat.getColor(this, R.color.deep_orange), "Deep Orange"))
        list.add(color_data(ContextCompat.getColor(this, R.color.indigo), "Indigo"))
        list.add(color_data(ContextCompat.getColor(this, R.color.deep_purple), "Deep Purple"))
        list.add(color_data(ContextCompat.getColor(this, R.color.light_green), "Light Green"))
        list.add(color_data(ContextCompat.getColor(this, R.color.blue_gray), "Blue Gray"))
        list.add(color_data(ContextCompat.getColor(this, R.color.black), "Black"))
        list.add(color_data(ContextCompat.getColor(this, R.color.magenta), "Magenta"))
        list.add(color_data(ContextCompat.getColor(this, R.color.gold), "Gold"))
        list.add(color_data(ContextCompat.getColor(this, R.color.silver), "Silver"))
        list.add(color_data(ContextCompat.getColor(this, R.color.bronze), "Bronze"))
        list.add(color_data(ContextCompat.getColor(this, R.color.navy), "Navy"))
        list.add(color_data(ContextCompat.getColor(this, R.color.olive), "Olive"))
        list.add(color_data(ContextCompat.getColor(this, R.color.maroon), "Maroon"))
        list.add(color_data(ContextCompat.getColor(this, R.color.beige), "Beige"))

        return list
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