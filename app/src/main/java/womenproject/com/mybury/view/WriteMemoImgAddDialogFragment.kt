package womenproject.com.mybury.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import womenproject.com.mybury.R
import womenproject.com.mybury.base.BaseDialogFragment
import womenproject.com.mybury.databinding.MemoImgAddDialogBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("ValidFragment")
class WriteMemoImgAddDialogFragment(private var memoAddListener: () -> Unit, private var checkAddImageListener: () -> Boolean,
                                    private var imgAddListener: (Uri) -> Unit) : BaseDialogFragment<MemoImgAddDialogBinding>() {


    private var photoUri: Uri? = null
    private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

    private var mCurrentPhotoPath: String? = null


    override val layoutResourceId: Int
        get() = R.layout.memo_img_add_dialog

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

        viewDataBinding.memoAddClickListener = memoAddOnClickListener()
        viewDataBinding.getAlbumImgListener = getAlbumImgAndCropOnClickListener()
        viewDataBinding.takePictureListener = takePictureAndCropOnClickListener()

    }


    companion object {

        private val PICK_FROM_CAMERA = 1
        private val PICK_FROM_ALBUM = 2
        private val CROP_FROM_CAMERA = 3
        private val MULTIPLE_PERMISSIONS = 101

    }

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.writeFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }


    private fun memoAddOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            memoAddListener.invoke()
            this.dismiss()

        }
    }


    private fun getAlbumImgAndCropOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            if (checkPermissions(this.context!!, activity as MainActivity)) {
                if (checkAddImageListener.invoke()) {
                    goToAlbum()
                }

            }

        }
    }

    private fun takePictureAndCropOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            if (checkPermissions(this.context!!, activity as MainActivity)) {
                if (checkAddImageListener.invoke()) {
                    takePhoto()
                }
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
            activity!!.finish()
            e.printStackTrace()
        }

        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(this.context!!, "MyBuryApplication.provider", photoFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(intent, PICK_FROM_CAMERA)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("HHmmss").format(Date())
        val imageFileName = "nostest_" + timeStamp + "_"
        val storageDir = File("${Environment.getExternalStorageDirectory()}/NOSTest/")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        mCurrentPhotoPath = "file:" + image.getAbsolutePath()
        return image
    }

    private fun goToAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }


    private fun checkPermissions(context: Context, activity: MainActivity): Boolean {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA))) {
                val permissionDialogFragment = PermissionNormalDialogFragment()
                permissionDialogFragment.show(activity.supportFragmentManager, "tag")
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), MULTIPLE_PERMISSIONS)
            }
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MULTIPLE_PERMISSIONS -> {
                for (i in 0..grantResults.size) {
                    if (grantResults[i] < 0) {
                        showNoPermissionToastAndFinish()
                        return
                    }
                }
            }
        }
    }


    private fun showNoPermissionToastAndFinish() {
        Toast.makeText(context, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show()
        activity!!.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(context, "취소 되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (requestCode == PICK_FROM_ALBUM) {
            if (data == null) {
                return
            }
            photoUri = data.data
            cropImage()
        } else if (requestCode == PICK_FROM_CAMERA) {
            cropImage()
            // 갤러리에 나타나게
            MediaScannerConnection.scanFile(context,
                    arrayOf(photoUri!!.getPath()), null,
                    object : MediaScannerConnection.OnScanCompletedListener {
                        override fun onScanCompleted(path: String, uri: Uri) {}
                    })
        } else if (requestCode == CROP_FROM_CAMERA) {
            Log.e("$this", "여기에 이미지 넣어야 함")
            imgAddListener.invoke(this.photoUri!!)
            this.dismiss()
        }
    }

    //Android N crop image
    fun cropImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activity!!.grantUriPermission("com.android.camera", photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(photoUri, "image/*")

        val list = context!!.packageManager.queryIntentActivities(intent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context!!.grantUriPermission(list[0].activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val size = list.size
        if (size == 0) {
            Toast.makeText(context, "취소 되었습니다.", Toast.LENGTH_SHORT).show()
            return
        } else {
            Toast.makeText(context, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            intent.putExtra("crop", "true")
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            intent.putExtra("scale", false)
            var croppedFileName: File? = null
            try {
                croppedFileName = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val folder = File("${Environment.getExternalStorageDirectory()}/NOSTest/")
            val tempFile = File(folder.toString(), croppedFileName!!.getName())

            photoUri = FileProvider.getUriForFile(context!!,
                    "MyBuryApplication.provider", tempFile)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }

            intent.putExtra("return-data", false)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())

            val i = Intent(intent)
            val res = list[0]
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                activity!!.grantUriPermission(res.activityInfo.packageName, photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            i.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            startActivityForResult(i, CROP_FROM_CAMERA)
        }
    }

}