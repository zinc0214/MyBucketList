package womenproject.com.mybury.presentation.detail

import android.util.Log
import android.view.View
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.detail_image_adapter.view.*
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.databinding.FragmentBucketDetailBinding
import womenproject.com.mybury.presentation.base.BaseFragment


/**
 * Created by HanAYeon on 2018. 11. 30..
 */

class BucketDetailFragment : BaseFragment<FragmentBucketDetailBinding, BucketDetailViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_bucket_detail
    override val viewModel: BucketDetailViewModel
        get() = BucketDetailViewModel()

    lateinit var bucketItem: BucketItem

    override fun initDataBinding() {
        viewDataBinding.viewModel = viewModel
        viewDataBinding.lifecycleOwner = this

        arguments?.let {
            val args = BucketDetailFragmentArgs.fromBundle(it)
            val bucket = args.bucket
            bucketItem = bucket!!
        }

        viewDataBinding.titleText.text = bucketItem.title
        viewDataBinding.lockText.text = if (bucketItem.open) "공개" else "비공개"
        viewDataBinding.categoryText.text = bucketItem.category.name
        viewDataBinding.dday.text = bucketItem.dDay.toString()
        viewDataBinding.currentCount.text = "${bucketItem.userCount}/${bucketItem.goalCount}"
        viewDataBinding.completeText.text = "${bucketItem.userCount}회 완료"

        viewDataBinding.bucketUpdateOnClickListener = createOnClickBucketUpdateListener()


        val uri1 = "https://image.shutterstock.com/image-photo/assortment-fine-chocolates-white-dark-260nw-123360676.jpg"
        val uri2 = "https://image.shutterstock.com/image-vector/doodle-cake-happy-birthday-vector-600w-1040176828.jpg"
        val uri3 = "https://image.shutterstock.com/image-photo/road-trip-sign-background-260nw-269296442.jpg"

        val list = ArrayList<String>()
        list.add(uri1)
        list.add(uri2)
        list.add(uri3)

        val viewPager = viewDataBinding.moreImage.viewPager
        val viewPagerAdapter = BucketDatailImageViewPageAdapter(this.context!!, list)

        viewPager.adapter = viewPagerAdapter

        viewDataBinding.tabLayout.setupWithViewPager(viewPager)
    }


    public fun createOnClickBucketUpdateListener() : View.OnClickListener {
        return View.OnClickListener {
            Log.e("ayham", "gpgin")
            val directions = BucketDetailFragmentDirections.actionDetailToUpdate()

            Log.e("ayhan", " Bucket??? ${bucketItem.title}" )
            directions.bucket = bucketItem
            this.findNavController().navigate(directions)
        }

    }


}