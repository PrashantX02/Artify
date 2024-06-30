package com.example.myapplication2345678

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.chrisbanes.photoview.PhotoView

class getCList4(
    context: Context,
    img1: Uri?,
    img2: Uri?,
    img3: Uri?,
    img4: Uri?
) {
    private val list  = ArrayList<Bitmap>()
    private lateinit var pOne : PhotoView
    private lateinit var pTwo : PhotoView
    private lateinit var pThree : PhotoView
    private lateinit var pFour : PhotoView

    init{
        val view:View = LayoutInflater.from(context).inflate(R.layout.c4one,null)
        pOne  = view.findViewById(R.id.photoView)
        pTwo = view.findViewById(R.id.photoView2)
        pThree = view.findViewById(R.id.photoView3)
        pFour= view.findViewById(R.id.photoView4)

        img1?.let { pOne.setImageURI(it) }
        img2?.let { pTwo.setImageURI(it) }
        img3?.let { pThree.setImageURI(it) }
        img4?.let { pFour.setImageURI(it) }

        val bitmap : Bitmap = convertViewToBitmap(view)

        list.add(bitmap)
    }

    fun getCList() : List<Bitmap> {
        return list
    }

    companion object{
        fun convertViewToBitmap(view: View): Bitmap {

            view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)

            val bitmap = Bitmap.createBitmap(
                view.measuredWidth,
                view.measuredHeight,
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(bitmap)
            view.draw(canvas)

            return bitmap
        }
    }

}