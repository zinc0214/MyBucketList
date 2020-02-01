package womenproject.com.mybury.presentation.detail

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import womenproject.com.mybury.R
import womenproject.com.mybury.data.DetailBucketItem
import womenproject.com.mybury.data.Preference.Companion.getUserId
import womenproject.com.mybury.data.UseUserIdRequest
import womenproject.com.mybury.databinding.FragmentBucketDetailBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
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
        viewModel.loadBucketDetail(object : BaseViewModel.MoreCallBackAny {
            override fun restart() {
                loadBucketDetailInfo()
            }

            override fun start() {
                startLoading()
            }

            override fun success(detailBucketItem: Any) {
                stopLoading()
                setUpViews(detailBucketItem as DetailBucketItem)
                bucketItem = detailBucketItem as DetailBucketItem
            }

            override fun fail() {
                stopLoading()
            }

        },  bucketItemId)
    }

    private fun setUpViews(bucketInfo: DetailBucketItem) {

        viewDataBinding.titleText.text = bucketInfo.title
        viewDataBinding.memo.text = bucketInfo.memo
        viewDataBinding.lockText.text = if (bucketInfo.open) "공개" else "비공개"
        viewDataBinding.categoryText.text = bucketInfo.category

        if (bucketInfo.dDate.isNullOrEmpty()) {
            viewDataBinding.ddayLayout.visibility = View.GONE
        } else {
            if(bucketInfo.dDay < 0) {
                val dday = bucketInfo.dDay.toString().replace("-","")
                viewDataBinding.dday.text = "${bucketInfo.dDate}(D+${dday})"
            } else {
                viewDataBinding.dday.text = "${bucketInfo.dDate}(D-${bucketInfo.dDay})"
            }
        }

        viewDataBinding.currentCount.text = "${bucketInfo.userCount}/${bucketInfo.goalCount}"
        viewDataBinding.completeText.text = "${bucketInfo.userCount}회 완료"

        viewDataBinding.bucketMoreMenu = showMoreMenuLayout(bucketInfo)
        viewDataBinding.bucketCompleteListener = bucketCompleteListener()

        val viewPager = viewDataBinding.moreImage.viewPager
        val imgList = setImgList(bucketInfo)
        val showWideListener: (String) -> Unit = { showImgWide(it) }
        val viewPagerAdapter = BucketDetailImageViewPageAdapter(this.context!!, imgList, showWideListener)
        viewPager.adapter = viewPagerAdapter
        viewDataBinding.tabLayout.setupWithViewPager(viewPager)

        if (imgList.size == 0) {
            viewDataBinding.tabLayout.visibility = View.INVISIBLE
            viewDataBinding.moreImage.layout.visibility = View.GONE
            viewDataBinding.oneImage.layout.visibility = View.VISIBLE
            viewDataBinding.oneImage.backgroundImage
                    .setImageDrawable(resources.getDrawable(R.drawable.gradient_background))
        } else if (imgList.size == 1) {
            viewDataBinding.tabLayout.visibility = View.INVISIBLE
            viewDataBinding.moreImage.layout.visibility = View.GONE
            viewDataBinding.oneImage.layout.visibility = View.VISIBLE
            Glide.with(context!!).load(imgList[0]).centerCrop().placeholder(
                    R.drawable.gradient_background).into(viewDataBinding.oneImage.backgroundImage)
            viewDataBinding.oneImage.backgroundImage.setOnClickListener { v -> showImgWide(imgList[0]) }
        } else {
            viewDataBinding.oneImage.layout.visibility = View.GONE
            viewDataBinding.moreImage.layout.visibility = View.VISIBLE
        }

        if (bucketInfo.userCount >= bucketInfo.goalCount ?:0) {
            viewDataBinding.completeLayout.visibility = View.VISIBLE
            viewDataBinding.completeText.text = "달성 완료! 대단해요"
            viewDataBinding.completeText.isEnabled = false
        }

        viewDataBinding.backButton.setOnClickListener(backBtnOnClickListener())
    }

    private fun setImgList(bucketInfo: DetailBucketItem): ArrayList<String> {
        val imgList = arrayListOf<String>()
        if (!bucketInfo.imgUrl1.isNullOrBlank()) {
            imgList.add(bucketInfo.imgUrl1)
        }
        if (!bucketInfo.imgUrl2.isNullOrBlank()) {
            imgList.add(bucketInfo.imgUrl2)
        }
        if (!bucketInfo.imgUrl3.isNullOrBlank()) {
            imgList.add(bucketInfo.imgUrl3)
        }
        return imgList
    }

    private fun showMoreMenuLayout(bucketItem: DetailBucketItem) = View.OnClickListener {
        if (viewDataBinding.detailMoreLayout.visibility == View.GONE) {
            viewDataBinding.detailMoreLayout.visibility = View.VISIBLE
        } else {
            viewDataBinding.detailMoreLayout.visibility = View.GONE
        }

        viewDataBinding.detailMoreMenu.updateOnClickListener = createOnClickBucketUpdateListener(bucketItem)
        viewDataBinding.detailMoreMenu.deleteOnClickListener = deleteBucketListener()
    }

    private fun createOnClickBucketUpdateListener(bucketItem: DetailBucketItem): View.OnClickListener {
        return View.OnClickListener {
            Log.e("ayham", "gpgin")
            val directions = BucketDetailFragmentDirections.actionDetailToUpdate()
            directions.bucket = bucketItem
            directions.bucketId = bucketItemId
            this.findNavController().navigate(directions)
            viewDataBinding.detailMoreLayout.visibility = View.GONE
        }
    }

    private fun deleteBucketListener(): View.OnClickListener {
        return View.OnClickListener {
            val deleteYes: () -> Unit = {
                deleteBucket()
            }
            DeleteBucketDialog(deleteYes).show(activity!!.supportFragmentManager)
        }
    }

    private fun deleteBucket() {
        Log.e("ayham", "gpgin")

        val userId = UseUserIdRequest(getUserId(context!!))
        viewModel.deleteBucketListener(object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                deleteBucket()
            }

            override fun start() {
                startLoading()
            }

            override fun success() {
                stopLoading()
                Toast.makeText(context!!, "버킷리스트가 삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                activity!!.onBackPressed()
            }

            override fun fail() {
                stopLoading()
                Toast.makeText(context!!, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }, userId, bucketItemId)
        viewDataBinding.detailMoreLayout.visibility = View.GONE
    }

    private fun bucketCompleteListener(): View.OnClickListener {
        return View.OnClickListener {
            bucketComplete()
        }
    }


    private fun bucketComplete() {
        viewModel.setBucketComplete(object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                bucketComplete()
            }

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

        }, bucketItemId)

    }

    fun showImgWide(url: String) {
        val showImgWideFragment = ShowImgWideFragment(url)
        showImgWideFragment.show(activity!!.supportFragmentManager, "tag")
    }

    class DeleteBucketDialog(val deleteYes: () -> Unit) : BaseNormalDialogFragment() {

        init {
            TITLE_MSG = "버킷리스트 삭제"
            CONTENT_MSG = "정말 버킷리스트를 삭제하시겠습니까?"
            CANCEL_BUTTON_VISIBLE = true
            GRADIENT_BUTTON_VISIBLE = false
            CONFIRM_TEXT = "삭제"
            CANCEL_TEXT = "취소"
            CANCEL_ABLE = false
        }

        override fun createOnClickConfirmListener(): View.OnClickListener {
            return View.OnClickListener {
                deleteYes.invoke()
                dismiss()
            }
        }

        override fun createOnClickCancelListener(): View.OnClickListener {
            return View.OnClickListener {
                dismiss()
            }
        }

    }
}

