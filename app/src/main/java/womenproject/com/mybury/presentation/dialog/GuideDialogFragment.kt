package womenproject.com.mybury.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.DialogGuideBinding

class GuideDialogFragment : DialogFragment() {

    private lateinit var binding: DialogGuideBinding

    private var onExistingBucketClick: (() -> Unit)? = null
    private var onNewStartClick: (() -> Unit)? = null

    fun setButtonActions(
        onExistingBucketClick: () -> Unit,
        onNewStartClick: () -> Unit
    ) {
        this.onExistingBucketClick = onExistingBucketClick
        this.onNewStartClick = onNewStartClick
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_guide, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnExistingBucket.setOnClickListener {
            onExistingBucketClick?.invoke()
            dismiss()
        }

        binding.btnNewStart.setOnClickListener {
            onNewStartClick?.invoke()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            // 상태바 영역까지 확장
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            WindowCompat.setDecorFitsSystemWindows(this, false)
            WindowInsetsControllerCompat(this, decorView).isAppearanceLightStatusBars = false
        }
    }

    companion object {
        fun newInstance(): GuideDialogFragment = GuideDialogFragment()
    }
}
