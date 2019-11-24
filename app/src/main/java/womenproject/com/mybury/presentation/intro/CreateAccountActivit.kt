package womenproject.com.mybury.presentation.intro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.fragment_base_dialog.*
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference.Companion.getNickname
import womenproject.com.mybury.data.Preference.Companion.setMyBuryLoginCompelete
import womenproject.com.mybury.databinding.ActivityCreateAccountBinding
import womenproject.com.mybury.databinding.SplashWithLoginBinding
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.mypage.BucketListByCategoryFragmentArgs
import womenproject.com.mybury.presentation.viewmodels.MainFragmentViewModel

class CreateAccountActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account)
        binding.nickname.setText(getNickname(this))
        binding.btn.setOnClickListener {
            setMyBuryLoginCompelete(this, true)
            val intent = Intent(MyBuryApplication.context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            MyBuryApplication.context.startActivity(intent)
            finish()
        }

    }
}

