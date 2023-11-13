package com.cin.testfeatures.alarms.viewmodel

import androidx.lifecycle.ViewModel
import com.cin.testfeatures.alarms.model.AlarmModel

class TestAlarmsViewModel: ViewModel() {
    val listAlarms = mutableListOf<AlarmModel>()
}