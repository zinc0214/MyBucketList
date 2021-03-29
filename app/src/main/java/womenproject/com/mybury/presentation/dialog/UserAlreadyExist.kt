package womenproject.com.mybury.presentation.dialog

import android.view.View
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment

class UserAlreadyExist : BaseNormalDialogFragment() {
    init {
        TITLE_MSG = "로그인 불가"
        CONTENT_MSG = "이미 생성되어있는 계정입니다."
        CANCEL_BUTTON_VISIBLE = false
        GRADIENT_BUTTON_VISIBLE = true
        CONFIRM_TEXT = "확인"
        CANCEL_ABLE = false
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            dismiss()
            requireActivity().finish()
        }
    }
}