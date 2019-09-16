package womenproject.com.mybury.presentation.mypage.profileedit

import android.graphics.Rect
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import com.bumptech.glide.Glide
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.FragmentProfileEditBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment
import womenproject.com.mybury.presentation.write.AddContentType
import womenproject.com.mybury.presentation.write.WriteMemoImgAddDialogFragment
import java.io.File

class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding, ProfileEditViewModel>()  {
    override val layoutResourceId: Int
        get() = R.layout.fragment_profile_edit

    override val viewModel: ProfileEditViewModel
        get() = ProfileEditViewModel()

    override fun initDataBinding() {
        viewDataBinding.viewModel = viewModel
        viewDataBinding.profileImageEditClickListener = profileImageEditClickLister
        viewDataBinding.backBtnOnClickListener = cancelClickListener
        viewDataBinding.nicknameEditText.addTextChangedListener(addTextChangedListener())

        viewDataBinding.root.viewTreeObserver.addOnGlobalLayoutListener(setOnSoftKeyboardChangedListener())
    }


    private fun addTextChangedListener(): TextWatcher {

        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(viewModel.nickname == viewDataBinding.nicknameEditText.text.toString()) {
                    viewDataBinding.nicknameEditText.setTextColor(resources.getColor(R.color.dialogUncheckBoxTextColor))
                    viewDataBinding.profileSave.isEnabled = false
                } else {
                    viewDataBinding.nicknameEditText.setTextColor(resources.getColor(R.color.bucketBaseText))
                    viewDataBinding.profileSave.isEnabled = true
                }
            }

        }


    }

    private val checkBaseProfileImgUsable: () -> Boolean = {
        true
    }
    private val baseProfileUseListener: () -> Unit = {
        Toast.makeText(activity, "기본 이미지 준비중, 찡긋", Toast.LENGTH_SHORT).show()
        viewDataBinding.profileImg.background = context!!.resources.getDrawable(R.drawable.app_info_icon)
    }

    private val checkAddImgAbleListener: () -> Boolean = {
        true
    }

    private val imgAddListener: (File, Uri) -> Unit = { file: File, uri: Uri ->
        Glide.with(context!!).load(uri).centerCrop().into(viewDataBinding.profileImg)
    }

    private val profileImageEditClickLister = View.OnClickListener {
        WriteMemoImgAddDialogFragment(AddContentType.PROFILE, checkBaseProfileImgUsable, baseProfileUseListener,
                checkAddImgAbleListener, imgAddListener).show(activity!!.supportFragmentManager, "tag")

    }

    private val cancelClickListener = View.OnClickListener {
        if(viewModel.nickname != viewDataBinding.nicknameEditText.text.toString()) {
            CancelDialog().show(activity!!.supportFragmentManager, "tag")
        } else {
            activity!!.onBackPressed()
        }
    }


    private fun setOnSoftKeyboardChangedListener(): ViewTreeObserver.OnGlobalLayoutListener {
        return ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            viewDataBinding.root.getWindowVisibleDisplayFrame(r)

            val heightDiff = viewDataBinding.root.rootView.height - (r.bottom - r.top)
            try {
                if (heightDiff < 500) {
                    viewDataBinding.nicknameEditText.clearFocus()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
    class CancelDialog : BaseNormalDialogFragment() {

        init {
            TITLE_MSG = "프로필 수정"
            CONTENT_MSG = "수정한 내용을 삭제하고 이전 프로필을 유지합니다."
            CANCEL_BUTTON_VISIBLE = true
            GRADIENT_BUTTON_VISIBLE = false
            CONFIRM_TEXT = "이전 프로필 유지"
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
                activity!!.onBackPressed()
            }
        }

    }




}