package com.example.myapplication2345678

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt


class DragView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var bitmap: Bitmap? = null
    val stickers: MutableList<Sticker> = mutableListOf()
    val texts: MutableList<Text> = mutableListOf()
    private val paint = Paint()

    private var selectedSticker: Sticker? = null
    private var selectedText: Text? = null
    private var stickerOffsetX: Float = 0f
    private var stickerOffsetY: Float = 0f
    private var textOffsetX: Float = 0f
    private var textOffsetY: Float = 0f

    // Variables to handle pinch-to-zoom
    private var initialDistance: Float = 0f
    private var initialStickerWidth: Int = 0
    private var initialStickerHeight: Int = 0
    private var initialTextSize: Float = 40f

    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate()
    }

    fun addSticker(sticker: Bitmap, x: Float, y: Float) {
        stickers.add(Sticker(sticker, sticker, x, y))
        invalidate()
    }

    fun clear() {
        stickers.clear()
        texts.clear()
    }

    fun addText(text: String, x: Float, y: Float, color: Int) {
        texts.add(Text(text,x,y,color,40f))
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
            canvas.drawBitmap(sticker.currentBitmap, sticker.x, sticker.y, null)
        }

        for (text in texts) {
            paint.color = text.color  // Set text color in the Paint object
            paint.textSize = text.size
            canvas.drawText(text.text, text.x, text.y, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                selectedSticker = stickers.find { it.isInside(x, y) }
                selectedText = texts.find { it.isInside(x, y) }
                selectedSticker?.let {
                    stickerOffsetX = x - it.x
                    stickerOffsetY = y - it.y
                }
                selectedText?.let {
                    textOffsetX = x - it.x
                    textOffsetY = y - it.y
                }
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    initialDistance = getDistance(event)
                    selectedSticker?.let {
                        initialStickerWidth = it.originalBitmap.width
                        initialStickerHeight = it.originalBitmap.height
                    }
                    selectedText?.let {
                        initialTextSize = it.size
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount == 1) {
                    selectedSticker?.let {
                        it.x = x - stickerOffsetX
                        it.y = y - stickerOffsetY
                        invalidate()
                    }
                    selectedText?.let {
                        it.x = x - textOffsetX
                        it.y = y - textOffsetY
                        invalidate()
                    }
                } else if (event.pointerCount == 2) {
                    val newDistance = getDistance(event)
                    val scaleFactor = newDistance / initialDistance
                    selectedSticker?.let {
                        it.currentBitmap = Bitmap.createScaledBitmap(
                            it.originalBitmap, (initialStickerWidth * scaleFactor).toInt(),
                            (initialStickerHeight * scaleFactor).toInt(), true
                        )
                        invalidate()
                    }
                    selectedText?.let {
                        it.size = initialTextSize * scaleFactor
                        invalidate()
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                selectedSticker = null
                selectedText = null
            }
        }

        return true
    }

    private fun getDistance(event: MotionEvent): Float {
        val dx = (event.getX(0) - event.getX(1)).toFloat()
        val dy = (event.getY(0) - event.getY(1)).toFloat()
        return sqrt(dx * dx + dy * dy)
    }

    data class Sticker(val originalBitmap: Bitmap, var currentBitmap: Bitmap, var x: Float, var y: Float) {
        fun isInside(px: Float, py: Float): Boolean {
            return px >= x && px <= x + currentBitmap.width && py >= y && py <= y + currentBitmap.height
        }
    }

    data class Text(val text: String, var x: Float, var y: Float, var color: Int, var size: Float = 40f) {
        fun isInside(px: Float, py: Float): Boolean {
            val paint = Paint().apply {
                textSize = size
                this.color = this@Text.color
            }
            val textWidth = paint.measureText(text)
            val textHeight = paint.fontMetrics.bottom - paint.fontMetrics.top
            return px >= x && px <= x + textWidth && py >= y - textHeight && py <= y
        }
    }
}
