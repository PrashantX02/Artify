package com.example.myapplication2345678

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.selects.select

class ColorAdapter(
    val context: Context,
    val list: List<color_data>,
    val draw: DrawingView,
    val selected: CircleImageView
) : RecyclerView.Adapter<ColorAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var circleImageView : CircleImageView

        init {
            circleImageView = itemView.findViewById(R.id.circular_color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.color_items,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.circleImageView.setColorFilter(list[position].color)
        holder.circleImageView.setOnClickListener{
            draw.brushColour(list[position].color)
            selected.setColorFilter(list[position].color)
        }

    }
}