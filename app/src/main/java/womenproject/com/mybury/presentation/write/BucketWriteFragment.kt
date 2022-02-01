package womenproject.com.mybury.presentation.write

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_bucket_write.*
import womenproject.com.mybury.BuildConfig
import womenproject.com.mybury.R
import womenproject.com.mybury.data.AddBucketItem
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.databinding.FragmentBucketWriteBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.dialog.LoadFailDialog
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel
import womenproject.com.mybury.ui.ShowImgWideFragment
import womenproject.com.mybury.ui.WriteImgLayout
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
open class BucketWriteFragment : BaseFragment<FragmentBucketWriteBinding>() {

    var alreadyImgList = mutableMapOf<Int, String?>()
    var addImgList = mutableMapOf<Int, String?>()
    var addImgViewList = mutableMapOf<String, View>()
    var imgList = mutableMapOf<String, Any?>()
    var currentCalendarDay: Date? = null
    var currentCalendarText: String = ""
    var selectCategory: Category? = null
    var goalCount = 1

    private var open = true
    private var categoryList = arrayListOf<Category>()
    var isAdsShow = false

    override val layoutResourceId: Int
        get() = R.layout.fragment_bucket_write

    val viewModel by viewModels<BucketWriteViewModel>()

    private val bucketInfoViewModel = BucketInfoViewModel()
    lateinit var imm: InputMethodManager

    override fun initDataBinding() {
        loadArgument()
        setUpViewModelObservers()
        bucketInfoViewModel.getCategoryList()
    }

