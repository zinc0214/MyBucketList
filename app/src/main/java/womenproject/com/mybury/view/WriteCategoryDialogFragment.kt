package womenproject.com.mybury.view

import android.annotation.SuppressLint
import android.app.ActionBar
import android.view.View
import womenproject.com.mybury.R
import womenproject.com.mybury.base.BaseDialogFragment
import womenproject.com.mybury.base.BaseFragment
import womenproject.com.mybury.base.BaseNormalDialogFragment
import womenproject.com.mybury.data.BucketUserCategory
import womenproject.com.mybury.databinding.WriteCategoryDialogBinding
import womenproject.com.mybury.ui.WriteItemLayout


@SuppressLint("ValidFragment")
class WriteCategoryDialogFragment(private var userCategory : BucketUserCategory, private var categorySetListener : (String) -> Unit) : BaseDialogFragment<WriteCategoryDialogBinding>() {

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.writeFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    override val layoutResourceId: Int
        get() = R.layout.write_category_dialog

    override fun initStartView() {
        viewDataBinding.addCategory.title = "카테고리 추가"
        viewDataBinding.notUseCategory.title = "없음"

        viewDataBinding.notUseCategory.itemClickListener = notUseCategoryListener()
        viewDataBinding.addCategory.itemClickListener = addCategoryListener()


        for(i in 0 until userCategory.categoryList.size) {
            addCategoryItem(userCategory.categoryList[i])
        }
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }


    private fun notUseCategoryListener() : View.OnClickListener {
        return View.OnClickListener {
            dismiss()
        }
    }

    private fun addCategoryListener() : View.OnClickListener {
        return View.OnClickListener {
            AddCategoryDialog().show(activity!!.supportFragmentManager, "tag")
        }
    }
    private fun addCategoryItem(title : String) {

        val categorySelectListener: (String) -> Unit = { it ->
            categorySetListener.invoke(it)
            dismiss()
        }

        val writeImgLayout = WriteItemLayout(this.context!!, categorySelectListener).setUI(title)
        viewDataBinding.categoryListLayout.addView(writeImgLayout)
    }


    class AddCategoryDialog : BaseNormalDialogFragment() {

        init {
            TITLE_MSG = "알림"
            CONTENT_MSG = "카테고리 추가 화면으로 이동합니다."
            CANCEL_BUTTON_VISIBLE = true
            GRADIENT_BUTTON_VISIBLE = false
            CONFIRM_TEXT = "이동"
            CANCEL_TEXT = "취소"
        }

        override fun createOnClickCancelListener(): View.OnClickListener {
            return View.OnClickListener {
                dismiss()
            }
        }

        override fun createOnClickConfirmListener(): View.OnClickListener {
            return View.OnClickListener {
                dismiss()
            }
        }

    }
}