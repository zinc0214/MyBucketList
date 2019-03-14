package womenproject.com.mybury.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

/**
 * Created by HanAYeon on 2019. 3. 13..
 */



abstract class BaseDialogFragment<T : ViewDataBinding> : DialogFragment() {

    lateinit var viewDataBinding: T

    abstract val layoutResourceId : Int

    abstract fun initStartView()
    abstract fun initDataBinding()
    abstract fun initAfterBinding()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)

        dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCanceledOnTouchOutside(true)

        initStartView()
        initDataBinding()
        initAfterBinding()

        return viewDataBinding.root
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, "Tag")
    }
}

