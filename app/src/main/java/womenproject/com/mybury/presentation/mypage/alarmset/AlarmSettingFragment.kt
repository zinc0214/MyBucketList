package womenproject.com.mybury.presentation.mypage.alarmset

import android.view.View
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.FragmentAlarmSettingBinding
import womenproject.com.mybury.databinding.FragmentAppInfoBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.mypage.appinfo.AppInfoViewModel

class AlarmSettingFragment :  BaseFragment<FragmentAlarmSettingBinding, BaseViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_alarm_setting

    override val viewModel: BaseViewModel
        get() = BaseViewModel()


    override fun initDataBinding() {
        viewDataBinding.backLayout.title = "알림"
        viewDataBinding.ddayAlram.title = "D-Day 알림"
        viewDataBinding.ddayAlram.content = "D-DAY가 다가온 버킷리스트가 있을 경우 알림 수신"

        viewDataBinding.eventAlarm.title = "이벤트, 업데이트 알림"
        viewDataBinding.eventAlarm.content = "진행 중인 이벤트 및 신규 업데이트 내역이 있을 경우 알림 수신"

        viewDataBinding.backLayout.backBtnOnClickListener = backBtnOnClickListener()
    }


}