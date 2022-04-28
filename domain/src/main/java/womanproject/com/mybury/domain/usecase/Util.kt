package womanproject.com.mybury.domain.usecase

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


fun File.fileToMultipartFile(name : String) : MultipartBody.Part {
    val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), this)
    val body = MultipartBody.Part.createFormData(name, this.name, requestFile)
    return body
}

fun Any.stringToMultipartFile(name : String) : MultipartBody.Part {
    val body = MultipartBody.Part.createFormData(name, this.toString())
    return body
}
