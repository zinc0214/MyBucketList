package womenproject.com.mybury.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import womenproject.com.mybury.R
import womenproject.com.mybury.data.DetailBucketItem
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.UseUserIdRequest
import womenproject.com.mybury.databinding.FragmentBucketDetailBinding
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.ui.ShowImgWideFragment

class BucketDetailFragment : Fragment() {

    lateinit var viewDataBinding: FragmentBucketDetailBinding
    lateinit var viewModel: BucketDetailViewModel
    lateinit var bucketItem: DetailBucketItem

    private lateinit var bucketItemId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bucket_detail, container, false)
        viewModel = BucketDetailViewModel()
        initDataBinding()
        return viewDataBinding.root
    }

    private fun initDataBinding() {
        viewDataBinding.lifecycleOwner = this

        arguments?.let {
            val args = BucketDetailFragmentArgs.fromBundle(it)
            val bucketId = args.bucketId
            bucketItemId = bucketId!!
        }

        loadBucketDetailInfo()
        initObserve()
    }

    private fun loadBucketDetailInfo() {
        viewModel.loadBucketDetail(object : BaseViewModel.MoreCallBackAny {
            override fun restart() {
                loadBucketDetailInfo()
            }

            override fun start() {
                startLoading()
            }

            override fun success(value: Any) {
                bucketItem = value as DetailBucketItem
                setUpViews()
                stopLoading()
            }

            override fun fail() {
                stopLoading()
            }

        }, bucketItemId)
    }

    private fun setUpViews() {
        viewDataBinding.apply {
            detailInfo = bucketItem
            backClickListener = setOnBackClickListener
            moreClickListener = showMoreMenuLayout
            countMinusClickListener = bucketCancelListener
            countPlusClickListener = bucketCompleteListener
            redoClickListener = bucketRedoListener
            lessCount = (bucketItem.goalCount - bucketItem.userCount).toString()

            val viewPager = viewDataBinding.viewPager
            val imgList = setImgList(bucketItem)
            val showWideListener: (String) -> Unit = { showImgWide(it) }
            val viewPagerAdapter = BucketDetailImageViewPageAdapter(requireContext(), imgList, showWideListener)
            viewPager.adapter = viewPagerAdapter
            viewDataBinding.tabLayout.setupWithViewPager(viewPager)

            isCategoryShow = bucketItem.category != "없음"
            val isShowCountLayout = bucketItem.goalCount > 1 && bucketItem.goalCount > bucketItem.userCount
            val isCompleted = bucketItem.goalCount <= bucketItem.userCount
            isCount = isShowCountLayout
            isDone = isCompleted
            isShowComment = bucketItem.imgUrl1 == null && bucketItem.imgUrl2 == null && bucketItem.imgUrl3 == null && !isShowCountLayout && !isCompleted && bucketItem.memo.isBlank()


            when (imgList.size) {
                0 -> {
                    viewDataBinding.tabLayout.visibility = View.GONE
                    viewDataBinding.imageLayout.visibility = View.GONE
                }
                1 -> {
                    viewDataBinding.tabLayout.visibility = View.GONE
                }
                else -> {
                    viewDataBinding.tabLayout.visibility = View.VISIBLE
                    viewDataBinding.imageLayout.visibility = View.VISIBLE
                }
            }

            executePendingBindings()

        }

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

    private fun showImgWide(url: String) {
        val showImgWideFragment = ShowImgWideFragment(url)
        showImgWideFragment.show(requireActivity().supportFragmentManager, "tag")
    }

    private val setOnBackClickListener = View.OnClickListener {
        requireActivity().onBackPressed()
    }

    private val showMoreMenuLayout = View.OnClickListener {
        if (viewDataBinding.detailMoreLayout.visibility == View.GONE) {
            viewDataBinding.detailMoreLayout.visibility = View.VISIBLE
        } else {
            viewDataBinding.detailMoreLayout.visibility = View.GONE
        }

        viewDataBinding.detailMoreMenu.updateOnClickListener = createOnClickBucketUpdateListener
        viewDataBinding.detailMoreMenu.deleteOnClickListener = deleteBucketListener
    }

    private val createOnClickBucketUpdateListener = View.OnClickListener {
        val directions = BucketDetailFragmentDirections.actionDetailToUpdate()
        directions.bucket = bucketItem
        directions.bucketId = bucketItemId
        this.findNavController().navigate(directions)
        viewDataBinding.detailMoreLayout.visibility = View.GONE
    }

    private val deleteBucketListener = View.OnClickListener {
        val deleteYes: () -> Unit = {
            deleteBucket()
        }
        DeleteBucketDialog(deleteYes).show(requireActivity().supportFragmentManager)
    }

    private fun deleteBucket() {
        val userId = UseUserIdRequest(Preference.getUserId(requireContext()))
        viewModel.deleteBucketListener(userId, bucketItemId)
        viewDataBinding.detailMoreLayout.visibility = View.GONE
    }

    private val bucketCompleteListener = View.OnClickListener {
        bucketComplete()
    }

    private val bucketRedoListener = View.OnClickListener {
        viewModel.redoBucketList(bucketItemId)
    }

    private fun initObserve() {
        viewModel.isDeleteSuccess.observe(viewLifecycleOwner, isDeleteSuccessObserver)
        viewModel.showLoading.observe(viewLifecycleOwner, isShowLoading)
        viewModel.isReDoSuccess.observe(viewLifecycleOwner, isRedoSuccessObserver)
    }

    private val isDeleteSuccessObserver = Observer<Boolean> {
        if(it==true) {
            stopLoading()
            Toast.makeText(requireContext(), "버킷리스트가 삭제 되었습니다.", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
        } else {
            stopLoading()
            Toast.makeText(requireContext(), "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private val isRedoSuccessObserver = Observer<Boolean> {
        if(it==true) {
            stopLoading()
            loadBucketDetailInfo()
        } else {
            stopLoading()
            Toast.makeText(requireContext(), "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private val isShowLoading = Observer<Boolean> {
        if(it==true) {
            startLoading()
        } else {
            stopLoading()
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

    private val bucketCancelListener = View.OnClickListener {
        bucketCancel()
    }


    private fun bucketCancel() {
        viewModel.setBucketCancel(object : BaseViewModel.Simple3CallBack {
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


    class DeleteBucketDialog(private val deleteYes: () -> Unit) : BaseNormalDialogFragment() {

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

    fun startLoading() {
        if (activity is MainActivity) {
            val a = activity as MainActivity
            a.startLoading()
        }
    }

    fun stopLoading() {
        if (activity is MainActivity) {
            val a = activity as MainActivity
            a.stopLoading()
        }
    }
}