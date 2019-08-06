package womenproject.com.mybury.viewmodels

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import womenproject.com.mybury.base.BaseViewModel

/**
 * Created by HanAYeon on 2018. 11. 30..
 */

class BucketDetailViewModel : BaseViewModel() {

    val lockText = "공개"
    val title = "우와 여기 존맛탱구리야 꼭 한번 더! 같이 갈 줄 알았지 혼자가는게 목표!"
    val memo = "명선,한아연,안예지는 김가은을 빼고 맛집탐험을 하러간다! 우리빼고 퇴사하다니! 배신자! 엉엉 가지마."
    val category = "맛집"
    val dday = "2019.09.11 (D-20)"

    var currentGoal = 10
    val totalGoal = 20

    val count = ObservableField("$currentGoal/$totalGoal")

    val buttonText  = ObservableField("${currentGoal}회 완료")

    var oneImageVisible = View.GONE
    var moreImageVisible = View.VISIBLE

    private fun changeCount() {
        currentGoal+=1
        count.set("$currentGoal/$totalGoal")
        buttonText.set("${currentGoal}회 완료")
    }


}