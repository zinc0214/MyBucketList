package womenproject.com.mybury.presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.CategoryInfo
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.databinding.FragmentBucketListByCategoryBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.viewmodels.BucketListViewModel
import womenproject.com.mybury.ui.snackbar.MainSnackBarWidget
import womenproject.com.mybury.util.observeNonNull

@AndroidEntryPoint
class BucketListByCategoryFragment : BaseFragment() {

    private lateinit var selectCategory: CategoryInfo

    private lateinit var binding: FragmentBucketListByCategoryBinding

    private val bucketInfoViewModel by viewModels<BucketListViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bucket_list_by_category,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViews()
        setUpObservers()
        getBucketListByCategory()
    }

    private fun setUpViews() {

        val layoutManager = LinearLayoutManager(context)

        binding.bucketList.layoutManager = layoutManager
        binding.bucketList.hasFixedSize()

        arguments?.let {
            val args = BucketListByCategoryFragmentArgs.fromBundle(it)
            val category = args.category
            this.selectCategory = category!!
        }

        binding.headerLayout.title = selectCategory.name
        binding.headerLayout.backBtnOnClickListener = backBtnOnClickListener()
    }

    private fun setUpObservers() {
        bucketInfoViewModel.bucketCancelLoadState.observeNonNull(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> {
                    startLoading()
                }
                LoadState.SUCCESS -> {
                    stopLoading()
                }
                LoadState.FAIL -> {
                    stopLoading()
                }
                else -> {
                    // do Nothing
                }
            }
        }

        bucketInfoViewModel.bucketListLoadState.observeNonNull(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> {
                    startLoading()
                }
                LoadState.SUCCESS -> {
                    stopLoading()
                }
                LoadState.FAIL -> {
                    stopLoading()
                }
                LoadState.RESTART -> {
                    getBucketListByCategory()
                }
            }
        }

        bucketInfoViewModel.categoryBucketList.observeNonNull(viewLifecycleOwner) {
            binding.bucketList.adapter =
                CategoryBucketListAdapter(it.bucketlists, showSnackBar)
        }
    }

    private fun getBucketListByCategory() {
        bucketInfoViewModel.getBucketListByCategory(selectCategory.id)
    }

    private fun bucketCancelListener(info: BucketItem) = View.OnClickListener {
        bucketInfoViewModel.bucketCancel(info.id)
    }

    private val showSnackBar: (BucketItem) -> Unit = { info: BucketItem ->
        showCancelSnackBar(requireView(), info)
    }

    private fun showCancelSnackBar(view: View, info: BucketItem) {
        val countText = if (info.goalCount > 1) "\" ${info.userCount}회 완료" else " \" 완료"
        MainSnackBarWidget.make(view, info.title, countText, bucketCancelListener(info))?.show()
    }
}