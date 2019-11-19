package womenproject.com.mybury.presentation.login

import android.graphics.Rect
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import womenproject.com.mybury.R
import womenproject.com.mybury.data.PreferenceData
import womenproject.com.mybury.data.PreferenceData.Companion.GOOGLE_AUTH
import womenproject.com.mybury.databinding.FragmentCreateAccountBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.mypage.profileedit.ProfileEditViewModel
import womenproject.com.mybury.presentation.write.AddContentType
import womenproject.com.mybury.presentation.write.WriteMemoImgAddDialogFragment
import java.io.File

class CreateAccountFragment : BaseFragment<FragmentCreateAccountBinding, ProfileEditViewModel>()  {

    override val layoutResourceId: Int
        get() = R.layout.fragment_create_account

    override val viewModel: ProfileEditViewModel
        get() = ProfileEditViewModel()


    override fun onResume() {
        super.onResume()

        if(PreferenceData.getString(context!!, GOOGLE_AUTH).isNotBlank()) {
            findNavController().navigate(R.id.action_create_to_main)
        }
    }


    override fun initDataBinding() {
        viewDataBinding.topLayout.title = "프로필생성"
        viewDataBinding.startMyburyBtn.setOnClickListener(myBuryStartListener)
        viewDataBinding.profileEditBtn.setOnClickListener(profileImageEditClickLister)
        viewDataBinding.nicknameEditText.addTextChangedListener(addTextChangedListener())
        viewDataBinding.root.viewTreeObserver.addOnGlobalLayoutListener(setOnSoftKeyboardChangedListener())
    }


    private val checkBaseProfileImgUsable: () -> Boolean = {
        true
    }
    private val baseProfileUseListener: () -> Unit = {
        Toast.makeText(activity, "기본 이미지 준비중, 찡긋", Toast.LENGTH_SHORT).show()
        viewDataBinding.profileImg.background = this.resources.getDrawable(R.drawable.app_info_icon)
    }

    private val checkAddImgAbleListener: () -> Boolean = {
        true
    }

    private val imgAddListener: (File, Uri) -> Unit = { file: File, uri: Uri ->
        Glide.with(this).load(uri).centerCrop().into(viewDataBinding.profileImg)
    }

    private val profileImageEditClickLister = View.OnClickListener {
        WriteMemoImgAddDialogFragment(AddContentType.PROFILE, checkBaseProfileImgUsable, baseProfileUseListener,
                checkAddImgAbleListener, imgAddListener).show(activity!!.supportFragmentManager, "tag")
    }


    private fun addTextChangedListener(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrBlank()) {
                    viewDataBinding.nicknameText.text = "닉네임을 입력해주세요."
                    viewDataBinding.startMyburyBtn.isEnabled = false
                } else {
                    viewDataBinding.nicknameEditText.setTextColor(resources.getColor(R.color._434343))
                    viewDataBinding.nicknameText.text = "${s}님 반가워요 :D"
                    viewDataBinding.startMyburyBtn.isEnabled = true
                }
            }

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


    private val myBuryStartListener = View.OnClickListener {
        PreferenceData.setString(context!!, GOOGLE_AUTH, viewDataBinding.nicknameEditText.text.toString())
        it.findNavController().navigate(R.id.action_create_to_main)
    }

}