package womenproject.com.mybury.presentation.write

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.DialogMemoImgAddBinding
import womenproject.com.mybury.databinding.WidgetWriteFragmentAddItemBinding
import womenproject.com.mybury.presentation.base.BaseActiviy
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import womenproject.com.mybury.ui.PermissionDialogFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

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
    private var mCurrentPhotoPath: String? = null


    private var imagePermitted = false
    private var cameraPermitted = false


    override val layoutResourceId: Int
        get() = R.layout.dialog_memo_img_add

    private val requestImagePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                imagePermitted = true
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
        if (cameraPermitted || checkImagePermission(
                this.requireContext(),
                activity as BaseActiviy
            )
        ) {
            Log.e("ayhan", "${binding.addAlbumImgLayout.isAddable}")
            if (binding.addAlbumImgLayout.isAddable!!) {
                if (checkAddImageListener()) {
                    goToAlbum()
                }
            } else {
                Toast.makeText(context, "더 이상 이미지를 추가하실 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val takePictureAndCropOnClickListener = View.OnClickListener {
        if (cameraPermitted || checkCameraPermission(
                this.requireContext(),
                activity as BaseActiviy
            )
        ) {
            if (binding.addCamImgLayout.isAddable!!) {
                if (checkAddImageListener()) {
                    takePhoto()
                }
            } else {
                Toast.makeText(context, "더 이상 이미지를 추가하실 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (e: IOException) {
            Toast.makeText(context, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
            e.printStackTrace()
        }

        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(
                this.requireContext(),
                "MyBuryApplication.provider",
                photoFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(intent, PICK_FROM_CAMERA)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("HHmmss").format(Date())
        val imageFileName = "mybury_" + timeStamp + "_"
        val storageDir = File("${requireContext().getExternalFilesDir(null)}/mybury/")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        mCurrentPhotoPath = "file:" + image.absolutePath
        return image
    }

    private fun goToAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra("crop", true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }


    private fun checkImagePermission(context: Context, activity: BaseActiviy): Boolean {
        val needPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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

    private fun checkImagePermission13(context: Context, activity: BaseActiviy): Boolean {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                imagePermitted = true
                return true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.READ_MEDIA_IMAGES
            ) -> {
                showNoPermissionDialog(activity)
            }

            else -> {
                requestImagePermissionLauncher.launch(
                    Manifest.permission.READ_MEDIA_IMAGES
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
        when (requestCode) {
            PICK_FROM_ALBUM -> {
                if (data == null) {
                    return
                }
                photoUri = data.data
                cropImage()
            }

            PICK_FROM_CAMERA -> {
                cropImage()
                // 갤러리에 나타나게
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(photoUri!!.path), null
                ) { _, _ -> }
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    result.uri?.let {
                        photoUri = result.uri
                        goToHome()
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(requireContext(), "이미지를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                    this.dismiss()
                }
            }
        }
    }

    private fun goToHome() {
        getFile()
        if (currentImgFile != null && photoUri != null) {
            imgAddListener.invoke(this.currentImgFile!!, this.photoUri!!)
        } else {
            Toast.makeText(requireContext(), "이미지를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
        this.dismiss()
    }

    private fun getFile() {
        val src = BitmapFactory.decodeFile(photoUri?.path)
        val resized = Bitmap.createScaledBitmap(src, 700, 700, true)
        val file = saveBitmapAsFile(resized, photoUri?.path!!)
        currentImgFile = file
    }

    private fun saveBitmapAsFile(bitmap: Bitmap?, filePath: String): File {
        val file = File(filePath)
        var os: OutputStream
        try {
            file.createNewFile()
            os = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, os)
            os.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }


    private fun cropImage() {
        CropImage.activity(photoUri).setGuidelines(CropImageView.Guidelines.ON)
            .setAllowFlipping(false)
            .setAspectRatio(1, 1)
            .setScaleType(CropImageView.ScaleType.CENTER_CROP)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            //사각형 모양으로 자른다
            .start(requireContext(), this)
    }

    private fun WidgetWriteFragmentAddItemBinding.disableAdd() {
        this.writeItemText.setTextColor(requireContext().getColor(R.color._b4b4b4))
        this.isAddable = false
    }

    companion object {
        private val PICK_FROM_CAMERA = 1
        private val PICK_FROM_ALBUM = 2
        private val MULTIPLE_PERMISSIONS = 101

    }
}