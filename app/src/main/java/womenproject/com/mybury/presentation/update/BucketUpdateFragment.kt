package womenproject.com.mybury.presentation.update

import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.data.DetailBucketItem
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.write.BucketWriteFragment
import java.util.*

class BucketUpdateFragment : BucketWriteFragment() {

    lateinit var bucketItem: DetailBucketItem
    lateinit var bucketId: String

    override fun initForUpdate() {

        loadArgument()

        viewModel.bucketItem = bucketItem
        if(alreadyImgList.isNullOrEmpty()) {
            alreadyImgList = setImgList(bucketItem)
        }

        binding.titleText.setText(bucketItem.title)

        if (bucketItem.memo.isNotBlank()) {
            binding.memoLayout.visibility = View.VISIBLE
            binding.memoText.setText(bucketItem.memo)
        }

        if (bucketItem.open) {
            binding.openSwitchBtn.transitionToStart()
        } else {
            binding.openSwitchBtn.transitionToEnd()
            binding.openText.text = requireContext().resources.getString(R.string.bucket_close)
            binding.openImg.background = requireContext().getDrawable(R.drawable.open_disable)
        }

        if (bucketItem.category != "없음") {
            binding.categoryText.text = bucketItem.category
            binding.categoryText.setEnableTextColor()
            binding.categoryImg.setImage(R.drawable.category_enable)
        }


        if (bucketItem.dDate != null && !bucketItem.dDate.equals("0")) {
            binding.ddayText.text = bucketItem.dDate

            val dday = bucketItem.dDate?.split("/")

            val date = Date()
            date.year = dday?.get(0)?.toInt()?.minus(1900) ?: 0
            date.month = dday?.get(1)?.toInt()?.minus(1) ?: 0
            date.date = dday?.get(2)?.toInt() ?: 0

            currentCalendarDay = date

            binding.ddayText.setEnableTextColor()
            binding.ddayImg.setImage(R.drawable.calendar_enable)
        }

        if (bucketItem.goalCount != null && bucketItem.goalCount.toString() != "1") {
            binding.goalCountText.text = bucketItem.goalCount.toString()
            goalCount = bucketItem.goalCount ?: 0
            binding.goalCountText.setEnableTextColor()
            binding.countImg.setImage(R.drawable.target_count_enable)
        }

        binding.writeRegist.isEnabled = true
    }

    override fun loadArgument() {
        arguments?.let {
            val args = BucketUpdateFragmentArgs.fromBundle(it)
            val bucket = args.bucket
            val id = args.bucketId
            bucketItem = bucket!!
            bucketId = id!!
        }
    }

    override fun bucketAddOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            addBucket()
        }
    }

    private fun addBucket() {
        viewModel.updateBucketList(
                bucketId, getBucketItemInfo(), alreadyImgList, getRealSendImgList(), object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                addBucket()
            }

            override fun start() {
                imm.hideSoftInputFromWindow(binding.titleText.windowToken, 0)
                imm.hideSoftInputFromWindow(binding.memoText.windowToken, 0)
                startLoading()

            }

            override fun success() {
                stopLoading()
                Toast.makeText(context, "버킷이 수정되었습니다", Toast.LENGTH_SHORT).show()
                isCancelConfirm = true
                requireActivity().onBackPressed()
            }

            override fun fail() {
                stopLoading()
                Toast.makeText(context, "버킷이 수정되지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun setImgList(bucketInfo: DetailBucketItem): MutableMap<Int, String?> {
        val imgList = mutableMapOf<Int, String?>()
        imgList[0] = bucketInfo.imgUrl1
        imgList[1] = bucketInfo.imgUrl2
        imgList[2] = bucketInfo.imgUrl3
        return imgList
    }

    override fun setUpCategory(categoryList: ArrayList<Category>) {
        for (i in categoryList) {
            if (i.name == bucketItem.category) {
                selectCategory = i
            }
        }
    }

    override fun moveToAddCategory(v: View): () -> Unit = {
        val directions = BucketUpdateFragmentDirections.actionWriteToCategoryEdit()
        v.findNavController().navigate(directions)
    }

    override fun actionByBackButton() {
        val cancelConfirm: (Boolean) -> Unit = {
            if (it) {
                isCancelConfirm = it
                requireActivity().onBackPressed()
            }
        }
        CancelDialog(cancelConfirm).show(requireActivity().supportFragmentManager, "tag")
    }

    class CancelDialog(private val cancelConfirm: (Boolean) -> Unit) : BaseNormalDialogFragment() {

        init {
            TITLE_MSG = "수정을 취소하시겠습니까?"
            CONTENT_MSG = "수정된 내용이 삭제됩니다."
            CANCEL_BUTTON_VISIBLE = true
            GRADIENT_BUTTON_VISIBLE = false
            CONFIRM_TEXT = "수정 취소"
            CANCEL_TEXT = "계속 작성"
        }

        override fun createOnClickConfirmListener(): View.OnClickListener {
            return View.OnClickListener {
                dismiss()
                cancelConfirm.invoke(true)
            }
        }


        override fun createOnClickCancelListener(): View.OnClickListener {
            return View.OnClickListener {
                dismiss()
                cancelConfirm.invoke(false)
            }
        }
    }

}