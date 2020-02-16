package womenproject.com.mybury.presentation.mypage.categoryedit

import android.annotation.SuppressLint
import android.app.ActionBar
import android.util.Log
import android.view.View
import android.widget.Toast
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.databinding.EditNewCategoryDialogBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment


@SuppressLint("ValidFragment")
class AddCategoryDialogFragment(private var currentList: List<Category>,
                                private var currentName: String?,
                                private var newCategoryName: (String) -> Unit)
    : BaseDialogFragment<EditNewCategoryDialogBinding>() {

    override fun onResume() {
        super.onResume()

        val dialogWidth = resources.getDimensionPixelSize(R.dimen.writeFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    override val layoutResourceId: Int
        get() = R.layout.edit_new_category_dialog


    override fun initDataBinding() {
        viewDataBinding.apply {
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
        Log.e("ayhan", "name : ${viewDataBinding.categoryEditText.text}")
        viewDataBinding.categoryEditText.text.apply {
            if (currentName != null && currentName == this.toString()) {
                Toast.makeText(context, "이름이 변경되지 않았습니다.", Toast.LENGTH_SHORT).show()
            } else if (currentName != null && currentName == viewDataBinding.categoryEditText.hint.toString() && this.isNullOrBlank()) {
                Toast.makeText(context, "이름이 변경되지 않았습니다.", Toast.LENGTH_SHORT).show()
            } else if (alreadyUseName(this.toString())) {
                Toast.makeText(context, "동일한 카테고리 이름이 존재합니다.", Toast.LENGTH_SHORT).show()
            } else if (this.isNullOrBlank()) {
                Toast.makeText(context, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                newCategoryName.invoke(this.toString())
                dismiss()
            }
        }

    }

    private fun alreadyUseName(newCategory: String): Boolean {
        Log.e("ayhan", "name1 : ${newCategory}")

        currentList.forEach {
            Log.e("ayhan", "name2 : ${it.name}")
            if (it.name == newCategory) return true
        }
        return false
    }
}