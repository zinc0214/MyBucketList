package womenproject.com.mybury.presentation.write

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import womenproject.com.mybury.util.FileUtil.getFileFromUri
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CropImageActivity : AppCompatActivity() {

    private var photoUri: Uri? = null
    private var currentImgFile: File? = null
    private var isActionProcessed = false

    companion object {
        const val EXTRA_ACTION_TYPE = "action_type"
        const val ACTION_GALLERY = "gallery"
        const val ACTION_CAMERA = "camera"
        const val EXTRA_RESULT_URI = "result_uri"
        const val EXTRA_RESULT_FILE = "result_file"
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            Log.e("ayhan", "croppedImageUri : ${result.cropRect}")
            photoUri = result.uriContent
            handleImageResult(result)
        } else {
            val exception = result.error
            Log.e("ayhan", "crop exception : $exception")
            Toast.makeText(this, "크롭 작업 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show()
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                photoUri?.let {
                    cropImage(it)
                }
            } else {
                setResult(RESULT_CANCELED)
                finish()
            }
        }

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let {
                    cropImage(it)
                }
            } else {
                setResult(RESULT_CANCELED)
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isActionProcessed) {
            val actionType = intent.getStringExtra(EXTRA_ACTION_TYPE)
            when (actionType) {
                ACTION_GALLERY -> goToGallery()
                ACTION_CAMERA -> takePhoto()
                else -> {
                    setResult(RESULT_CANCELED)
                    finish()
                }
            }
            isActionProcessed = true
        }
    }

    private fun goToGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra("crop", true)
        intent.action = Intent.ACTION_GET_CONTENT
        imageLauncher.launch(intent)
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null

        try {
            photoFile = createImageFile()
        } catch (e: IOException) {
            Toast.makeText(
                this,
                "이미지를 가져오는데 실패했습니다.",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
            setResult(RESULT_CANCELED)
            finish()
            return
        }

        photoUri = FileProvider.getUriForFile(
            this,
            "womenproject.com.mybury.fileprovider",
            photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        cameraLauncher.launch(intent)
    }

    private fun cropImage(photoUri: Uri) {
        Log.e("ayhan", "cropImage : $photoUri")
        cropImage.launch(
            CropImageContractOptions(
                uri = photoUri,
                cropImageOptions = CropImageOptions(
                    maxZoom = 3,
                    showCropLabel = true,
                    showCropOverlay = true,
                    guidelines = CropImageView.Guidelines.ON,
                    outputCompressFormat = Bitmap.CompressFormat.JPEG,
                    outputCompressQuality = 90,
                    cropMenuCropButtonTitle = "저장",
                    activityTitle = "이미지 자르기",
                    aspectRatioX = 1,
                    aspectRatioY = 1,
                    fixAspectRatio = true,
                    initialCropWindowPaddingRatio = 0f
                )
            )
        )
    }

    private fun handleImageResult(result: CropImageView.CropResult) {
        if (result.isSuccessful) {
            result.uriContent?.let { uri ->
                try {
                    val file = getFileFromUri(this, uri)
                    if (file != null) {
                        photoUri = uri
                        currentImgFile = file
                        goToHome()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "이미지 처리 중 오류가 발생했습니다", Toast.LENGTH_SHORT)
                        .show()
                    setResult(RESULT_CANCELED)
                    finish()
                }
            }
        }
    }

    private fun goToHome() {
        if (currentImgFile != null && photoUri != null) {
            val intent = Intent()
            intent.putExtra(EXTRA_RESULT_URI, photoUri.toString())
            intent.putExtra(EXTRA_RESULT_FILE, currentImgFile!!.absolutePath)
            setResult(RESULT_OK, intent)
        } else {
            Toast.makeText(this, "이미지를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            setResult(RESULT_CANCELED)
        }
        finish()
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        storageDir?.mkdirs()

        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
}
