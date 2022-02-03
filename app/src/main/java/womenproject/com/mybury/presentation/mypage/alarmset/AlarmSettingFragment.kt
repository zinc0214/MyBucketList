package womenproject.com.mybury.presentation.mypage.alarmset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.FragmentAlarmSettingBinding
import womenproject.com.mybury.presentation.base.BaseFragment

@AndroidEntryPoint
class AlarmSettingFragment : BaseFragment() {
    private lateinit var binding: FragmentAlarmSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_setting, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        binding.backLayout.title = "알림"
        binding.ddayAlram.title = "D-Day 알림"
        binding.ddayAlram.content = "D-DAY가 다가온 버킷리스트가 있을 경우 알림 수신"

        binding.eventAlarm.title = "이벤트, 업데이트 알림"
        binding.eventAlarm.content = "진행 중인 이벤트 및 신규 업데이트 내역이 있을 경우 알림 수신"

        binding.backLayout.backBtnOnClickListener = backBtnOnClickListener()
    }
}