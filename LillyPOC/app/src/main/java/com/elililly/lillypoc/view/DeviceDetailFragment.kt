package com.elililly.lillypoc.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.elililly.lillypoc.R
import com.elililly.lillypoc.model.DeviceData
import com.elililly.lillypoc.model.isBluetoothEnabled
import com.elililly.lillypoc.viewmodel.ConfigurationViewModel
import kotlinx.android.synthetic.main.bottom_nav_view.btnNext
import kotlinx.android.synthetic.main.bottom_nav_view.btnPrevious
import kotlinx.android.synthetic.main.fragment_device_detail.editTextDeviceName
import kotlinx.android.synthetic.main.fragment_device_detail.editTextDeviceSerialNo
import kotlinx.android.synthetic.main.fragment_device_detail.editTextDeviceVersion
import kotlinx.android.synthetic.main.fragment_device_detail.textViewSelectedDevice

/**
 * Author: Prasanna B
 * Modified Date: 10-Feb-2022
 *
 * This class is used to gather device data
 *
 * As per the requirement, user has to enter only these below values
 * DEVICE_SERIAL_NO = "AXVBP89"
 * DEVICE_VERSION = "2.4.5"
 * DEVICE_NAME = "Alpha"
 *
 * so condition is written to check exactly these values only
 * If value matches, user will proceed further
 * If value does not match, user will be shown alert message and redirected to setup page (3rd screen)
 *
 * User will be allowed to proceed further if bluetooth is on
 * If bluetooth is off, user will be redirected to welcome page by clearing all data
 */
class DeviceDetailFragment: Fragment() {
    private lateinit var configureViewModel: ConfigurationViewModel
    companion object {
        const val DEVICE_SERIAL_NO = "AXVBP89"
        const val DEVICE_VERSION = "2.4.5"
        const val DEVICE_NAME = "Alpha"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_device_detail, container, false)
        val activity = (activity as MainActivity)
        activity.showCancelButton(showCancel = true)
        configureViewModel = ViewModelProviders.of(activity).get(ConfigurationViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPageData()
        handleClickEvent()
    }

    private fun loadPageData() {
        configureViewModel.deviceData?.let {
            textViewSelectedDevice.text = configureViewModel.selectedSetupType
            editTextDeviceSerialNo.setText(it.deviceSerialNo)
            editTextDeviceVersion.setText(it.deviceVersion)
            editTextDeviceName.setText(it.deviceName)
        } ?: run {
            textViewSelectedDevice.text = configureViewModel.selectedSetupType
            editTextDeviceSerialNo.hint = "Enter $DEVICE_SERIAL_NO"
            editTextDeviceVersion.hint = "Enter $DEVICE_VERSION"
            editTextDeviceName.hint = "Enter $DEVICE_NAME"
        }
    }

    private fun handleClickEvent() {
        btnPrevious.setOnClickListener{
            findNavController().popBackStack()
        }

        btnNext.setOnClickListener{
            if (isBluetoothEnabled()) {
                val deviceSerialNo = editTextDeviceSerialNo.text.toString()
                val deviceVersion = editTextDeviceVersion.text.toString()
                val deviceName = editTextDeviceName.text.toString()

                when {
                    (deviceSerialNo.isEmpty()) -> {
                        editTextDeviceSerialNo.error = resources.getString(R.string.device_serial_no_empty)
                        editTextDeviceSerialNo.requestFocus()
                    }
                    (deviceVersion.isEmpty()) -> {
                        editTextDeviceVersion.error = resources.getString(R.string.device_version_empty)
                        editTextDeviceVersion.requestFocus()
                    }
                    (deviceName.isEmpty()) -> {
                        editTextDeviceName.error = resources.getString(R.string.device_name_empty)
                        editTextDeviceName.requestFocus()
                    }

                    (deviceSerialNo.toUpperCase() == DEVICE_SERIAL_NO
                            && deviceVersion == DEVICE_VERSION
                            && deviceName.toLowerCase() == DEVICE_NAME.toLowerCase())-> {
                        configureViewModel.deviceData =
                            DeviceData(configureViewModel.selectedSetupType, deviceSerialNo, deviceVersion, deviceName)
                        findNavController().navigate(R.id.action_deviceDetailFragment_to_successFragment)
                    }

                    else -> {
                        showErrorAlertDialog()
                    }
                }
            } else {
                Toast.makeText(context, resources.getString(R.string.enable_bluetooth_error), Toast.LENGTH_SHORT).show()
                configureViewModel.clearConfigurationData()
                findNavController().navigate(R.id.goToWelcomePageWithClearStack)
            }
        }
    }

    private fun showErrorAlertDialog() {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setMessage(resources.getString(R.string.invalid_device_data))
            .setCancelable(false)
            .setNegativeButton(resources.getString(R.string.dismiss)) { dialog, _ ->
                dialog.cancel()
                findNavController().navigate(R.id.action_deviceDetailFragment_to_setupOptionFragment)
            }

        val alert = dialogBuilder.create()
        alert.setTitle(resources.getString(R.string.alert))
        alert.show()
    }
}