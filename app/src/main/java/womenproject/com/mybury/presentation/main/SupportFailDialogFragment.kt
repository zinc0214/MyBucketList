package womenproject.com.mybury.presentation.main

import android.app.ActionBar
import android.content.Intent
import android.net.Uri
import womenproject.com.mybury.BuildConfig
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.DialogSupportFailBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment


class SupportFailDialogFragment(private val token: String, private val errorCode: String) : BaseDialogFragment<DialogSupportFailBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.dialog_support_fail

    override fun onResume() {
        super.onResume()
        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialogFragmentWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
        dialog?.window?.setDimAmount(0.8F)
        dialog?.setCanceledOnTouchOutside(false)

    }

    override fun initDataBinding() {
        viewDataBinding.errorCode = errorCode
        //  viewDataBinding.goToEmailTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        viewDataBinding.goToEmailTextView.setOnClickListener { goToSendEmail() }
        viewDataBinding.closeButton.setOnClickListener { dismiss() }
    }

    private fun goToSendEmail() {
        val send = Intent(Intent.ACTION_SENDTO)
        val uriText = "mailto:" + Uri.encode("mybury.info@gmail.com") +
                "?subject=" + Uri.encode("< 마이버리 후원하기 결제 실패 문의 >") +
                "&body=" + Uri.encode("아래의 정보를 그대로 전송해주세요. \n\n ======================" +
                "\n\n 앱 버전 (AppVersion) + ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})" +
                "\n\n Purchase Token : $token \n\n ErrorCode : $errorCode" +
                "\n\n ===================")
        val uri = Uri.parse(uriText)

        send.data = uri
        startActivity(Intent.createChooser(send, "마이버리 후원 문의하기"))
    }
}
