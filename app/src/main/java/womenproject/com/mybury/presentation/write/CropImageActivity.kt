package womenproject.com.mybury.presentation.write

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CropImageActivity : AppCompatActivity() {

    private var photoUri: Uri? = null
    private var currentImgFile: File? = null
    private var isActionProcessed = false
    private var outputFile: File? = null

    companion object {
        const val EXTRA_ACTION_TYPE = "action_type"
        const val ACTION_GALLERY = "gallery"
        const val ACTION_CAMERA = "camera"
        const val EXTRA_RESULT_URI = "result_uri"
        const val EXTRA_RESULT_FILE = "result_file"
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

    // 시스템 사진 선택 도구. 저장소/미디어 권한 없이 사용자가 고른 이미지에만 접근한다.
    // (Photo Picker 미지원 기기에서는 ACTION_OPEN_DOCUMENT 로 자동 폴백된다.)
    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                cropImage(uri)
            } else {
                setResult(RESULT_CANCELED)
                finish()
            }
        }

    private val cropImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    // outputFile이 생성되었고 존재하는지 확인
                    if (outputFile != null && outputFile!!.exists()) {
                        currentImgFile = outputFile
                        photoUri = FileProvider.getUriForFile(
                            this,
                            "womenproject.com.mybury.fileprovider",
                            outputFile!!
                        )
                        goToHome()
                    } else {
                        Toast.makeText(this, "크롭된 이미지를 저장하지 못했습니다", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_CANCELED)
                        finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "이미지 처리 중 오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                    setResult(RESULT_CANCELED)
                    finish()
                }
            } else {
                Log.e("ayhan", "cropImageLauncher canceled")
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
        imageLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            val photoFile = createImageFile()
            photoUri = FileProvider.getUriForFile(
                this,
                "womenproject.com.mybury.fileprovider",
                photoFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            cameraLauncher.launch(intent)
        } catch (e: IOException) {
            Toast.makeText(
                this,
                "이미지를 가져오는데 실패했습니다.",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun cropImage(photoUri: Uri) {
        Log.e("ayhan", "cropImage : $photoUri")

        try {
            // 크롭할 이미지를 앱의 외부 저장소로 복사
            val copiedFile = copyUriToFile(photoUri)
            if (copiedFile == null) {
                Log.e("ayhan", "Failed to copy image file")
                Toast.makeText(this, "이미지를 복사하지 못했습니다", Toast.LENGTH_SHORT).show()
                setResult(RESULT_CANCELED)
                finish()
                return
            }

            val copiedUri = FileProvider.getUriForFile(
                this,
                "womenproject.com.mybury.fileprovider",
                copiedFile
            )
            Log.e("ayhan", "copiedUri: $copiedUri")

            // 크롭 결과를 저장할 새로운 파일 생성
            outputFile = createImageFile()
            Log.e("ayhan", "outputFile created: ${outputFile?.absolutePath}")

            val outputUri = FileProvider.getUriForFile(
                this,
                "womenproject.com.mybury.fileprovider",
                outputFile!!
            )
            Log.e("ayhan", "outputUri: $outputUri")

            // 표준 Android 크롭 인텐트 사용
            val intent = Intent("com.android.camera.action.CROP").apply {
                setDataAndType(copiedUri, "image/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                putExtra("crop", "true")
                putExtra("aspectX", 1)
                putExtra("aspectY", 1)
                putExtra("outputX", 500)
                putExtra("outputY", 500)
                putExtra("scale", true)
                putExtra("return-data", false)
                putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
            }

            Log.e("ayhan", "intent created successfully")

            // Grant permissions to the resolved activity
            val resInfoList =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                grantUriPermission(
                    packageName,
                    outputUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }


            // 크롭 인텐트를 처리할 수 있는 앱이 있는지 확인
            val resolveInfo = packageManager.resolveActivity(intent, 0)
            if (resolveInfo == null) {
                Log.e("ayhan", "No activity found to handle CROP action")
                Toast.makeText(this, "이미지 자르기 앱이 없습니다", Toast.LENGTH_SHORT).show()
                setResult(RESULT_CANCELED)
                finish()
                return
            }

            Log.e("ayhan", "Starting crop activity")
            cropImageLauncher.launch(intent)
        } catch (e: Exception) {
            Log.e("ayhan", "cropImage error: ${e.message}", e)
            e.printStackTrace()
            Toast.makeText(this, "이미지 자르기를 시작할 수 없습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun copyUriToFile(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            if (inputStream == null) {
                Log.e("ayhan", "Failed to open input stream")
                return null
            }

            // 캐시 디렉토리 대신 외부 저장소 사용
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            storageDir?.mkdirs()

            val copiedFile = File(storageDir, "temp_image_${System.currentTimeMillis()}.jpg")
            val outputStream = copiedFile.outputStream()

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            Log.e("ayhan", "Image copied to: ${copiedFile.absolutePath}")
            copiedFile
        } catch (e: Exception) {
            Log.e("ayhan", "Failed to copy image: ${e.message}", e)
            null
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
