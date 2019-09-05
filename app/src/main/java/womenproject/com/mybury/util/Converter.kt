package womenproject.com.mybury.util

import android.content.res.Resources
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


/**
 * Created by HanAYeon on 2018. 12. 4..
 */

class Converter {


    companion object {

        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().getDisplayMetrics().density).toInt()
        }

        fun stringFormat(origin : String, text1 :String): String {
            return String.format(origin, text1)
        }

        fun stringFormat(origin : String, text1 :String, text2 : String): String {
            return String.format(origin, text1, text2)
        }

    }
}


fun File.fileToMultipartFile() : MultipartBody.Part {
    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),this)
    val body = MultipartBody.Part.createFormData("fileName", this.name, requestFile)
    return body
}
