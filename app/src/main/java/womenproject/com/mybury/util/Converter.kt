package womenproject.com.mybury.util

import android.content.res.Resources
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.annotation.AnyRes
import androidx.annotation.NonNull
import androidx.room.util.FileUtil


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

        fun getUriToDrawable(@NonNull context: Context,
                             @AnyRes drawableId: Int): Uri {
            return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + context.getResources().getResourcePackageName(drawableId)
                    + '/'.toString() + context.getResources().getResourceTypeName(drawableId)
                    + '/'.toString() + context.getResources().getResourceEntryName(drawableId))
        }
    }
}



fun fileListToMultipartFile(imgList : ArrayList<File>, name : String) : ArrayList<MultipartBody.Part> {
    val imageList = arrayListOf<MultipartBody.Part>()
    for(img in imgList) {
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), img)
        val uploadFile = MultipartBody.Part.createFormData(name, img.name, requestFile)
        imageList.add(uploadFile)
    }
    return imageList
}


fun File.fileToMultipartFile(name : String) : MultipartBody.Part {
    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),this)
    val body = MultipartBody.Part.createFormData(name, this.name, requestFile)
    return body
}

fun Any.stringToMultipartFile(name : String) : MultipartBody.Part {
    val body = MultipartBody.Part.createFormData(name, this.toString())
    return body
}
