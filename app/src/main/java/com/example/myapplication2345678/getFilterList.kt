package com.example.myapplication2345678

import android.content.Context
import android.graphics.Bitmap
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorMatrixFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageRGBFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter
import java.util.ArrayList

class getFilterList(val context: Context) {

    fun setFilter(image :Bitmap) : List<filter>{

        val list  = ArrayList<filter>()

        val gpuimage = GPUImage(context).apply {
            setImage(image)
        }

        //0

        GPUImageColorMatrixFilter().also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Normal",filter,gpuimage.bitmapWithFilterApplied))
        }

        //1
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f,0.0f,0.0f,0.0f,
                0.0f,1.0f,0.2f,0.0f,
                0.1f,0.1f,0.1f,0.0f,
                1.0f,0.0f,0.0f,1.0f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Retro",filter,gpuimage.bitmapWithFilterApplied))
        }

         //2
        GPUImageColorMatrixFilter(
            0.9f,
            floatArrayOf(
                0.4f,0.6f,0.5f,0.0f,
                0.0f,0.4f,1.0f,0.0f,
                0.05f,0.1f,0.4f,0.4f,
                1.0f,1.0f,1.0f,1.0f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Just",filter,gpuimage.bitmapWithFilterApplied))
        }

        //3
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.25f,0.0f,0.2f,0.0f,
                0.0f,1.0f,0.2f,0.0f,
                0.0f,0.3f,1.0f,0.3f,
                0.0f,0.0f,0.0f,1.0f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Hume",filter,gpuimage.bitmapWithFilterApplied))
        }

        //4
        GPUImageColorMatrixFilter(
            0.9f,
            floatArrayOf(
                0.6f,0.4f,0.2f,0.05f,
                0.0f,0.8f,1.3f,0.05f,
                0.3f,0.3f,0.5f,0.08f,
                1.0f,0.0f,0.0f,1.0f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Desert",filter,gpuimage.bitmapWithFilterApplied))
        }


        //5
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f,0.5f,0.0f,0.0f,
                -0.2f,1.1f,-0.2f,0.11f,
                0.2f,0.0f,1.0f,0.0f,
                0.0f,0.0f,0.0f,1.0f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Old timos",filter,gpuimage.bitmapWithFilterApplied))
        }


        //6
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f,0.6f,0.08f,0.0f,
                0.4f,1.0f,0.0f,0.0f,
                0.0f,0.0f,1.0f,0.1f,
                0.0f,0.0f,0.0f,1.0f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Limos",filter,gpuimage.bitmapWithFilterApplied))
        }

        //7
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.5f,0f,0f,0f,
                0f,1f,0f,0f,
                0f,0f,1f,0f,
                0f,0f,0f,1f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("solar",filter,gpuimage.bitmapWithFilterApplied))
        }

        //8
        GPUImageSaturationFilter(2.0f).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("wole",filter,gpuimage.bitmapWithFilterApplied))
        }

        //9
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0f,1f,0f,0f,
                0f,1f,0f,0f,
                0f,0.6f,1f,0f,
                0f,0f,0f,1f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Neutron",filter,gpuimage.bitmapWithFilterApplied))
        }

        //10

        GPUImageRGBFilter(1.1f,1.3f,1.6f).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Bright",filter,gpuimage.bitmapWithFilterApplied))
        }

        //11
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.0f,1.0f,0.0f,0.0f,
                0.0f,1.0f,0.0f,0.0f,
                0.0f,1.0f,0.0f,0.0f,
                0.0f,1.0f,0.0f,1.0f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("BW",filter,gpuimage.bitmapWithFilterApplied))
        }


        //12
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.0f,0.0f,1.0f,0.0f,
                0.0f,0.0f,1.0f,0.0f,
                0.0f,0.0f,1.0f,0.0f,
                0.0f,0.0f,0.0f,1.0f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Clue",filter,gpuimage.bitmapWithFilterApplied))
        }

        //13
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0f,0f,1f,0f,
                1f,0f,0f,0f,
                0f,1f,0f,0f,
                0f,0f,0f,1f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Aero",filter,gpuimage.bitmapWithFilterApplied))
        }

        //14
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.763f,0.0f,0.2062f,0f,
                0.0f,0.9416f,0.0f,0f,
                0.1623f,0.2614f,0.8052f,0f,
                0f,0f,0f,1f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Classic",filter,gpuimage.bitmapWithFilterApplied))
        }

        //15
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.5162f,0.3799f,0.3247f,0f,
                0.039f,1.0f,0f,0f,
                -0.4773f,0.461f,1.0f,0f,
                0f,0f,0f,1f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Atom",filter,gpuimage.bitmapWithFilterApplied))
        }


        //16
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.0f,0.0f,0.5183f,0.3183f,
                0.0f,0.5497f,0.5416f,0f,
                0.5237f,0.5269f,0.0f,0f,
                0f,0f,0f,1f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Mars",filter,gpuimage.bitmapWithFilterApplied))
        }

        //17
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f,-0.3831f,0.3883f,0.0f,
                0.0f,1.0f,0.2f,0f,
                -0.1961f,0.0f,1.0f,0f,
                0f,0f,0f,1f
            )
        ).also { filter ->
            gpuimage.setFilter(filter)

            list.add(filter("Yeli",filter,gpuimage.bitmapWithFilterApplied))
        }



        return list;
    }
}