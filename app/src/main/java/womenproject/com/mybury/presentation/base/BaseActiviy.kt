package womenproject.com.mybury.presentation.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by HanAYeon on 2018. 11. 26..
 */

@AndroidEntryPoint
open class BaseActiviy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}