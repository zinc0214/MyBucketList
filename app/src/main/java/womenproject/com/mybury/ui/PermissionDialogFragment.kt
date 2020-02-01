package womenproject.com.mybury.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.presentation.base.BaseNormalDialogFragment

/**
 * Created by HanAYeon on 2019. 3. 6..
 */

class PermissionDialogFragment : BaseNormalDialogFragment() {

    init {
        TITLE_MSG = "권한알림"
        CONTENT_MSG = "권한을 설정해 주어야만 사용이 가능합니다."
        CANCEL_BUTTON_VISIBLE = true
        GRADIENT_BUTTON_VISIBLE = false
        CANCEL_TEXT = "취소"
        CONFIRM_TEXT = "설정"
    }

    override fun createOnClickConfirmListener(): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + MyBuryApplication.context.packageName))
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
