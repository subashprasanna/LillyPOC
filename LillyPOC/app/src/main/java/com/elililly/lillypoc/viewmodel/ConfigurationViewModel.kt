package com.elililly.lillypoc.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.elililly.lillypoc.model.DeviceData
import com.elililly.lillypoc.model.EMPTY
import com.elililly.lillypoc.model.UserData
import com.elililly.lillypoc.model.saveDataIntoFile

class ConfigurationViewModel: ViewModel() {
    var userData: UserData? = null
    var selectedSetupType: String = EMPTY
    var selectedDIYDate: String = EMPTY
    var deviceData: DeviceData? = null

    fun clearConfigurationData() {
        userData = null
        selectedSetupType = EMPTY
        selectedDIYDate = EMPTY
        deviceData = null
    }

    fun saveData(context: Context, data: String) = saveDataIntoFile(context, data)
}