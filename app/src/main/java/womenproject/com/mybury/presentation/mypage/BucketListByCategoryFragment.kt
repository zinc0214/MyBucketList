package womenproject.com.mybury.presentation.mypage

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.CategoryInfo
import womenproject.com.mybury.databinding.FragmentBucketListByCategoryBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.dialog.NetworkFailDialog
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel
import womenproject.com.mybury.ui.snackbar.MainSnackBarWidget

@AndroidEntryPoint
class BucketListByCategoryFragment : BaseFragment<FragmentBucketListByCategoryBinding>() {

    private lateinit var selectCategory: CategoryInfo

    override val layoutResourceId: Int
        get() = R.layout.fragment_bucket_list_by_category

    private val viewModel by viewModels<BucketInfoViewModel>()

    override fun initDataBinding() {

        arguments?.let {
            val args = BucketListByCategoryFragmentArgs.fromBundle(it)
            val category = args.category
            this.selectCategory = category!!
        }

        binding.headerLayout.title = selectCategory.name
        binding.headerLayout.backBtnOnClickListener = backBtnOnClickListener()

        initBucketListUI()
    }


    private fun initBucketListUI() {
        val layoutManager = LinearLayoutManager(context)

        binding.bucketList.layoutManager = layoutManager
        binding.bucketList.hasFixedSize()

        viewModel.getBucketListByCategory(object : BaseViewModel.MoreCallBackAnyList {
            override fun restart() {
                initBucketListUI()
            }

            override fun fail() {
                stopLoading()
            }

            override fun start() {
                startLoading()
            }

            override fun success(value: List<Any>) {
                binding.bucketList.adapter = CategoryBucketListAdapter(value as List<BucketItem>, showSnackBar)
                stopLoading()
            }
        }, selectCategory.id)

    }


    private fun setBucketCancel(bucketId: String) {
        viewModel.setBucketCancel(object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                setBucketCancel(bucketId)
            }

            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(requireActivity().supportFragmentManager)
            }

            override fun start() {
                startLoading()
            }

            override fun success() {
                initBucketListUI()
            }

        }, bucketId)

    }

    private fun bucketCancelListener(info: BucketItem) = View.OnClickListener {
        setBucketCancel(info.id)
    }

    private val showSnackBar: (BucketItem) -> Unit = { info: BucketItem ->
        showCancelSnackBar(requireView(), info)
    }

    private fun showCancelSnackBar(view: View, info: BucketItem) {
        val countText = if (info.goalCount > 1) "\" ${info.userCount}회 완료" else " \" 완료"
        MainSnackBarWidget.make(view, info.title, countText, bucketCancelListener(info))?.show()
    }


}