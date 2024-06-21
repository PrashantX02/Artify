package com.example.myapplication2345678

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [sticker.newInstance] factory method to
 * create an instance of this fragment.
 */
class sticker(private val dragView: DragView) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view :View =  inflater.inflate(R.layout.fragment_sticker, container, false)

        val gridView : GridView = view.findViewById(R.id.gridView);
        val images = arrayOf(
            BitmapFactory.decodeResource(resources,R.drawable.hug), BitmapFactory.decodeResource(resources,R.drawable.hipster), BitmapFactory.decodeResource(resources,R.drawable.iloveyou),
            BitmapFactory.decodeResource(resources,R.drawable.inlove), BitmapFactory.decodeResource(resources,R.drawable.handsign),  BitmapFactory.decodeResource(resources,R.drawable.teddy),
            BitmapFactory.decodeResource(resources,R.drawable.essentials), BitmapFactory.decodeResource(resources,R.drawable.sunglasses), BitmapFactory.decodeResource(resources,R.drawable.thinking),
            BitmapFactory.decodeResource(resources,R.drawable.sad)
        );

        val adapter : Gadapter =  Gadapter(images,requireContext(),dragView);
        gridView.adapter = adapter
        return view
    }


}