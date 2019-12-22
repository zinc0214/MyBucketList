package womenproject.com.mybury.presentation.write

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_bucket_write.*
import womenproject.com.mybury.R
import womenproject.com.mybury.data.AddBucketItem
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.databinding.FragmentBucketWriteBinding
import womenproject.com.mybury.presentation.NetworkFailDialog
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel
import womenproject.com.mybury.ui.ShowImgWideFragment
import womenproject.com.mybury.ui.WriteImgLayout
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


open class BucketWriteFragment : BaseFragment<FragmentBucketWriteBinding, BucketWriteViewModel>() {

    var alreadyImgList = arrayListOf<String>()
    var imgList = ArrayList<Any>()


    private var addImgList = HashMap<Int, RelativeLayout>()
    private var goalCount = 1
    private var currentCalendarDay: Date? = null
    private var open = true
    private var categoryList = arrayListOf<Category>()

    lateinit var selectCategory: Category

    override val layoutResourceId: Int
        get() = R.layout.fragment_bucket_write

    override val viewModel: BucketWriteViewModel
        get() = BucketWriteViewModel()

    private val bucketInfoViewModel = BucketInfoViewModel()

    override fun initDataBinding() {
        getCategory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.addOnBackPressedCallback(this, OnBackPressedCallback {
            Log.e("ayhan", "softBackBtnClick")
            if(isCancelConfirm) {
                false
            } else {
                backBtn()
                true
            }

        })
    }

    open fun backBtn() {
        val cancelConfirm: (Boolean) -> Unit = {
            if (it) {
                isCancelConfirm = it
                activity!!.onBackPressed()
            }
        }
        CancelDialog(cancelConfirm).show(activity!!.supportFragmentManager, "tag")
    }

    private fun setBackClickListener() = View.OnClickListener { backBtn() }

    private fun getCategory() {
        bucketInfoViewModel.getCategoryList(object : BaseViewModel.MoreCallBackAnyList {
            override fun restart() {
                getCategory()
            }

            override fun success(_categoryList: List<Any>) {
                stopLoading()
                Log.e("ayhan", "cate: ${_categoryList.size}")
                categoryList = _categoryList as ArrayList<Category>
                initForUpdate()
                alreadyAdd()
                setUpView()
                setUpCategory(categoryList)
            }

            override fun start() {
                startLoading()
            }

            override fun fail() {
                NetworkFailDialog().show(activity!!.supportFragmentManager, "tag")
                stopLoading()
            }
        })

    }

    private fun alreadyAdd() {

        val removeImgListener: (View) -> Unit = { it ->
            onDeleteImgField(it)
        }


        val imgFieldClickListener: (Any) -> Unit = {
            if (it is String) {
                showImgWide(it)
            }
        }
        for (list in alreadyImgList) {
            val writeImgLayout = WriteImgLayout(this.context!!, removeImgListener, imgFieldClickListener).setAleadyUI(list)
            addImgList.put(viewDataBinding.imgLayout.childCount, writeImgLayout as RelativeLayout)
            imgList.add(list)
            viewDataBinding.imgLayout.addView(writeImgLayout)
        }

    }

    private fun setUpView() {

        viewDataBinding.apply {
            cancelBtnClickListener = setBackClickListener()
            registerBtnClickListener = bucketAddOnClickListener()
            memoImgAddListener = memoImgAddOnClickListener()
            memoRemoveListener = memoRemoveListener()
            ddayAddListener = ddayAddListener()
            goalCountSettingListener = goalCountSetListener()
            categorySelectListener = selectCategoryListener()

            titleText.addTextChangedListener(titleTextChangedListener(viewDataBinding.titleText))
            memoText.addTextChangedListener(memoTextChangedListener(viewDataBinding.memoText))
            memoRemoveImg.setOnTouchListener(memoRemoveOnTouchListener())
            memoImgLayout.setOnTouchListener(memoImgAddOnTouchListener())

            memoText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    memoRemoveImg.visibility = View.INVISIBLE
                } else {
                    memoRemoveImg.visibility = View.VISIBLE
                }
            }

