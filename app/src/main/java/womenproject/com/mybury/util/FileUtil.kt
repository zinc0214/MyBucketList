package womenproject.com.mybury.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileUtil {
    fun createTempImageFile(context: Context, fileName: String): File {
        val storageDir = context.getExternalFilesDir("my_images")
        storageDir?.mkdirs()
        return File(storageDir, fileName)
    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val fileName = "temp_${System.currentTimeMillis()}.png"
            val outputFile = createTempImageFile(context, fileName)
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(outputFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            outputFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
