package womenproject.com.mybury.presentation.detail

import android.animation.Animator
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.BuildConfig
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference.Companion.isAlreadyBucketRetryGuideShow
import womenproject.com.mybury.data.Preference.Companion.setAlreadyBucketRetryGuideShow
import womenproject.com.mybury.data.model.BucketDetailItem
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.databinding.FragmentBucketDetailBinding
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.dialog.LoadFailDialog
import womenproject.com.mybury.presentation.viewmodels.BucketListViewModel
import womenproject.com.mybury.ui.ShowImgWideFragment
import womenproject.com.mybury.util.Converter.Companion.dpToPx
import womenproject.com.mybury.util.ScreenUtils.Companion.getScreenWidth
import womenproject.com.mybury.util.observeNonNull
import womenproject.com.mybury.util.showToast
import java.util.Random

@AndroidEntryPoint
class BucketDetailFragment : Fragment() {

    lateinit var viewDataBinding: FragmentBucketDetailBinding
    lateinit var bucketItem: BucketDetailItem

    private lateinit var bucketItemId: String
    private var isLoadForFinalSucceed = false
    private var memoArrowIsClicked = false

    private val bucketDetailViewModel by viewModels<BucketDetailViewModel>()
    private val bucketListViewModel by viewModels<BucketListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bucket_detail, container, false)
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
        setUpObservers()
    }

    private fun loadBucketDetailInfo() {
        bucketDetailViewModel.loadBucketDetail(bucketItemId)
    }

    private fun setUpViews() {
        viewDataBinding.apply {
            view = this@BucketDetailFragment
            detailInfo = bucketItem

            comment = getRandomComment()
            setImageViewPager()

            val desc: String = requireContext().getString(R.string.bucket_least_count)
            viewDataBinding.currentStateTextView.text = Html.fromHtml(
                String.format(
                    desc,
                    (bucketItem.goalCount - bucketItem.userCount).toString()
                ),
                FROM_HTML_MODE_LEGACY
            )

            contentView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                if (scrollY > 10 && bucketItem.isDone().not()) {
                    bottomDivider.visibility = View.VISIBLE
                } else {
                    bottomDivider.visibility = View.GONE
                }
            }

            viewDataBinding.bucketMemo.post {
                val lineCount: Int = viewDataBinding.bucketMemo.lineCount
                viewDataBinding.memoArrow.visibility =
                    if (lineCount >= 2 && !memoArrowIsClicked) View.VISIBLE else View.GONE
            }
        }

    }

    private fun setImageViewPager() {

        viewDataBinding.imageLayout.layoutParams.height = getImageSize()
        viewDataBinding.imageLayout.layoutParams.width = getImageSize()

        val viewPager = viewDataBinding.viewPager
        val imgList = setImgList(bucketItem)
        val showWideListener: (String) -> Unit = { showImgWide(it) }
        val viewPagerAdapter =
            BucketDetailImageViewPageAdapter(requireContext(), imgList, showWideListener)
        viewPager.adapter = viewPagerAdapter
        viewDataBinding.tabLayout.setupWithViewPager(viewPager)

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
    }

    private fun setImgList(bucketInfo: BucketDetailItem): ArrayList<String> {
        val imgList = arrayListOf<String>()
        if (!bucketInfo.imgUrl1.isNullOrBlank()) {
            imgList.add(bucketInfo.imgUrl1!!)
        }
        if (!bucketInfo.imgUrl2.isNullOrBlank()) {
            imgList.add(bucketInfo.imgUrl2!!)
        }
        if (!bucketInfo.imgUrl3.isNullOrBlank()) {
            imgList.add(bucketInfo.imgUrl3!!)
        }
        return imgList
    }

    private fun showImgWide(url: String) {
        val showImgWideFragment = ShowImgWideFragment(url)
        showImgWideFragment.show(requireActivity().supportFragmentManager, "tag")
    }

    private fun setUpObservers() {
        bucketDetailViewModel.loadBucketDetail.observeNonNull(viewLifecycleOwner) {
            bucketItem = it
            setUpViews()
        }

        bucketDetailViewModel.loadBucketState.observeNonNull(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> {
                    startLoading()
                }
                LoadState.SUCCESS -> {
                    stopLoading()
                }
                LoadState.FAIL -> {
                    stopLoading()
                    LoadFailDialog {
                        requireActivity().onBackPressed()
                    }.show(parentFragmentManager)
                }
                LoadState.RESTART -> {
                    loadBucketDetailInfo()
                }
            }
        }

        bucketDetailViewModel.isDeleteSuccess.observe(viewLifecycleOwner) {
            stopLoading()
            if (it == true) {
                requireContext().showToast("버킷리스트가 삭제 되었습니다.")
                requireActivity().onBackPressed()
            } else {
                requireContext().showToast("다시 시도해주세요.")
            }
        }

        bucketDetailViewModel.showLoading.observe(viewLifecycleOwner) {
            if (it == true) {
                startLoading()
            } else {
                stopLoading()
            }
        }

        bucketDetailViewModel.isReDoSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                stopLoading()
                loadBucketDetailInfo()
            } else {
                stopLoading()
                requireContext().showToast("다시 시도해주세요.")
            }
        }

        bucketListViewModel.bucketCancelLoadState.observeNonNull(
            viewLifecycleOwner
        ) {
            when (it) {
                LoadState.FAIL -> {
                    stopLoading()
                    requireContext().showToast("다시 시도해주세요.")
                }
                LoadState.RESTART -> {
                    bucketListViewModel.bucketCancel(bucketItemId)
                }
                LoadState.START -> {
                    startLoading()
                }
                LoadState.SUCCESS -> {
                    stopLoading()
                    loadBucketDetailInfo()
                }
            }
        }

        bucketDetailViewModel.completeBucketState.observeNonNull(viewLifecycleOwner) {
            when (it) {
                LoadState.FAIL -> {
                    requireContext().showToast("다시 시도해주세요.")
                }
                LoadState.RESTART -> {
                    bucketComplete()
                }
                LoadState.START -> {

                }
                LoadState.SUCCESS -> {
                    loadBucketDetailInfo()
                }
            }
        }
    }

    fun goToBack() {
        requireActivity().onBackPressed()
    }

    fun showMoreMenu() {
        viewDataBinding.detailMoreLayout.apply {
            visibility = if (visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    fun goToBucketUpdate() {
        val directions = BucketDetailFragmentDirections.actionDetailToUpdate()
        directions.bucket = bucketItem
        directions.bucketId = bucketItemId
        this.findNavController().navigate(directions)
        viewDataBinding.detailMoreLayout.visibility = View.GONE
    }

    fun showDeleteBucketDialog() {
        DeleteBucketDialog {
            goToDeleteBucket()
        }.show(parentFragmentManager)
    }

    fun showRedoBucketDialog() {
        RedoBucketDialog {
            bucketDetailViewModel.redoBucketList(bucketItemId)
        }.show(parentFragmentManager)
    }

    fun showMoreMemoContext() {
        viewDataBinding.bucketMemo.maxLines = Int.MAX_VALUE
        viewDataBinding.memoArrow.visibility = View.GONE
        memoArrowIsClicked = true
    }

    fun bucketCountToMinus() {
        bucketListViewModel.bucketCancel(bucketItemId)
    }

    fun bucketCountToPlus() {
        bucketComplete()
    }


    private fun goToDeleteBucket() {
        bucketDetailViewModel.deleteBucketListener(bucketItemId)
        viewDataBinding.detailMoreLayout.visibility = View.GONE
    }


    private fun bucketComplete() {

        (bucketItem.goalCount == 1 || bucketItem.goalCount - 1 == bucketItem.userCount).also {
            isLoadForFinalSucceed = it
        }

        if (isLoadForFinalSucceed) {
            bucketFinalSucceedAction()
        }

        bucketDetailViewModel.setBucketComplete(bucketItemId)

    }

    private fun bucketFinalSucceedAction() {
        viewDataBinding.successLottieView.visibility = View.VISIBLE
        viewDataBinding.successLottieView.playAnimation()

        viewDataBinding.successLottieView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {
                // do nothing
            }

            override fun onAnimationEnd(animation: Animator) {
                viewDataBinding.successLottieView.visibility = View.GONE
                if (isAlreadyBucketRetryGuideShow(requireContext()) || BuildConfig.DEBUG) {
                    BucketRetryGuideDialogFragment().show(
                        requireActivity().supportFragmentManager,
                        "tag"
                    )
                    setAlreadyBucketRetryGuideShow(requireContext(), true)
                }
            }

            override fun onAnimationCancel(animation: Animator) {
                // do nothing
            }

            override fun onAnimationStart(animation: Animator) {
                // do nothing
            }
        })

    }


    private fun getRandomComment(): String {

        val commentList = listOf(
            "하나씩 목표를 달성해볼까요?",
            "시작이 반이라는 말이 있죠. 아자아자!",
            "새로운 일을 시작하는 용기가 깃들기를",
            "천 리 길도 한걸음부터",
            "우리만의 페이스로 달성해봐요",
            "당신은 해낼 수 있을 거에요",
            "이제 도전을 시작해보는 건 어떨까요?",
            "인생은 게으름과 자기자신의 싸움이래요",
            "당신의 버킷리스트는 이제 이루어졌나요?",
            "포기하고 싶을 순간에도 응원하고 있어요",
            "잘못되는 것을 두려워말고 도전해봐요!"
        )

        val random = Random()
        val randomNum = random.nextInt(commentList.size)

        return commentList[randomNum]

    }

    private fun startLoading() {
        viewDataBinding.detailMoreLayout.visibility = View.GONE
        if (activity is MainActivity) {
            val a = activity as MainActivity
            a.startLoading()
        }
    }

    private fun stopLoading() {
        if (activity is MainActivity) {
            val a = activity as MainActivity
            a.stopLoading()
        }
    }


    private fun getImageSize() = getScreenWidth(requireContext()) - dpToPx(60)

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

    class RedoBucketDialog(private val confirmAction: () -> Unit) : BaseNormalDialogFragment() {

        init {
            TITLE_MSG = "다시 도전하기"
            CONTENT_MSG = "달성 횟수가 초기화 됩니다"
            CANCEL_BUTTON_VISIBLE = true
            GRADIENT_BUTTON_VISIBLE = false
            CONFIRM_TEXT = "확인"
            CANCEL_TEXT = "취소"
            CANCEL_ABLE = false
        }

        override fun createOnClickConfirmListener(): View.OnClickListener {
            return View.OnClickListener {
                confirmAction()
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