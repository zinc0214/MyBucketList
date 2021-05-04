package womenproject.com.mybury.presentation.dialog

import android.view.View
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment

class CanNotGoMainDialog : BaseNormalDialogFragment() {
    init {
        TITLE_MSG = "로그인 불가"
        CONTENT_MSG = "로그인에 실패했습니다. 다시 시도해주세요."
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
