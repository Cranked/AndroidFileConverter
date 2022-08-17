package com.cranked.androidfileconverter.utils.image

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.annotation.NonNull
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import com.cranked.androidfileconverter.R
import java.io.File


object BitmapUtils {
    fun pdfToBitmap(context: Context, pdfFile: File): ArrayList<Bitmap> {
        val bitmaps: ArrayList<Bitmap> = ArrayList()
        try {
            val renderer = PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY))
            var bitmap: Bitmap
            val pageCount = renderer.pageCount
            for (i in 0 until pageCount) {
                val page = renderer.openPage(i)
                val width: Int = context.resources.displayMetrics.densityDpi / 72 * page.width
                val height: Int = context.resources.displayMetrics.densityDpi / 72 * page.height
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                bitmaps.add(bitmap)

                // close the page
                page.close()
            }
            // close the renderer
            renderer.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return bitmaps
    }

    fun getImageOfPdf(context: Context, file: File, index: Int): Bitmap {
        val bitmaps: ArrayList<Bitmap> = ArrayList()
        try {
            val renderer = PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY))
            var bitmap: Bitmap
            val pageCount = renderer.pageCount
            for (i in 0..index) {
                val page = renderer.openPage(i)
                val width: Int = context.resources.displayMetrics.densityDpi / 72 * page.width
                val height: Int = context.resources.displayMetrics.densityDpi / 72 * page.height
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                bitmaps.add(bitmap)

                // close the page
                page.close()
            }
            // close the renderer
            renderer.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return bitmaps[index]
    }

    fun getRoundedBitmap(resources: Resources, bitmap: Bitmap, cornerRadius: Float): Bitmap {
        var roundedBitmap = RoundedBitmapDrawableFactory.create(resources, bitmap)
        roundedBitmap.cornerRadius = cornerRadius
        return roundedBitmap.toBitmap()
    }


}