package womenproject.com.mybury.view

import android.view.View
import womenproject.com.mybury.R
import womenproject.com.mybury.base.BaseFragment
import womenproject.com.mybury.databinding.FragmentBucketWriteBinding
import womenproject.com.mybury.viewmodels.BucketWriteViewModel

class BucketWriteFragment : BaseFragment<FragmentBucketWriteBinding, BucketWriteViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_bucket_write

    override val viewModel: BucketWriteViewModel
        get() = BucketWriteViewModel()

    override fun initStartView() {
        viewDataBinding.memoImgAddListener = memoImgAddOnClickListener()
        viewDataBinding.memoRemoveListener = memoRemoveListener()


    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }

    private fun memoImgAddOnClickListener(): View.OnClickListener {

        val memoAddListener: () -> Unit = {
            viewDataBinding.memoLayout.visibility = View.VISIBLE
        }

        return View.OnClickListener {
            val filterDialogFragment = WriteMemoImgAddDialogFragment.instance(memoAddListener)
            filterDialogFragment.show(activity!!.supportFragmentManager, "tag")
        }
    }

    private fun memoRemoveListener() : View.OnClickListener {

        return View.OnClickListener {
            viewDataBinding.memoLayout.visibility = View.GONE
        }
    }
}