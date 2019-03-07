package womenproject.com.mybury.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import womenproject.com.mybury.R
import womenproject.com.mybury.base.BaseFragment
import womenproject.com.mybury.databinding.FragmentBucketDetailBinding
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

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }


}