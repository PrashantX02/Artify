package com.example.myapplication2345678

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class Gadapter(private val array: Array<Bitmap>, private val context: Context,private val dView : DragView) : BaseAdapter() {
    override fun getCount(): Int {
        return array.size
    }

    override fun getItem(position: Int): Any {
        return array[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val gridView : View

        if(convertView == null){
            gridView = inflater.inflate(R.layout.glayoutimg,parent,false)
            val imageView = gridView.findViewById<ImageView>(R.id.grid_item_image)
            val bitmap : Bitmap =  array[position]
            val sbitmap = Bitmap.createScaledBitmap(bitmap,bitmap.width/4,bitmap.height/4,true)
            imageView.setImageBitmap(bitmap)

            imageView.setOnClickListener{
                dView.addSticker(sbitmap,100f,100f)
            }
        }else{
            gridView = convertView
        }

        return gridView
    }
}