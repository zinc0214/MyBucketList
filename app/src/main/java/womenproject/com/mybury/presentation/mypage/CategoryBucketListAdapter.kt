package womenproject.com.mybury.presentation.mypage

import android.view.View
import androidx.navigation.findNavController
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.presentation.detail.BucketDetailViewModel
import womenproject.com.mybury.presentation.main.bucketlist.MainBucketListAdapter


class CategoryBucketListAdapter(
    viewModel: BucketDetailViewModel,
    showSnackBar: ((BucketItem) -> Unit)
) : MainBucketListAdapter(viewModel, showSnackBar) {

    override fun createOnClickBucketListener(bucket: BucketItem): View.OnClickListener {

        return View.OnClickListener {
            val directions =
                BucketListByCategoryFragmentDirections.actionCategoryBucketListToDetail()
            directions.bucketId = bucket.id
            it.findNavController().navigate(directions)

        }
    }
}