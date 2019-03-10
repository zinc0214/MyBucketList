package womenproject.com.mybury.view

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import womenproject.com.mybury.R
import womenproject.com.mybury.base.BaseFragment
import womenproject.com.mybury.databinding.FragmentBucketWriteBinding
import womenproject.com.mybury.viewmodels.BucketWriteViewModel
import android.content.Context.LAYOUT_INFLATER_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import womenproject.com.mybury.ui.WriteImgLayout


class BucketWriteFragment : BaseFragment<FragmentBucketWriteBinding, BucketWriteViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_bucket_write

    override val viewModel: BucketWriteViewModel
        get() = BucketWriteViewModel()

    override fun initStartView() {
        viewDataBinding.memoImgAddListener = memoImgAddOnClickListener()
        viewDataBinding.memoRemoveListener = memoRemoveListener()
        viewDataBinding.ddayAddListener = ddayAddListener()

        viewDataBinding.memoText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                viewDataBinding.memoRemoveImg.visibility = View.INVISIBLE
            } else {
                viewDataBinding.memoRemoveImg.visibility = View.VISIBLE
            }
        }

        viewDataBinding.openSwitchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewDataBinding.openText.text = context!!.resources.getString(R.string.bucket_open)
            } else {
                viewDataBinding.openText.text = context!!.resources.getString(R.string.bucket_close)
            }
        }


    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }

    private fun memoImgAddOnClickListener(): View.OnClickListener {

        val memoAddListener: () -> Unit = {
            viewDataBinding.memoLayout.visibility = View.VISIBLE
        }

        val checkAddImgAbleListener: () -> Boolean = {
            if (viewDataBinding.imgLayout.childCount > 3) {
                Toast.makeText(context, "더 이상 이미지를 추가하실 수 없습니다.", Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        }

        val imgAddListener: (Uri) -> Unit = {
            onAddField(it)
        }

        return View.OnClickListener {
            val filterDialogFragment = WriteMemoImgAddDialogFragment.instance(memoAddListener, checkAddImgAbleListener, imgAddListener)
            filterDialogFragment.show(activity!!.supportFragmentManager, "tag")
        }
    }

    lateinit var imgLayout : WriteImgLayout

    fun onAddField(uri: Uri) {

        val removeImgListener : (Int) -> Unit = { it ->
            onDelete(viewDataBinding.imgLayout.getChildAt(it))
        }

        imgLayout = WriteImgLayout(viewDataBinding.imgLayout.childCount+1, this.context!!,removeImgListener)
        imgLayout.setUI(uri)
        viewDataBinding.imgLayout.addView(imgLayout)
    }


    fun onDelete(v: View) {
        viewDataBinding.imgLayout.removeView(v.parent as View)
    }

    private fun memoRemoveListener(): View.OnClickListener {

        return View.OnClickListener {
            viewDataBinding.memoLayout.visibility = View.GONE
        }
    }

    private fun ddayAddListener(): View.OnClickListener {

        val ddayAddListener: (String) -> Unit = { dday ->
            viewDataBinding.ddayText.text = dday
            viewDataBinding.ddayText.setTextColor(context!!.resources.getColor(R.color.mainColor))
        }

        return View.OnClickListener {
            val calendarDialogFragment = CalendarDialogFragment.instance(ddayAddListener)
            calendarDialogFragment.show(activity!!.supportFragmentManager, "tag")
        }
    }

    fun hideKeyboardFrom(view: View) {
        val imm = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}