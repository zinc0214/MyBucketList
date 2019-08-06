package womenproject.com.mybury.view

import kotlinx.android.synthetic.main.detail_image_adapter.view.*
import womenproject.com.mybury.R
import womenproject.com.mybury.base.BaseFragment
import womenproject.com.mybury.databinding.FragmentBucketDetailBinding
import womenproject.com.mybury.adapter.ImageViewPageAdapter
import womenproject.com.mybury.viewmodels.BucketDetailViewModel


/**
 * Created by HanAYeon on 2018. 11. 30..
 */

class BucketDetailFragment : BaseFragment<FragmentBucketDetailBinding, BucketDetailViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_bucket_detail
    override val viewModel: BucketDetailViewModel
        get() = BucketDetailViewModel()

    override fun initStartView() {

        viewDataBinding.viewModel = viewModel

        val uri1 = "https://image.shutterstock.com/image-photo/assortment-fine-chocolates-white-dark-260nw-123360676.jpg"
        val uri2 = "https://image.shutterstock.com/image-vector/doodle-cake-happy-birthday-vector-600w-1040176828.jpg"
        val uri3 = "https://image.shutterstock.com/image-photo/road-trip-sign-background-260nw-269296442.jpg"

        val list = ArrayList<String>()
        list.add(uri1)
        list.add(uri2)
        list.add(uri3)

        val viewPager = viewDataBinding.moreImage.viewPager
        val viewPagerAdapter = ImageViewPageAdapter(this.context!!, list)

        viewPager.adapter = viewPagerAdapter

        viewDataBinding.tabLayout.setupWithViewPager(viewPager)



    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }


}