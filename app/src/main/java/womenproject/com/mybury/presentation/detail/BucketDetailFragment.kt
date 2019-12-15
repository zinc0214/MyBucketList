package womenproject.com.mybury.presentation.detail

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.detail_image_adapter.view.*
import womenproject.com.mybury.R
import womenproject.com.mybury.data.DetailBucketItem
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
import womenproject.com.mybury.databinding.FragmentBucketDetailBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.ui.ShowImgWideFragment


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
        viewDataBinding.memo.text = bucketInfo.memo
        viewDataBinding.lockText.text = if (bucketInfo.open) "공개" else "비공개"
        viewDataBinding.categoryText.text = bucketInfo.category

        if(bucketInfo.dDate.isNullOrEmpty()) {
            viewDataBinding.ddayLayout.visibility = View.GONE
        } else {
            viewDataBinding.dday.text = bucketInfo.dDate
        }

        viewDataBinding.currentCount.text = "${bucketInfo.userCount}/${bucketInfo.goalCount}"
        viewDataBinding.completeText.text = "${bucketInfo.userCount}회 완료"

        viewDataBinding.bucketUpdateOnClickListener = createOnClickBucketUpdateListener(bucketInfo)
        viewDataBinding.bucketCompleteListener = bucketCompleteListener()

        val viewPager = viewDataBinding.moreImage.viewPager
        val imgList = setImgList(bucketInfo)
        val showWideListener: (String) -> Unit = { showImgWide(it) }
        val viewPagerAdapter = BucketDetailImageViewPageAdapter(this.context!!, imgList, showWideListener)
        viewPager.adapter = viewPagerAdapter
        viewDataBinding.tabLayout.setupWithViewPager(viewPager)

        if (imgList.size == 0) {
            viewDataBinding.moreImage.layout.visibility = View.GONE
            viewDataBinding.oneImage.layout.visibility = View.VISIBLE
            viewDataBinding.oneImage.backgroundImage
                    .setImageDrawable(resources.getDrawable(R.drawable.gradient_background))
        } else if (imgList.size == 1) {
            viewDataBinding.moreImage.layout.visibility = View.GONE
            viewDataBinding.oneImage.layout.visibility = View.VISIBLE
            Glide.with(context!!).load(imgList[0]).centerCrop().placeholder(
                    R.drawable.gradient_background).into(viewDataBinding.oneImage.backgroundImage)
            viewDataBinding.oneImage.backgroundImage.setOnClickListener { v -> showImgWide(imgList[0]) }
        } else {
            viewDataBinding.oneImage.layout.visibility = View.GONE
            viewDataBinding.moreImage.layout.visibility = View.VISIBLE
        }

        if (bucketInfo.userCount >= bucketInfo.goalCount) {
            Log.e("ayhan", "foggo")
            viewDataBinding.completeLayout.visibility = View.VISIBLE
            viewDataBinding.completeText.text = "바킷리스트 달성 완료를 축하해"
            viewDataBinding.completeText.isEnabled = false
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

    private fun createOnClickBucketUpdateListener(bucketItem: DetailBucketItem): View.OnClickListener {
        return View.OnClickListener {
            Log.e("ayham", "gpgin")
            val directions = BucketDetailFragmentDirections.actionDetailToUpdate()
            directions.bucket = bucketItem
            directions.bucketId = bucketItemId
            this.findNavController().navigate(directions)
        }
    }

    private fun bucketCompleteListener(): View.OnClickListener {
        return View.OnClickListener {
            viewModel.setBucketComplete(object : BucketDetailViewModel.OnBucketCompleteEventListener {
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

    fun showImgWide(url: String) {
        val showImgWideFragment = ShowImgWideFragment(url)
        showImgWideFragment.show(activity!!.supportFragmentManager, "tag")
    }

}