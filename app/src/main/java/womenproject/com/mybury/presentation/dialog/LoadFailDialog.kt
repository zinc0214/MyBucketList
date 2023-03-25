package womenproject.com.mybury.presentation.dialog

import android.view.View
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment

class LoadFailDialog(private val onConfirmClicked: (() -> Unit)? = null) : BaseNormalDialogFragment() {

    init {
        TITLE_MSG = "데이터 로드 실패"
        CONTENT_MSG = "데이터 로드에 실패했습니다.\n앱을 종료합니다."
        CANCEL_BUTTON_VISIBLE = false
        GRADIENT_BUTTON_VISIBLE = true
        CONFIRM_TEXT = "확인"
        CANCEL_ABLE = false
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            onConfirmClicked?.invoke()
            requireActivity().finish()
        }
    }

}