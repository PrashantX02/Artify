package com.example.myapplication2345678

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.color.utilities.Contrast

class AdjustFragment() : BottomSheetDialogFragment(){

    companion object{

         lateinit var saturationSeekBar : SeekBar
         lateinit var contrastSeekBar: SeekBar
         lateinit var brightnessSeekBar : SeekBar
         lateinit var shadowSeekBar : SeekBar
         lateinit var warmSeekBar : SeekBar

        var colorMatrix = ColorMatrix()
        fun updateColorMatrix() {
            colorMatrix.reset()

            // Apply saturation adjustment
            val saturation = saturationSeekBar.progress / 50f
            colorMatrix.setSaturation(saturation)

            // Apply brightness adjustment
            val brightness = brightnessSeekBar.progress / 50f
            val brightnessMatrix = ColorMatrix().apply { setScale(brightness, brightness, brightness, 1f) }
            colorMatrix.postConcat(brightnessMatrix)


            //shadow
            val shadow = shadowSeekBar.progress/100f
            applyShadow(shadow)

            //warm
            val warm = warmSeekBar.progress/100f
            applyWarmth(warm)

            //contrast
            val contrast = contrastSeekBar.progress/50f
            applyContrast(contrast)



            // Apply the combined color matrix to the image
            val colorFilter = ColorMatrixColorFilter(colorMatrix)
            MainActivity.img.setColorFilter(colorFilter)
        }

        private fun applyShadow(fadeIntensity: Float) {
            val intensity = fadeIntensity * 0.5f
            val fade = 1f - intensity
            val fadeMatrix = floatArrayOf(
                fade, 0f, 0f, 0f, 0f,
                0f, fade, 0f, 0f, 0f,
                0f, 0f, fade, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
            colorMatrix.postConcat(ColorMatrix(fadeMatrix))
        }

         fun applyContrast(contrast: Float) {
            val scale = contrast + 1f
            val translate = (-.5f * scale + .5f) * 255f
            val contrastMatrix = floatArrayOf(
                scale, 0f, 0f, 0f, translate,
                0f, scale, 0f, 0f, translate,
                0f, 0f, scale, 0f, translate,
                0f, 0f, 0f, 1f, 0f
            )
            colorMatrix.postConcat(ColorMatrix(contrastMatrix))
        }

         fun applyWarmth(warmth: Float) {
            val intensity = warmth * 0.5f

            val r = 1f + intensity * 0.1f
            val g = 1f + intensity * 0.05f
            val b = 1f - intensity * 0.1f
            val warmthMatrix = floatArrayOf(
                r, 0f, 0f, 0f, 0f,
                0f, g, 0f, 0f, 0f,
                0f, 0f, b, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
            colorMatrix.postConcat(ColorMatrix(warmthMatrix))
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_adjust, container, false)

        val lightBlack25Transparent = Color.parseColor("#00000000")
        view.setBackgroundColor(lightBlack25Transparent)

        saturationSeekBar  = view.findViewById(R.id.Saturation)
        saturationSeekBar.progress  = 50



        warmSeekBar  = view.findViewById(R.id.Warm)

        brightnessSeekBar  = view.findViewById(R.id.Brightness)
        brightnessSeekBar.progress  = 50

        shadowSeekBar = view.findViewById(R.id.Shadow)

        shadowSeekBar = view.findViewById(R.id.Shadow)
        shadowSeekBar.progress = 0



        warmSeekBar.setOnSeekBarChangeListener(object  : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorMatrix()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        shadowSeekBar.setOnSeekBarChangeListener(object  : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorMatrix()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })



        saturationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorMatrix()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        brightnessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorMatrix()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        shadowSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorMatrix()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        contrastSeekBar= view.findViewById(R.id.Contrast)
        contrastSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorMatrix()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        return view
    }


}