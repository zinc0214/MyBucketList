package womenproject.com.mybury.presentation.write

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.DialogMemoImgAddBinding
import womenproject.com.mybury.databinding.WidgetWriteFragmentAddItemBinding
import womenproject.com.mybury.presentation.base.BaseActiviy
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import womenproject.com.mybury.ui.PermissionDialogFragment
import womenproject.com.mybury.util.FileUtil.getFileFromUri
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AddContentType {
    MEMO, PROFILE
}

@SuppressLint("ValidFragment")
class WriteMemoImgAddDialogFragment(
    private var addType: AddContentType,
    private var checkAddTypeAble: () -> Boolean,
    private var addTypeClickListener: () -> Unit,
    private var checkAddImageListener: () -> Boolean,
    private var imgAddListener: (File, Uri) -> Unit
) : BaseDialogFragment<DialogMemoImgAddBinding>() {


    private var photoUri: Uri? = null
    private var currentImgFile: File? = null

    private var imagePermitted = false
    private var cameraPermitted = false

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the cropped image URI.
            Log.e("ayhan", "croppedImageUri : ${result.cropRect}")
            photoUri = result.uriContent
            getFile(result)
        } else {
            val exception = result.error
        }
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                photoUri?.let {
                    cropImage(it)
                }
            }
        }

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let {
                cropImage(it)
            }
        }

    override val layoutResourceId: Int
        get() = R.layout.dialog_memo_img_add

    private val requestImagePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                imagePermitted = true
                goToGallery()
            } else {
                showNoPermissionDialog(requireActivity() as BaseActiviy)
            }
        }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                cameraPermitted = true
                takePhoto()
            } else {
                showNoPermissionDialog(requireActivity() as BaseActiviy)
            }
        }

    private fun initStartView() {
        if (!checkAddImageListener()) {
            binding.addAlbumImgLayout.disableAdd()
            binding.addCamImgLayout.disableAdd()
        }

        if (!checkAddTypeAble()) {
            binding.addMemoLayout.disableAdd()
        }

        when (addType) {
            AddContentType.MEMO -> {
                binding.addMemoLayout.writeItemLayout.visibility = View.VISIBLE
                binding.setBaseProfileImg.writeItemLayout.visibility = View.GONE
            }

            AddContentType.PROFILE -> {
                binding.setBaseProfileImg.writeItemLayout.visibility = View.VISIBLE
                binding.addMemoLayout.writeItemLayout.visibility = View.GONE
            }
        }
    }

    override fun initDataBinding() {

        binding.apply {
            addAlbumImgLayout.isAddable = true
            addCamImgLayout.isAddable = true
            addMemoLayout.isAddable = true

            initStartView()

            addMemoLayout.itemClickListener = memoAddOnClickListener
            addAlbumImgLayout.itemClickListener = getAlbumImgAndCropOnClickListener
            addCamImgLayout.itemClickListener = takePictureAndCropOnClickListener
            setBaseProfileImg.itemClickListener = baseProfileImgClickListener

            addMemoLayout.title = "메모 추가"
            addAlbumImgLayout.title = "앨범에서 사진 선택"
            addCamImgLayout.title = "사진 촬영"
            setBaseProfileImg.title = "기본 이미지로 변경"
        }

    }


    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.writeFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }


    private val memoAddOnClickListener = View.OnClickListener {
        if (binding.addMemoLayout.isAddable!!) {
            addTypeClickListener()
            this.dismiss()
        } else {
            Toast.makeText(context, "이미 메모가 있습니다.", Toast.LENGTH_SHORT).show()
        }

    }

    private val baseProfileImgClickListener = View.OnClickListener {
        addTypeClickListener()
        this.dismiss()
    }

    private val getAlbumImgAndCropOnClickListener = View.OnClickListener {
        if (checkImagePermission(this.requireContext(), activity as BaseActiviy)) {
            if (binding.addAlbumImgLayout.isAddable!!) {
                if (checkAddImageListener()) {
                    goToGallery()
                }
            } else {
                Toast.makeText(context, "더 이상 이미지를 추가하실 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val takePictureAndCropOnClickListener = View.OnClickListener {
        if (checkCameraPermission(this.requireContext(), activity as BaseActiviy)) {
            if (binding.addCamImgLayout.isAddable!!) {
                if (checkAddImageListener()) {
                    takePhoto()
                }
            } else {
                Toast.makeText(context, "더 이상 이미지를 추가하실 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkImagePermission(context: Context, activity: BaseActiviy): Boolean {
        val needPermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            }
        when {
            ContextCompat.checkSelfPermission(
                context,
                needPermission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                imagePermitted = true
                return true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                needPermission
            ) -> {
                showNoPermissionDialog(activity)
            }

            else -> {
                requestImagePermissionLauncher.launch(
                    needPermission
                )
            }
        }
        return false
    }

    private fun checkCameraPermission(context: Context, activity: BaseActiviy): Boolean {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                cameraPermitted = true
                return true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            ) -> {
                showNoPermissionDialog(activity)
            }

            else -> {
                requestCameraPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
        return false
    }

    private fun showNoPermissionDialog(activity: BaseActiviy) {
        val permissionDialogFragment = PermissionDialogFragment()
        permissionDialogFragment.show(activity.supportFragmentManager, "tag")
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(context, "취소 되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
    }

    private fun goToHome() {
        if (currentImgFile != null && photoUri != null) {
            imgAddListener.invoke(this.currentImgFile!!, this.photoUri!!)
        } else {
            Toast.makeText(requireContext(), "이미지를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
        this.dismiss()
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
            photoFile = createImageFile(requireContext())
        } catch (e: IOException) {
            Toast.makeText(
                requireContext(),
                "이미지를 가져오는데 실패했습니다.",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }

        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(
                requireContext(),
                "womenproject.com.mybury.fileprovider",
                photoFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            cameraLauncher.launch(intent)
        }
    }

    private fun getFile(result: CropImageView.CropResult) {
        if (photoUri == null) {
            Toast.makeText(
                requireContext(),
                "이미지를 가져오는데 실패했습니다.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
//            val imageInfo = result.uriContent?.let { uri ->
//                UserSelectedImageInfo(
//                    key = imageCount++,
//                    uri = uri,
//                    file = File(uri.path!!),
//                    path = result.getUriFilePath(this).orEmpty()
//                )
//            }
//            takePhotoAction.succeed(imageInfo!!)
            handleImageResult(result)
        }
    }

    private fun handleImageResult(result: CropImageView.CropResult) {
        if (result.isSuccessful) {
            result.uriContent?.let { uri ->
                try {
                    val file = getFileFromUri(requireContext(), uri)
                    if (file != null) {
                        photoUri = uri
                        currentImgFile = file
                        goToHome()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "이미지 처리 중 오류가 발생했습니다", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
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

    private fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        storageDir?.mkdirs()

        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    private fun WidgetWriteFragmentAddItemBinding.disableAdd() {
        this.writeItemText.setTextColor(requireContext().getColor(R.color._b4b4b4))
        this.isAddable = false
    }
}

fun <T : Number> Context?.dp2px(dp: T, default: Int = 0) =
    if (this == null) default else TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        this.resources.displayMetrics
    ).toInt()