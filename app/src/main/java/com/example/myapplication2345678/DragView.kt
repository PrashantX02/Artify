package com.example.myapplication2345678

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DragView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var bitmap: Bitmap? = null
    private val stickers: MutableList<Sticker> = mutableListOf()
    private val texts: MutableList<Text> = mutableListOf()
    private val paint = Paint()

    private var selectedSticker: Sticker? = null
    private var selectedText: Text? = null
    private var offsetX: Float = 0f
    private var offsetY: Float = 0f

    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate()
    }

    fun addSticker(sticker: Bitmap, x: Float, y: Float) {
        stickers.add(Sticker(sticker, x, y))
        invalidate()
    }

    fun addText(text: String, x: Float, y: Float, color: Int) {
        texts.add(Text(text, x, y, color))
        invalidate()
    }

    fun updateTextColor(textIndex: Int, color: Int) {
        if (textIndex >= 0 && textIndex < texts.size) {
            texts[textIndex].color = color
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }

        for (sticker in stickers) {
            canvas.drawBitmap(sticker.bitmap, sticker.x + offsetX, sticker.y + offsetY, null)
        }

        for (text in texts) {
            paint.color = text.color
            paint.textSize = 40f
            canvas.drawText(text.text, text.x + offsetX, text.y + offsetY, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                selectedSticker = stickers.find { it.isInside(x - offsetX, y - offsetY) }
                selectedText = texts.find { it.isInside(x - offsetX, y - offsetY) }
                selectedSticker?.let {
                    offsetX = x - it.x
                    offsetY = y - it.y
                }
                selectedText?.let {
                    offsetX = x - it.x
                    offsetY = y - it.y
                }
            }

            MotionEvent.ACTION_MOVE -> {
                selectedSticker?.let {
                    it.x = x - offsetX
                    it.y = y - offsetY
                    invalidate()
                }
                selectedText?.let {
                    it.x = x - offsetX
                    it.y = y - offsetY
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                selectedSticker = null
                selectedText = null
            }
        }

        return true
    }

    private data class Sticker(val bitmap: Bitmap, var x: Float, var y: Float) {
        fun isInside(px: Float, py: Float): Boolean {
            return px >= x && px <= x + bitmap.width && py >= y && py <= y + bitmap.height
        }
    }

    private data class Text(val text: String, var x: Float, var y: Float, var color: Int) {
        fun isInside(px: Float, py: Float): Boolean {
            val paint = Paint().apply {
                textSize = 40f
            }
            val textWidth = paint.measureText(text)
            val textHeight = paint.fontMetrics.bottom - paint.fontMetrics.top
            return px >= x && px <= x + textWidth && py >= y - textHeight && py <= y
        }
    }
}