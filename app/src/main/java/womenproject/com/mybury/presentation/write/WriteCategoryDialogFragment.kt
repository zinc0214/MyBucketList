package womenproject.com.mybury.presentation.write

import android.annotation.SuppressLint
import android.app.ActionBar
import android.view.View
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.databinding.DialogWriteFragmentCategoryBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.ui.WriteItemLayout


@SuppressLint("ValidFragment")
class WriteCategoryDialogFragment(private var userCategory: List<Category>,
                                  private var categorySetListener: (Category) -> Unit,
                                  private var moveToAddCategory: () -> Unit) : BaseDialogFragment<DialogWriteFragmentCategoryBinding>() {

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.writeFragmentWidth)
        val dialogHeight = if (userCategory.size < 5) ActionBar.LayoutParams.WRAP_CONTENT else resources.getDimensionPixelSize(R.dimen.writeFragmentWidth)
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    override val layoutResourceId: Int
        get() = R.layout.dialog_write_fragment_category


    override fun initDataBinding() {
        binding.addCategory.title = "카테고리 추가"
        binding.addCategory.itemClickListener = addCategoryListener()
        context?.resources?.getColor(R.color.press_5a95ff_995a95ff)?.let { binding.addCategory.writeItemText.setTextColor(it) }

        for (category in userCategory) {
            addCategoryItem(category)
        }
    }

    private fun addCategoryListener() : View.OnClickListener {
        return View.OnClickListener {
            AddCategoryDialog(moveToAddCategory).show(requireActivity().supportFragmentManager, "tag")
            dismiss()
        }
    }
    private fun addCategoryItem(category: Category) {

        val categorySelectListener: (Category) -> Unit = { it ->
            categorySetListener.invoke(it)
            dismiss()
        }

        val writeImgLayout = WriteItemLayout(this.requireContext(), categorySelectListener).setUI(category)
        binding.categoryListLayout.addView(writeImgLayout)
    }


    class AddCategoryDialog(private val moveToAddCategory: () -> Unit) : BaseNormalDialogFragment() {

        init {
            TITLE_MSG = "카테고리 추가"
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
                moveToAddCategory.invoke()
                dismiss()
            }
        }

    }
}