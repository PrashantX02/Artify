package com.example.myapplication2345678

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AdjustFragment() : BottomSheetDialogFragment(){


    companion object{

         lateinit var saturationSeekBar : SeekBar
         lateinit var contrastSeekBar: SeekBar
         lateinit var brightnessSeekBar : SeekBar
         lateinit var shadowSeekBar : SeekBar
         lateinit var warmSeekBar : SeekBar

        var colorMatrix = ColorMatrix()
        fun updateColorMatrix(context: Context) {
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
            MainActivity.img.colorFilter = colorFilter


            val file = converters.imageViewToBitmap2(MainActivity.img)
                ?.let { bitmapToFile(context, it) }
            val imageUri = Uri.fromFile(file)
            MainActivity.uri = imageUri
            MainActivity.img.setImageURI(MainActivity.uri)
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


        fun bitmapToFile(context: Context, bitmap: Bitmap): File {
            val file = File(context.cacheDir, "image2.jpg")

            try {
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return file
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context : Context = requireContext()
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
                updateColorMatrix(context)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        shadowSeekBar.setOnSeekBarChangeListener(object  : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorMatrix(context)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })



        saturationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorMatrix(context)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        brightnessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorMatrix(context)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        shadowSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorMatrix(context)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        contrastSeekBar= view.findViewById(R.id.Contrast)
        contrastSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorMatrix(context)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        // brushSizeSeekBar.progressDrawable.setColorFilter(ContextCompat.getColor(this,R.color.buttonbgc),PorterDuff.Mode.SRC_IN)
        contrastSeekBar.progressDrawable.setColorFilter(ContextCompat.getColor(context,R.color.buttonbgc),PorterDuff.Mode.SRC_IN)
        brightnessSeekBar.progressDrawable.setColorFilter(ContextCompat.getColor(context,R.color.buttonbgc),PorterDuff.Mode.SRC_IN)
        shadowSeekBar.progressDrawable.setColorFilter(ContextCompat.getColor(context,R.color.buttonbgc),PorterDuff.Mode.SRC_IN)
        warmSeekBar.progressDrawable.setColorFilter(ContextCompat.getColor(context,R.color.buttonbgc),PorterDuff.Mode.SRC_IN)
        saturationSeekBar.progressDrawable.setColorFilter(ContextCompat.getColor(context,R.color.buttonbgc),PorterDuff.Mode.SRC_IN)

        return view
    }


}