package womenproject.com.mybury.presentation.mypage.categoryedit

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.databinding.ItemCategoryBinding
import womenproject.com.mybury.ui.ItemCheckedListener
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.util.Converter.Companion.dpToPx

class EditCategoryListViewHolder(
    private val binding: ItemCategoryBinding,
    private val dragListener: ItemDragListener,
    private val checkedListener: ItemCheckedListener,
    private val editCategoryName: (Category) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var isKeyBoardShown = false

    @SuppressLint("ClickableViewAccessibility")
    fun bind(category: Category) {
        binding.apply {

            val isDefaultItem = category.name == "없음"

            Log.e("ayhan", "category : $category")
            bucketName = category.name
            removeBox.isEnabled = !isDefaultItem
            //  removeBox.isChecked = !isDefaultItem
            editLayout.visibility = if (isDefaultItem) View.GONE else View.VISIBLE

            dragLayout.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this@EditCategoryListViewHolder)
                    categoryItemLayout.elevation = dpToPx(0).toFloat()
                } else if (event.action == MotionEvent.ACTION_CANCEL) {
                    categoryItemLayout.elevation = dpToPx(5).toFloat()
                }
                true
            }
            editLayout.setOnClickListener { v ->
                editCategoryName.invoke(category)
            }
            removeBox.setOnCheckedChangeListener { _, isChecked ->
                checkedListener.checked(isChecked, category)
            }

            binding.root.viewTreeObserver.addOnGlobalLayoutListener(
                setOnSoftKeyboardChangedListener(
                    category.name
                )
            )
            executePendingBindings()
        }
    }

    private fun setOnSoftKeyboardChangedListener(name: String): ViewTreeObserver.OnGlobalLayoutListener {
        return ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            binding.root.getWindowVisibleDisplayFrame(r)

            val heightDiff = binding.root.rootView.height - (r.bottom - r.top)
            try {
                isKeyBoardShown = if (heightDiff < 500) {
                    binding.categoryText.setText(name)
                    false
                } else {
                    true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}