package com.example.myapplication2345678

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class collageAdapter(private val context: Context, private val dataList: List<Int>,private val imageUris : List<Uri>) :
    RecyclerView.Adapter<collageAdapter.ViewHolder>() {

    private var previousGl : ConstraintLayout ? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.collage_cards, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.card_image.setImageResource(dataList[position])
        holder.card.setOnClickListener{
            holder.constraintLayout.setBackgroundResource(R.drawable.glow_white)

            if(position == 0){
                Collage_main.UpdateCollage(R.layout.c4four,imageUris,context)
            }else if (position == 1){
                Collage_main.UpdateCollage(R.layout.c4two,imageUris,context)
            }else if (position == 2){
                Collage_main.UpdateCollage(R.layout.c4five,imageUris,context)
            }else if(position == 3){
                Collage_main.UpdateCollage(R.layout.c4three,imageUris,context)
            }

            if(previousGl != null){
                previousGl!!.setBackgroundResource(R.color.LightBlack)
            }
            previousGl = holder.constraintLayout
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.card_collage)
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.glow_constrain)
        val card_image: ImageView = itemView.findViewById(R.id.card_image)
    }
}