            openSwitchBtn.setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                    if (p0!!.targetPosition.toString().equals("0.0")) {
                        Log.e("ayhan", "111")
                        openImg.background = context!!.getDrawable(R.drawable.open_enable)
                        open = true
                    } else {
                        Log.e("ayhan", "222")
                        openImg.background = context!!.getDrawable(R.drawable.open_disable)
                        open = false
                    }
                    Log.e("ayhan", "tran  :" + p0!!.id + "," + p0.targetPosition + "," + p0.transitionName)
                }

                override fun allowsTransition(p0: MotionScene.Transition?): Boolean {
                    return true
                }

                override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                }

                override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                }

            })

        }

    }

    class CancelDialog(val cancelConfirm: (Boolean) -> Unit) : BaseNormalDialogFragment() {

        init {
            TITLE_MSG = "등록을 취소하시겠습니까?"
            CONTENT_MSG = "작성중이던 버킷리스트가 삭제됩니다."
            CANCEL_BUTTON_VISIBLE = true
            GRADIENT_BUTTON_VISIBLE = false
            CONFIRM_TEXT = "등록 취소"
            CANCEL_TEXT = "계속 작성"
        }

        override fun createOnClickConfirmListener(): View.OnClickListener {
            return View.OnClickListener {
                cancelConfirm.invoke(true)
                dismiss()
            }
        }

        override fun createOnClickCancelListener(): View.OnClickListener {
            return View.OnClickListener {
                cancelConfirm.invoke(false)
                dismiss()
            }
        }
    }

    override fun actionByBackButton() {
        Log.e("ayhan", "writeBack")


    }

    open fun bucketAddOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            addBucket()
        }
    }

    private fun addBucket() {
        viewModel.uploadBucketList(getBucketItemInfo(), imgList, object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                addBucket()
            }

            override fun start() {
                startLoading()

            }

            override fun success() {
                stopLoading()
                Toast.makeText(context, "버킷리스트를 등록했습니다.", Toast.LENGTH_SHORT).show()
                isCancelConfirm = true
                activity!!.onBackPressed()
            }

            override fun fail() {
                stopLoading()
                Toast.makeText(context, "버킷리스트 등록에 실패했습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }

        })
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
                    viewDataBinding.memoImgText.typeface = Typeface.DEFAULT_BOLD
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    viewDataBinding.memoImgText.typeface = Typeface.DEFAULT
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


        val imgFieldClickListener: (Any) -> Unit = {
            if (it is Uri) {
                showImgWide(it)
            }
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

    fun showImgWide(uri: Uri) {
        val showImgWideFragment = ShowImgWideFragment(uri)
        showImgWideFragment.show(activity!!.supportFragmentManager, "tag")
    }

    fun showImgWide(url: String) {
        val showImgWideFragment = ShowImgWideFragment(url)
        showImgWideFragment.show(activity!!.supportFragmentManager, "tag")
    }


    private fun memoRemoveListener(): View.OnClickListener {

        return View.OnClickListener {
            viewDataBinding.memoLayout.visibility = View.GONE
        }
    }

    private fun ddayAddListener(): View.OnClickListener {

        val ddayAddListener: (String, Date) -> Unit = { dday, date ->

            if (dday.isEmpty()) {
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
            if (currentCalendarDay == null) {
                currentCalendarDay = Calendar.getInstance().time
            }
            val calendarDialogFragment = WriteCalendarDialogFragment(ddayAddListener, currentCalendarDay!!)
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
        val categorySetListener: (Category) -> Unit = { category ->
            selectCategory = category
            if (category.name.equals("없음")) {
                viewDataBinding.categoryText.text = category.name
                viewDataBinding.categoryText.setDisableTextColor()
                viewDataBinding.categoryImg.setImage(R.drawable.category_disable)
            } else {
                viewDataBinding.categoryText.text = category.name
                viewDataBinding.categoryText.setEnableTextColor()
                viewDataBinding.categoryImg.setImage(R.drawable.category_enable)
            }

        }

        return View.OnClickListener {
            val moveToAddCategory: () -> Unit = {
                val directions = BucketWriteFragmentDirections.actionWriteToCategoryEdit()
                it.findNavController().navigate(directions)
            }
            WriteCategoryDialogFragment(categoryList, categorySetListener, moveToAddCategory).show(activity!!.supportFragmentManager, "tag")
        }
    }


    fun getBucketItemInfo(): AddBucketItem {
        if (goal_count_text.text.toString() == "설정") {
            goalCount = 1
        } else {
            goalCount = goal_count_text.text.toString().toInt()
        }


        var formDate = ""

        if (currentCalendarDay != null) {
            formDate = SimpleDateFormat("yyyy-MM-dd").format(currentCalendarDay).toString()
        }

        return AddBucketItem(viewDataBinding.titleText.text.toString(), open,
                formDate, goalCount,
                viewDataBinding.memoText.text.toString(), selectCategory.id)
    }


    protected fun TextView.setEnableTextColor() {
        this.setTextColor(context!!.resources.getColor(R.color._5a95ff))
    }

    protected fun TextView.setDisableTextColor() {
        this.setTextColor(context!!.resources.getColor(R.color._888888))
    }

    protected fun ImageView.setImage(resource: Int) {
        this.background = resource.getDrawable()
    }

    private fun Int.getDrawable(): Drawable {
        return context!!.getDrawable(this)
    }


    open fun initForUpdate() {}
    open fun setUpCategory(categoryList: ArrayList<Category>) {}

}