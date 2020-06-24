package womenproject.com.mybury.presentation.mypage

import android.view.View
import androidx.navigation.findNavController
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.presentation.main.bucketlist.MainBucketListAdapter


class CategoryBucketListAdapter(bucketList: List<BucketItem>,
                                private val showSnackBar: ((BucketItem) -> Unit)) : MainBucketListAdapter(bucketList, showSnackBar) {

    override fun createOnClickBucketListener(bucket: BucketItem): View.OnClickListener {

        return View.OnClickListener {
            val directions = BucketListByCategoryFragmentDirections.actionCategoryBucketListToDetail()
            directions.bucketId = bucket.id
            it.findNavController().navigate(directions)

        }
    }
}