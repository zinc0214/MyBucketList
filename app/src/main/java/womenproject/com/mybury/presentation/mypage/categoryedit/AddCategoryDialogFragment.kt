package womenproject.com.mybury.presentation.mypage.categoryedit

import android.annotation.SuppressLint
import android.app.ActionBar
import android.view.View
import android.widget.Toast
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.databinding.DialogEditNewCategoryBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment


@SuppressLint("ValidFragment")
class AddCategoryDialogFragment(private var currentList: List<Category>,
                                private var currentName: String?,
                                private var newCategoryName: (String) -> Unit)
    : BaseDialogFragment<DialogEditNewCategoryBinding>() {

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.writeFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    override val layoutResourceId: Int
        get() = R.layout.dialog_edit_new_category


    override fun initDataBinding() {
        binding.apply {
            confirmButton = View.OnClickListener { addCategoryListener() }
            if (currentName.isNullOrBlank()) {
                categoryName = "카테고리 이름"
                isAddForm = true
            } else {
                categoryName = currentName
                isAddForm = false
            }
        }
    }


    private fun addCategoryListener() {
        binding.categoryEditText.text.apply {
            if (currentName != null && currentName == this.toString()) {
                Toast.makeText(context, "이름이 변경되지 않았습니다.", Toast.LENGTH_SHORT).show()
            } else if (currentName != null && currentName == binding.categoryEditText.hint.toString() && this.isNullOrBlank()) {
                Toast.makeText(context, "이름이 변경되지 않았습니다.", Toast.LENGTH_SHORT).show()
            } else if (alreadyUseName(this.toString())) {
                Toast.makeText(context, "중복된 이름입니다. 다른 카테고리명을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (this.isNullOrBlank()) {
                Toast.makeText(context, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                newCategoryName.invoke(this.toString())
                dismiss()
            }
        }
    }

    private fun alreadyUseName(newCategory: String): Boolean {
        currentList.forEach {
            if (it.name == newCategory) return true
        }
        return false
    }
}