package womenproject.com.mybury.presentation.update

import android.util.Log
import android.view.View
import android.widget.Toast
import womenproject.com.mybury.R
import womenproject.com.mybury.data.DetailBucketItem
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.presentation.write.BucketWriteFragment
import womenproject.com.mybury.presentation.write.BucketWriteViewModel

class BucketUpdateFragment : BucketWriteFragment() {

    lateinit var bucketItem: DetailBucketItem
    lateinit var bucketId: String


    override fun initForUpdate() {

        arguments?.let {
            val args = BucketUpdateFragmentArgs.fromBundle(it)
            val bucket = args.bucket
            val id = args.bucketId
            bucketItem = bucket!!
            bucketId = id!!
        }

        viewModel.bucketItem = bucketItem
        alreadyImgList = setImgList(bucketItem)



        Log.e("ayhan", "bucket...? , ${bucketItem.title}")
        viewDataBinding.titleText.setText(bucketItem.title)

        if (bucketItem.memo.isNotEmpty()) {
            viewDataBinding.memoLayout.visibility = View.VISIBLE
            viewDataBinding.memoText.setText(bucketItem.memo)
        }

        if (bucketItem.open) {
            viewDataBinding.openSwitchBtn.transitionToStart()
        } else {
            viewDataBinding.openSwitchBtn.transitionToEnd()
            viewDataBinding.openText.text = context!!.resources.getString(R.string.bucket_close)
            viewDataBinding.openImg.background = context!!.getDrawable(R.drawable.open_disable)
        }



        if (!bucketItem.category.equals("없음")) {
            viewDataBinding.categoryText.text = bucketItem.category
            viewDataBinding.categoryText.setEnableTextColor()
            viewDataBinding.categoryImg.setImage(R.drawable.category_enable)
        }


        if (bucketItem.dDate != null && !bucketItem.dDate.equals("0")) {
            viewDataBinding.ddayText.text = bucketItem.dDate.toString()
            viewDataBinding.ddayText.setEnableTextColor()
            viewDataBinding.ddayImg.setImage(R.drawable.calendar_enable)
        }

        if (bucketItem.goalCount != null && !bucketItem.goalCount.equals("1")) {
            viewDataBinding.goalCountText.text = bucketItem.goalCount.toString()
            viewDataBinding.goalCountText.setEnableTextColor()
            viewDataBinding.countImg.setImage(R.drawable.target_count_enable)
        }

        viewDataBinding.writeRegist.isEnabled = true
    }


    override fun bucketAddOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            viewModel.updateBucketList(
                    Preference.getAccessToken(context!!), Preference.getUserId(context!!), bucketId,
                    getBucketItemInfo(), imgList, object : BucketWriteViewModel.OnBucketAddEvent {
                override fun start() {
                    startLoading()

                }

                override fun success() {
                    stopLoading()
                    Toast.makeText(context, "버킷이 수정되었습니다", Toast.LENGTH_SHORT).show()
                    activity!!.onBackPressed()
                }

                override fun fail() {
                    stopLoading()
                    Toast.makeText(context, "버킷이 수정되지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    private fun setImgList(bucketInfo: DetailBucketItem): ArrayList<String> {
        val imgList = arrayListOf<String>()
        if (!bucketInfo.imgUrl1.isNullOrBlank()) {
            Log.e("ayhan", "1 is use")
            imgList.add(bucketInfo.imgUrl1)
        }
        if (!bucketInfo.imgUrl2.isNullOrBlank()) {
            Log.e("ayhan", "2 is use")
            imgList.add(bucketInfo.imgUrl2)
        }
        if (!bucketInfo.imgUrl3.isNullOrBlank()) {
            Log.e("ayhan", "3 is use")
            imgList.add(bucketInfo.imgUrl3)
        }

        return imgList
    }


}