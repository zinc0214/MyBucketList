package womenproject.com.mybury.presentation.write

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.fragment_bucket_write.*
import womenproject.com.mybury.R
import womenproject.com.mybury.data.AddBucketItem
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.databinding.FragmentBucketWriteBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.viewmodels.CategoryInfoViewModel
import womenproject.com.mybury.ui.ShowImgWideFragment
import womenproject.com.mybury.ui.WriteImgLayout
import java.io.File
import java.util.*
import kotlin.collections.HashMap


class BucketWriteFragment : BaseFragment<FragmentBucketWriteBinding, BucketWriteViewModel>() {

    private var addImgList = HashMap<Int, RelativeLayout>()
    private var goalCount = 1
    private var currentCalendarDay = Calendar.getInstance().time
    private lateinit var categoryList: BucketCategory
    private var imgList = ArrayList<File>()

    private val bucketInfoViewModel = CategoryInfoViewModel()

    override val layoutResourceId: Int
        get() = R.layout.fragment_bucket_write

    override val viewModel: BucketWriteViewModel
        get() = BucketWriteViewModel()

    override fun initStartView() {

        bucketInfoViewModel.getCategoryList(object: CategoryInfoViewModel.GetBucketListCallBackListener {


            override fun start() {
                viewDataBinding.addBucketProgressBar.visibility = View.VISIBLE
            }

            override fun success(bucketCategory: BucketCategory) {
                categoryList = bucketCategory
                viewDataBinding.addBucketProgressBar.visibility = View.GONE
            }

            override fun fail() {
                viewDataBinding.addBucketProgressBar.visibility = View.GONE
                Toast.makeText(context, "카테고리 값을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                //activity!!.onBackPressed()

            }

        })
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

        viewDataBinding.cancelBtnClickListener = writeCancelOnClickListener()
        viewDataBinding.registerBtnClickListener = bucketAddOnClickListener()
        viewDataBinding.memoImgAddListener = memoImgAddOnClickListener()
        viewDataBinding.memoRemoveListener = memoRemoveListener()
        viewDataBinding.ddayAddListener = ddayAddListener()
        viewDataBinding.goalCountSettingListener = goalCountSetListener()
        viewDataBinding.categorySelectListener = selectCategoryListener()

        viewDataBinding.titleText.addTextChangedListener(titleTextChangedListener(viewDataBinding.titleText))
        viewDataBinding.memoText.addTextChangedListener(memoTextChangedListener(viewDataBinding.memoText))
        viewDataBinding.memoRemoveImg.setOnTouchListener(memoRemoveOnTouchListener())
        viewDataBinding.memoImgLayout.setOnTouchListener(memoImgAddOnTouchListener())

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
                viewDataBinding.openImg.background = context!!.getDrawable(R.drawable.open_enable)
            } else {
                viewDataBinding.openText.text = context!!.resources.getString(R.string.bucket_close)
                viewDataBinding.openImg.background = context!!.getDrawable(R.drawable.open_disable)
            }
        }

    }

    private fun writeCancelOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            activity!!.onBackPressed()
        }
    }

    private fun bucketAddOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            viewModel.uploadBucketList(setBucketItemInfoData(), imgList, object : BucketWriteViewModel.OnBucketAddEvent {
                override fun start() {
                    viewDataBinding.addBucketProgressBar.visibility = View.VISIBLE

                }

                override fun success() {
                    viewDataBinding.addBucketProgressBar.visibility = View.GONE
                    Toast.makeText(context, "버킷이 등록되었습니다", Toast.LENGTH_SHORT).show()
                    activity!!.onBackPressed()
                }

                override fun fail() {
                    viewDataBinding.addBucketProgressBar.visibility = View.GONE
                    Toast.makeText(context, "버킷이 등록되지 못했습니다. 흑흑흑", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun titleTextChangedListener(editText: EditText): TextWatcher {

        return object : TextWatcher {
            var previousString = ""

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                previousString = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.lineCount > 2) {
                    editText.setText(previousString)
                    editText.setSelection(editText.length())
                }
                viewDataBinding.writeRegist.isEnabled = editText.length() >= 1
            }
        }
    }

    private fun memoTextChangedListener(editText: EditText): TextWatcher {

        return object : TextWatcher {
            var previousString = ""

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                previousString = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.lineCount > 2) {
                    editText.setText(previousString)
                    editText.setSelection(editText.length())
                }
            }
        }
    }

    private fun memoImgAddOnTouchListener(): View.OnTouchListener {
        return View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewDataBinding.memoImgText.typeface = Typeface.DEFAULT_BOLD;
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    viewDataBinding.memoImgText.typeface = Typeface.DEFAULT;
                }
            }
            false
        }
    }

    private fun memoRemoveOnTouchListener(): View.OnTouchListener {

        return View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewDataBinding.memoLayout.background = context!!.getDrawable(R.drawable.write_memo_press_background)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    viewDataBinding.memoLayout.background = context!!.getDrawable(R.drawable.write_memo_background)
                }
            }
            false
        }
    }

    private fun memoImgAddOnClickListener(): View.OnClickListener {

        val checkMemoAddListener: () -> Boolean = {
            viewDataBinding.memoLayout.visibility != View.VISIBLE
        }
        val memoAddListener: () -> Unit = {
            viewDataBinding.memoLayout.visibility = View.VISIBLE
        }

        val checkAddImgAbleListener: () -> Boolean = {
            if (viewDataBinding.imgLayout.childCount > 2) {
                Toast.makeText(context, "더 이상 이미지를 추가하실 수 없습니다.", Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        }

        val imgAddListener: (File, Uri) -> Unit = { file: File, uri: Uri ->
            onAddImgField(file, uri)
        }

        return View.OnClickListener {
            WriteMemoImgAddDialogFragment(AddContentType.MEMO, checkMemoAddListener, memoAddListener, checkAddImgAbleListener, imgAddListener).show(activity!!.supportFragmentManager, "tag")
        }
    }


    private fun onAddImgField(file: File, uri: Uri) {

        val removeImgListener: (View) -> Unit = { it ->
            onDeleteImgField(it)
        }


        val imgFieldClickListener: (Uri) -> Unit = {
            showImgWide(it)
        }


        val writeImgLayout = WriteImgLayout(this.context!!, removeImgListener, imgFieldClickListener).setUI(uri)
        addImgList.put(viewDataBinding.imgLayout.childCount, writeImgLayout as RelativeLayout)
        imgList.add(file)
        viewDataBinding.imgLayout.addView(writeImgLayout)
    }

    private fun onDeleteImgField(layout: View) {
        var deleteImgValue = 0

        for (i in 0..addImgList.size) {
            if (layout.equals(addImgList[i])) {
                deleteImgValue = i
            }
        }
        viewDataBinding.imgLayout.removeView(viewDataBinding.imgLayout.getChildAt(deleteImgValue))
        addImgList.remove(deleteImgValue)
        imgList.removeAt(deleteImgValue)
    }

    private fun showImgWide(uri: Uri) {
        val showImgWideFragment = ShowImgWideFragment(uri)
        showImgWideFragment.show(activity!!.supportFragmentManager, "tag")
    }

    private fun memoRemoveListener(): View.OnClickListener {

        return View.OnClickListener {
            viewDataBinding.memoLayout.visibility = View.GONE
        }
    }

    private fun ddayAddListener(): View.OnClickListener {

        val ddayAddListener: (String, Date) -> Unit = { dday, date ->

            if(dday.isEmpty()) {
                viewDataBinding.ddayText.text = "추가"
                currentCalendarDay = date
                viewDataBinding.ddayText.setDisableTextColor()
                viewDataBinding.ddayImg.setImage(R.drawable.calendar_disable)
            } else {
                viewDataBinding.ddayText.text = dday
                currentCalendarDay = date
                viewDataBinding.ddayText.setEnableTextColor()
                viewDataBinding.ddayImg.setImage(R.drawable.calendar_enable)
            }

        }

        return View.OnClickListener {

            Log.e("ayhan", currentCalendarDay.toString())
            val calendarDialogFragment = WriteCalendarDialogFragment(ddayAddListener, currentCalendarDay)
            calendarDialogFragment.show(activity!!.supportFragmentManager, "tag")
        }
    }

    private fun goalCountSetListener(): View.OnClickListener {
        val goalCountSetListener: (String) -> Unit = { count ->
            viewDataBinding.goalCountText.text = count
            goalCount = count.toInt()
            if (count.toInt() != 1) {
                viewDataBinding.goalCountText.setEnableTextColor()
                viewDataBinding.countImg.setImage(R.drawable.target_count_enable)
            } else {
                viewDataBinding.goalCountText.setDisableTextColor()
                viewDataBinding.countImg.setImage(R.drawable.target_count_disable)
            }
        }

        return View.OnClickListener {
            WriteGoalCountDialogFragment(goalCount, goalCountSetListener).show(activity!!.supportFragmentManager, "tag")
        }
    }

    private fun selectCategoryListener(): View.OnClickListener {
        val categorySetListener: (String) -> Unit = { title ->
            if (title.equals("없음")) {
                viewDataBinding.categoryText.text = title
                viewDataBinding.categoryText.setDisableTextColor()
                viewDataBinding.categoryImg.setImage(R.drawable.category_disable)
            } else {
                viewDataBinding.categoryText.text = title
                viewDataBinding.categoryText.setEnableTextColor()
                viewDataBinding.categoryImg.setImage(R.drawable.category_enable)
            }

        }

        return View.OnClickListener {
            WriteCategoryDialogFragment(categoryList, categorySetListener).show(activity!!.supportFragmentManager, "tag")
        }
    }


    private fun setBucketItemInfoData(): AddBucketItem {
        if(goal_count_text.text.toString() == "설정") {
            goalCount = 1
        } else {
            goalCount = goal_count_text.text.toString().toInt()
        }

        return AddBucketItem(viewDataBinding.titleText.text.toString(), viewDataBinding.openSwitchBtn.isChecked,
                currentCalendarDay.time , goalCount,
                viewDataBinding.memoText.text.toString(),  viewDataBinding.categoryText.text.toString(), "userId1")
    }


    private fun TextView.setEnableTextColor() {
        this.setTextColor(context!!.resources.getColor(R.color.mainColor))
    }

    private fun TextView.setDisableTextColor() {
        this.setTextColor(context!!.resources.getColor(R.color.writeUnusedText))
    }

    private fun ImageView.setImage(resource: Int) {
        this.background = resource.getDrawable()
    }

    private fun Int.getDrawable(): Drawable {
        return context!!.getDrawable(this)
    }

}