    open fun loadArgument() {
        arguments?.let {
            val args = BucketWriteFragmentArgs.fromBundle(it)
            val argIsAdsShow = args.isAdsShow
            this.isAdsShow = argIsAdsShow
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelConfirm = false

        val goToActionCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isCancelConfirm) {
                    isEnabled = false
                    requireActivity().onBackPressed()
                } else {
                    actionByBackButton()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, goToActionCallback)
        imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onResume() {
        super.onResume()
        updateView()
    }

    override fun actionByBackButton() {
        val cancelConfirm: (Boolean) -> Unit = {
            if (it) {
                isCancelConfirm = it
                requireActivity().onBackPressed()
            }
        }
        CancelDialog(cancelConfirm).show(requireActivity().supportFragmentManager, "tag")
    }

    private fun setBackClickListener() = View.OnClickListener { actionByBackButton() }

    private fun setUpViewModelObservers() {
        bucketInfoViewModel.categoryLoadState.observe(viewLifecycleOwner) {
            when (it) {
                BaseViewModel.LoadState.START -> {
                    startLoading()
                }
                BaseViewModel.LoadState.RESTART -> {
                    bucketInfoViewModel.getCategoryList()
                }
                BaseViewModel.LoadState.SUCCESS -> {
                    stopLoading()
                }
                BaseViewModel.LoadState.FAIL -> {
                    stopLoading()
                    LoadFailDialog {
                        backBtnOnClickListener()
                    }.show(requireActivity().supportFragmentManager, "tag")
                }
            }
        }

        bucketInfoViewModel.categoryList.observe(viewLifecycleOwner) {
            categoryList = it as ArrayList<Category>
            initForUpdate()
            alreadyAdd()
            setUpView()
            setUpCategory(categoryList)
        }
    }

    open fun setUpCategory(categoryList: ArrayList<Category>) {
        selectCategory = categoryList.firstOrNull { it.name == "없음" }
    }

    private fun updateView() {
        binding.apply {
            if (memoText.text.isNotBlank()) {
                memoLayout.visibility = View.VISIBLE
            }

            categoryText.text = selectCategory?.name ?: "없음"
            if (categoryText.text != "없음") {
                categoryText.setEnableTextColor()
                categoryImg.setImage(R.drawable.category_enable)
            }
            if (currentCalendarText.isNotBlank()) {
                ddayText.text = currentCalendarText
                ddayText.setEnableTextColor()
                ddayImg.setImage(R.drawable.calendar_enable)
            }
            if (goalCount > 2) {
                goalCountText.text = goalCount.toString()
                goalCountText.setEnableTextColor()
                countImg.setImage(R.drawable.target_count_enable)
            }
            if (addImgViewList.isNotEmpty()) {
                val currentCount = imgLayout.childCount
                addImgViewList.forEach { (_, u) ->
                    if (currentCount == 0) {
                        if (u.parent != null) {
                            val view = u.parent as ViewGroup
                            view.removeView(u)
                            imgLayout.addView(u)
                        } else {
                            imgLayout.addView(u)
                        }
                    }
                }
            }
        }
    }

    private fun setUpView() {

        binding.apply {
            writeViewModel = viewModel
            cancelBtnClickListener = setBackClickListener()
            registerBtnClickListener = bucketAddOnClickListener()
            memoImgAddListener = memoImgAddOnClickListener()
            memoRemoveListener = memoRemoveListener()
            ddayAddListener = ddayAddListener()
            goalCountSettingListener = goalCountSetListener()
            categorySelectListener = selectCategoryListener()

            writeRegist.isEnabled = titleText.text.isNotBlank()

            titleText.addTextChangedListener(titleTextChangedListener(binding.titleText))
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
                    if (p0!!.targetPosition.toString() == "0.0") {
                        openImg.background = requireContext().getDrawable(R.drawable.open_enable)
                        open = true
                    } else {
                        openImg.background = requireContext().getDrawable(R.drawable.open_disable)
                        open = false
                    }
                }

                override fun onTransitionTrigger(
                    p0: MotionLayout?,
                    p1: Int,
                    p2: Boolean,
                    p3: Float
                ) {
                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                }

                override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                }

            })

        }

    }

    class CancelDialog(private val cancelConfirm: (Boolean) -> Unit) : BaseNormalDialogFragment() {

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

    open fun bucketAddOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            addBucket()
        }
    }

    private fun addBucket() {
        viewModel.uploadBucketList(
            getBucketItemInfo(),
            getRealSendImgList(),
            object : BaseViewModel.Simple3CallBack {
                override fun restart() {
                    addBucket()
                }

                override fun start() {
                    imm.hideSoftInputFromWindow(binding.titleText.windowToken, 0)
                    imm.hideSoftInputFromWindow(binding.memoText.windowToken, 0)
                    startLoading()
                }

                override fun success() {
                    stopLoading()
                    Toast.makeText(context, "버킷리스트를 등록했습니다.", Toast.LENGTH_SHORT).show()
                    isCancelConfirm = true
                    if (isAdsShow || BuildConfig.DEBUG) {
                        startAdMob()
                    }
                    requireActivity().onBackPressed()
                }

                override fun fail() {
                    stopLoading()
                    Toast.makeText(context, "버킷리스트 등록에 실패했습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    fun getRealSendImgList(): ArrayList<Any?> {
        val realImgList = ArrayList<Any?>()

        addImgList.forEach { (_, addValue) ->
            imgList.forEach { (fileKey, fileValue) ->
                if (addValue == fileKey) {
                    realImgList.add(fileValue)
                }
            }
        }
        return realImgList
    }

    private fun titleTextChangedListener(editText: EditText): TextWatcher {

        return object : TextWatcher {
            var previousString = ""

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                previousString = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.lineCount > 3) {
                    editText.setText(previousString)
                    editText.setSelection(editText.length())
                }
                binding.writeRegist.isEnabled = editText.text.isNotBlank()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun memoImgAddOnTouchListener(): View.OnTouchListener {
        return View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.memoImgText.typeface = Typeface.DEFAULT_BOLD
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    binding.memoImgText.typeface = Typeface.DEFAULT
                }
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun memoRemoveOnTouchListener(): View.OnTouchListener {

        return View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.memoLayout.background =
                        requireContext().getDrawable(R.drawable.shape_dfdfdf_r4)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    binding.memoLayout.background =
                        requireContext().getDrawable(R.drawable.shape_f3f3f3_r4)
                }
            }
            false
        }
    }

    private fun memoImgAddOnClickListener(): View.OnClickListener {

        val checkMemoAddListener: () -> Boolean = {
            binding.memoLayout.visibility != View.VISIBLE
        }
        val memoAddListener: () -> Unit = {
            binding.memoLayout.visibility = View.VISIBLE
        }

        val checkAddImgAbleListener: () -> Boolean = {
            binding.imgLayout.childCount <= 2
        }

        val imgAddListener: (File, Uri) -> Unit = { file: File, uri: Uri ->
            onAddImgField(file, uri)
        }

        return View.OnClickListener {
            WriteMemoImgAddDialogFragment(
                AddContentType.MEMO,
                checkMemoAddListener,
                memoAddListener,
                checkAddImgAbleListener,
                imgAddListener
            ).show(requireActivity().supportFragmentManager, "tag")
        }
    }


    private fun onAddImgField(file: File, uri: Uri) {

        val removeImgListener: (String) -> Unit = { it ->
            onDeleteImgField(it)
        }


        val imgFieldClickListener: (Any) -> Unit = {
            if (it is Uri) {
                showImgWide(it)
            }
        }

        imgList[file.name] = file
        val id = file.name
        val writeImgLayout = WriteImgLayout(
            this.requireContext(),
            id,
            removeImgListener,
            imgFieldClickListener
        ).setUI(uri)
        binding.imgLayout.addView(writeImgLayout)

        addImgList[binding.imgLayout.childCount - 1] = id
        addImgViewList[id] = writeImgLayout
    }

    private fun alreadyAdd() {

        val removeImgListener: (String) -> Unit = { it ->
            onDeleteImgField(it)
        }

        val imgFieldClickListener: (Any) -> Unit = {
            if (it is String) {
                showImgWide(it)
            }
        }

        alreadyImgList.forEach {
            val id = it.value
            if (!id.isNullOrEmpty()) {
                id.run {
                    val writeImgLayout = WriteImgLayout(
                        requireContext(),
                        this,
                        removeImgListener,
                        imgFieldClickListener
                    ).setAleadyUI(this)
                    if (!addImgList.values.contains(this)) {
                        addImgList[binding.imgLayout.childCount] = this
                        addImgViewList[this] = writeImgLayout
                        imgList[id] = this
                        binding.imgLayout.addView(writeImgLayout)
                    }
                }
            }
        }
    }


    private fun onDeleteImgField(layout: String) {
        var deleteImgValue: Int? = null

        for (i in 0 until addImgList.size) {
            if (layout == addImgList[i]) {
                deleteImgValue = i
                break
            }
        }

        deleteImgValue?.run {
            binding.imgLayout.removeView(binding.imgLayout.getChildAt(this))
            val id = addImgList[this]
            addImgViewList.remove(id)
            addImgList.replace(this, null)

            val newList = addImgList.filter { it.value != null }.values
            var newNum = 0

            val tmpImgList = mutableMapOf<Int, String?>()

            newList.forEach {
                tmpImgList[newNum] = it
                newNum += 1
            }
            addImgList = tmpImgList

            alreadyImgList.forEach { (num, str) ->
                if (id == str) {
                    alreadyImgList[num] = null
                }
            }
        }
    }

    private fun showImgWide(uri: Uri) {
        val showImgWideFragment = ShowImgWideFragment(uri)
        showImgWideFragment.show(requireActivity().supportFragmentManager, "tag")
    }

    private fun showImgWide(url: String) {
        val showImgWideFragment = ShowImgWideFragment(url)
        showImgWideFragment.show(requireActivity().supportFragmentManager, "tag")
    }


    private fun memoRemoveListener() = View.OnClickListener {
        binding.memoText.text.clear()
        binding.memoLayout.visibility = View.GONE
    }


    private fun ddayAddListener(): View.OnClickListener {
        val ddayAddListener: (String, Date) -> Unit = { dday, date ->
            if (dday.isBlank()) {
                binding.ddayText.text = "추가"
                currentCalendarDay = null
                currentCalendarText = ""
                binding.ddayText.setDisableTextColor()
                binding.ddayImg.setImage(R.drawable.calendar_disable)
            } else {
                binding.ddayText.text = dday
                currentCalendarDay = date
                currentCalendarText = dday
                binding.ddayText.setEnableTextColor()
                binding.ddayImg.setImage(R.drawable.calendar_enable)
            }

        }

        return View.OnClickListener {
            if (currentCalendarDay == null) {
                currentCalendarDay = Calendar.getInstance().time
            }
            val calendarDialogFragment =
                WriteCalendarDialogFragment(ddayAddListener, currentCalendarDay!!)
            calendarDialogFragment.show(requireActivity().supportFragmentManager, "tag")
        }
    }

    private fun goalCountSetListener(): View.OnClickListener {
        val goalCountSetListener: (String) -> Unit = { count ->
            binding.goalCountText.text = count
            goalCount = count.toInt()
            if (count.toInt() != 1) {
                binding.goalCountText.setEnableTextColor()
                binding.countImg.setImage(R.drawable.target_count_enable)
            } else {
                binding.goalCountText.setDisableTextColor()
                binding.countImg.setImage(R.drawable.target_count_disable)
            }
        }

        return View.OnClickListener {
            WriteGoalCountDialogFragment(
                goalCount,
                goalCountSetListener
            ).show(requireActivity().supportFragmentManager, "tag")
        }
    }

    open fun moveToAddCategory(v: View): () -> Unit = {
        val directions = BucketWriteFragmentDirections.actionWriteToCategoryEdit()
        v.findNavController().navigate(directions)
    }

    private fun selectCategoryListener(): View.OnClickListener {
        val categorySetListener: (Category) -> Unit = { category ->
            selectCategory = category
            if (category.name == "없음") {
                binding.categoryText.text = category.name
                binding.categoryText.setDisableTextColor()
                binding.categoryImg.setImage(R.drawable.category_disable)
            } else {
                binding.categoryText.text = category.name
                binding.categoryText.setEnableTextColor()
                binding.categoryImg.setImage(R.drawable.category_enable)
            }

        }

        return View.OnClickListener {
            WriteCategoryDialogFragment(
                categoryList,
                categorySetListener,
                moveToAddCategory(it)
            ).show(requireActivity().supportFragmentManager, "tag")
        }
    }


    fun getBucketItemInfo(): AddBucketItem {
        goalCount = if (goal_count_text.text.toString() == "설정") {
            1
        } else {
            goal_count_text.text.toString().toInt()
        }

        var formDate = ""
        currentCalendarDay?.let {
            formDate = SimpleDateFormat("yyyy-MM-dd").format(it).toString()
        }

        return AddBucketItem(
            binding.titleText.text.toString(), open,
            formDate, goalCount,
            binding.memoText.text.toString(), selectCategory?.id!!
        )
    }


    protected fun TextView.setEnableTextColor() {
        this.setTextColor(requireContext().resources.getColor(R.color._5a95ff))
    }

    private fun TextView.setDisableTextColor() {
        this.setTextColor(requireContext().resources.getColor(R.color._888888))
    }

    protected fun ImageView.setImage(resource: Int) {
        this.background = resource.getDrawable()
    }

    private fun Int.getDrawable(): Drawable? {
        return requireContext().getDrawable(this)
    }

    open fun initForUpdate() {}

}