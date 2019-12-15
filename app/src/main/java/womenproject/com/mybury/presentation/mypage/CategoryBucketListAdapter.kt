package womenproject.com.mybury.presentation.mypage

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.presentation.main.bucketlist.MainBucketListAdapter
import womenproject.com.mybury.presentation.mypage.BucketListByCategoryFragmentDirections


class CategoryBucketListAdapter(context: Context?, bucketList: List<BucketItem>) : MainBucketListAdapter(context, bucketList) {

    override fun createOnClickBucketListener(bucket: BucketItem): View.OnClickListener {

        return View.OnClickListener {
            Toast.makeText(context, "count : ${bucket.title}", Toast.LENGTH_SHORT).show()
            val directions = BucketListByCategoryFragmentDirections.actionCategoryBucketListToDetail()
            directions.bucketId = bucket.id
            it.findNavController().navigate(directions)

        }
    }


}