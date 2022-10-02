package com.cranked.androidfileconverter.utils.image

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.cranked.androidfileconverter.BuildConfig
import com.cranked.androidfileconverter.utils.Constants
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.rendering.PDFRenderer
import kotlinx.coroutines.*
import java.io.File


object BitmapUtils {
    suspend fun pdfToBitmap(pdfFile: File): List<Bitmap> {
        val bitmaps = mutableListOf<Deferred<Bitmap>>()
        coroutineScope {
            try {
                val renderer = PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY))
                val pageCount = renderer.pageCount
                for (i in 0 until pageCount) {

                    val bitmap = async {
                        getImagePdf(pdfFile, i)
                    }
                    bitmaps.add(bitmap)
                }
                // close the renderer
                renderer.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return bitmaps.awaitAll()
    }

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

    fun getImagePdf(file: File): Bitmap {
        val pd = PDDocument.load(file)
        val pdfRenderer = PDFRenderer(pd)
        val bitmap = pdfRenderer.renderImageWithDPI(0, 100f)
        pd.close()
        return bitmap
    }

    suspend fun getImagePdf(file: File, pageIndex: Int): Bitmap {
        var bitmap: Bitmap
        val pd = PDDocument.load(file)
        val pdfRenderer = PDFRenderer(pd)
        bitmap = pdfRenderer.renderImageWithDPI(pageIndex, 100f)
        pd.close()
        return bitmap
    }

    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)

        return resizedBitmap
    }

    fun getRoundedBitmap(resources: Resources, bitmap: Bitmap, cornerRadius: Float): Bitmap {
        var roundedBitmap = RoundedBitmapDrawableFactory.create(resources, bitmap)
        roundedBitmap.cornerRadius = cornerRadius
        return roundedBitmap.toBitmap()
    }

    fun takePhoto(fragment: Fragment, path: String) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var file: File? = null

        if (intent.resolveActivity(fragment.requireActivity().packageManager) != null) {
            try {
                file = createImageFile(path)
            } catch (e: Exception) {
            }
            val uri = FileProvider.getUriForFile(fragment.requireActivity(), BuildConfig.APPLICATION_ID + ".provider", file!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            fragment.startActivityForResult(intent, Constants.RESULT_ADD_PHOTO)
        }
    }

    private fun createImageFile(filePath: String): File {
        val imageFileName = "JPEG_"
        val file = File(filePath)
        file.mkdirs()

        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            file /* directory */
        )
    }

    fun getBitmapFromPath(path: String) = BitmapFactory.decodeFile(path)

    fun setViewVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE

    }
}