package com.example.myapplication2345678

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.github.chrisbanes.photoview.PhotoView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class converters {
    companion object{


        fun bitmapToUri(context: Context, bitmap: Bitmap): Uri {
            val file = File(context.cacheDir, "image3.jpg")

            try {
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val uriFile = converters.imageViewToBitmap2(MainActivity.img)
                ?.let { AdjustFragment.bitmapToFile(context, it) }
            val imageUri = Uri.fromFile(uriFile)

            return imageUri
        }
        fun imageViewToBitmap2(imageView: PhotoView): Bitmap {
            val drawable = imageView.drawable ?: throw IllegalArgumentException("ImageView does not have a drawable")

            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(bitmap)

            drawable.colorFilter = imageView.colorFilter
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            return bitmap
        }


        fun imageViewToBitmapWithoutChangeInternal(imageView: PhotoView): Bitmap? {
            val drawable: Drawable = imageView.drawable
            return when (drawable) {
                is BitmapDrawable -> drawable.bitmap
                is VectorDrawable -> getBitmapFromVectorDrawable(drawable)
                else -> null
            }
        }

        fun getBitmapFromVectorDrawable(vectorDrawable: VectorDrawable): Bitmap {
            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
            return bitmap
        }

        fun imageViewToUri(context: Context, imageView: PhotoView): Uri? {
            // Get the bitmap from ImageView
            val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap ?: return null

            // Create a file to save the bitmap
            val file = createImageFile(context)
                ?: // Handle file creation failure
                return null

            try {
                // Write the bitmap to the file
                val fileOutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()

                // Return the Uri for the saved file
                return Uri.fromFile(file)
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle IOException
            }

            return null
        }

        // Helper function to create a file in the external storage
        private fun createImageFile(context: Context): File? {
            val timeStamp = System.currentTimeMillis()
            val imageFileName = "JPEG_${timeStamp}_"
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(imageFileName, ".jpg", storageDir)
        }
    }
}