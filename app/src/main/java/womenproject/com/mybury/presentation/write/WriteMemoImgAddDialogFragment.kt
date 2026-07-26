package womenproject.com.mybury.presentation.write

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.DialogMemoImgAddBinding
import womenproject.com.mybury.databinding.WidgetWriteFragmentAddItemBinding
import womenproject.com.mybury.presentation.base.BaseActiviy
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import womenproject.com.mybury.ui.PermissionDialogFragment
import java.io.File

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

    private val cropImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val resultUri = data?.getStringExtra(CropImageActivity.EXTRA_RESULT_URI)
                val resultFilePath = data?.getStringExtra(CropImageActivity.EXTRA_RESULT_FILE)

                if (resultUri != null && resultFilePath != null) {
                    val file = File(resultFilePath)
                    val uri = Uri.parse(resultUri)
                    imgAddListener.invoke(file, uri)
                    this.dismiss()
                } else {
                    Toast.makeText(requireContext(), "이미지를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "작업이 취소되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    override val layoutResourceId: Int
        get() = R.layout.dialog_memo_img_add

    private val requestCameraPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
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

    // 시스템 사진 선택 도구를 쓰므로 저장소/미디어 권한 확인이 필요 없다.
    private val getAlbumImgAndCropOnClickListener = View.OnClickListener {
        if (binding.addAlbumImgLayout.isAddable!!) {
            if (checkAddImageListener()) {
                goToGallery()
            }
        } else {
            Toast.makeText(context, "더 이상 이미지를 추가하실 수 없습니다.", Toast.LENGTH_SHORT).show()
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

    private fun checkCameraPermission(context: Context, activity: BaseActiviy): Boolean {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
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

    private fun goToGallery() {
        val intent = Intent(requireContext(), CropImageActivity::class.java)
        intent.putExtra(CropImageActivity.EXTRA_ACTION_TYPE, CropImageActivity.ACTION_GALLERY)
        cropImageLauncher.launch(intent)
    }

    private fun takePhoto() {
        val intent = Intent(requireContext(), CropImageActivity::class.java)
        intent.putExtra(CropImageActivity.EXTRA_ACTION_TYPE, CropImageActivity.ACTION_CAMERA)
        cropImageLauncher.launch(intent)
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