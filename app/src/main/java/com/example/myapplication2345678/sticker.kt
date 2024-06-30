package com.example.myapplication2345678

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
class sticker(private val dragView: DragView) : BottomSheetDialogFragment() {

    lateinit var text:EditText
    lateinit var add : TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view :View =  inflater.inflate(R.layout.fragment_sticker, container, false)

        text = view.findViewById(R.id.editText)
        add = view.findViewById(R.id.textView2)

        add.setOnClickListener {
            val line : String  = text.text.toString()
            dragView.addText(line,100f,100f,R.color.white)
        }

        val gridView : GridView = view.findViewById(R.id.gridView);
        val images = arrayOf(
            BitmapFactory.decodeResource(resources,R.drawable.hug), BitmapFactory.decodeResource(resources,R.drawable.hipster), BitmapFactory.decodeResource(resources,R.drawable.iloveyou),
            BitmapFactory.decodeResource(resources,R.drawable.inlove), BitmapFactory.decodeResource(resources,R.drawable.handsign),  BitmapFactory.decodeResource(resources,R.drawable.teddy),
            BitmapFactory.decodeResource(resources,R.drawable.essentials), BitmapFactory.decodeResource(resources,R.drawable.sunglasses), BitmapFactory.decodeResource(resources,R.drawable.thinking),
            BitmapFactory.decodeResource(resources,R.drawable.sad),BitmapFactory.decodeResource(resources,R.drawable.koala),BitmapFactory.decodeResource(resources,R.drawable.congratulations),
            BitmapFactory.decodeResource(resources,R.drawable.dog),BitmapFactory.decodeResource(resources,R.drawable.doglover),BitmapFactory.decodeResource(resources,R.drawable.makeawish),
            BitmapFactory.decodeResource(resources,R.drawable.youhavegotit),BitmapFactory.decodeResource(resources,R.drawable.makeawish)
        );

        val adapter : Gadapter =  Gadapter(images,requireContext(),dragView);
        gridView.adapter = adapter
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}