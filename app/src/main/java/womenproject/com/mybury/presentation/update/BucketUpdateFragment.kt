package womenproject.com.mybury.presentation.update

import android.util.Log
import android.view.View
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.presentation.detail.BucketDetailFragmentArgs
import womenproject.com.mybury.presentation.write.BucketWriteFragment

class BucketUpdateFragment : BucketWriteFragment() {

    lateinit var bucketItem: BucketItem

    override fun initForUpdate() {

        arguments?.let {
            val args = BucketDetailFragmentArgs.fromBundle(it)
            val bucket = args.bucket
            bucketItem = bucket!!
        }

        viewModel.bucketItem = bucketItem

        Log.e("ayhan", "bucket...? , ${bucketItem.dDay}")
        viewDataBinding.titleText.setText(bucketItem.title)

        if(bucketItem.memo.isNotEmpty()) {
            viewDataBinding.memoLayout.visibility = View.VISIBLE
            viewDataBinding.memoText.setText(bucketItem.memo)
        }

        if(bucketItem.open) {
            viewDataBinding.openSwitchBtn.transitionToStart()
        } else {
            viewDataBinding.openSwitchBtn.transitionToEnd()
            viewDataBinding.openText.text = context!!.resources.getString(R.string.bucket_close)
            viewDataBinding.openImg.background = context!!.getDrawable(R.drawable.open_disable)
        }



        if(!bucketItem.category.name.equals("없음")) {
            viewDataBinding.categoryText.text = bucketItem.category.name
            viewDataBinding.categoryText.setEnableTextColor()
            viewDataBinding.categoryImg.setImage(R.drawable.category_enable)
        }


        if(bucketItem.dDay != null && !bucketItem.dDay.equals("0")) {
            viewDataBinding.ddayText.text = bucketItem.dDay.toString()
            viewDataBinding.ddayText.setEnableTextColor()
            viewDataBinding.ddayImg.setImage(R.drawable.calendar_enable)
        }

        if(bucketItem.goalCount != null && !bucketItem.goalCount.equals("1")) {
            viewDataBinding.goalCountText.text = bucketItem.goalCount.toString()
            viewDataBinding.goalCountText.setEnableTextColor()
            viewDataBinding.countImg.setImage(R.drawable.target_count_enable)
        }





    }


}