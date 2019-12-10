package womenproject.com.mybury.presentation.detail

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.detail_image_adapter.view.*
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.DetailBucketItem
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
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

    lateinit var bucketItemId: String
    lateinit var bucketItem: DetailBucketItem

    override fun initDataBinding() {
        viewDataBinding.viewModel = viewModel
        viewDataBinding.lifecycleOwner = this

        arguments?.let {
            val args = BucketDetailFragmentArgs.fromBundle(it)
            val bucketId = args.bucketId
            bucketItemId = bucketId!!
        }

        loadBucketDetailInfo()

    }


    private fun loadBucketDetailInfo() {
        viewModel.loadBucketDetail(object : BucketDetailViewModel.OnBucketLoadEventListener {
            override fun start() {
                startLoading()
            }

            override fun success(detailBucketItem: DetailBucketItem) {
                stopLoading()
                setUpViews(detailBucketItem)
                bucketItem = detailBucketItem
            }

            override fun fail() {
                stopLoading()
            }

        }, getAccessToken(context!!), bucketItemId)
    }

    private fun setUpViews(bucketInfo: DetailBucketItem) {

        viewDataBinding.titleText.text = bucketInfo.title
        viewDataBinding.lockText.text = if (bucketInfo.open) "공개" else "비공개"
        viewDataBinding.categoryText.text = bucketInfo.category
        viewDataBinding.dday.text = bucketInfo.dDay.toString()
        viewDataBinding.currentCount.text = "${bucketInfo.userCount}/${bucketInfo.goalCount}"
        viewDataBinding.completeText.text = "${bucketInfo.userCount}회 완료"

        viewDataBinding.bucketUpdateOnClickListener = createOnClickBucketUpdateListener(bucketInfo)
        viewDataBinding.bucketCompleteListener = bucketCompleteListener()

        val viewPager = viewDataBinding.moreImage.viewPager
        val viewPagerAdapter = BucketDatailImageViewPageAdapter(this.context!!, bucketInfo.imgList as ArrayList<String>)

        viewPager.adapter = viewPagerAdapter

        viewDataBinding.tabLayout.setupWithViewPager(viewPager)

    }


    private fun createOnClickBucketUpdateListener(bucketItem: DetailBucketItem): View.OnClickListener {
        return View.OnClickListener {
            Log.e("ayham", "gpgin")
            val directions = BucketDetailFragmentDirections.actionDetailToUpdate()
            directions.bucket = bucketItem
            this.findNavController().navigate(directions)
        }

    }

    private fun bucketCompleteListener() : View.OnClickListener {
        return View.OnClickListener {
            viewModel.setBucektComplete(object : BucketDetailViewModel.OnBucketCompleteEventListener {
                override fun start() {
                    startLoading()
                }

                override fun success() {
                    loadBucketDetailInfo()
                }

                override fun fail() {
                    stopLoading()
                    Toast.makeText(context!!, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }

            }, getAccessToken(context!!), bucketItemId)
        }
    }


    override fun setOnBackBtnClickListener(): View.OnClickListener {
        return super.setOnBackBtnClickListener()
    }


}