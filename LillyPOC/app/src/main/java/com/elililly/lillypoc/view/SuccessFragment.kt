package com.elililly.lillypoc.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.elililly.lillypoc.R
import com.elililly.lillypoc.model.EMPTY
import com.elililly.lillypoc.model.isBluetoothEnabled
import com.elililly.lillypoc.viewmodel.ConfigurationViewModel
import java.lang.StringBuilder
import kotlinx.android.synthetic.main.bottom_nav_view.btnNext
import kotlinx.android.synthetic.main.bottom_nav_view.btnPrevious
import kotlinx.android.synthetic.main.fragment_success.saveDataProgressBar
import kotlinx.android.synthetic.main.fragment_success.textViewSuccessMessage

/**
 * Author: Prasanna B
 * Modified Date: 10-Feb-2022
 *
 * This class is the end configuration page
 * This class is used to store data all configuration data into local file
 *
 * User can store data / complete configuration only if bluetooth is on
 * If bluetooth is off, user will be redirected to welcome screen by clearing all data.
 */
class SuccessFragment: Fragment() {
    private lateinit var configureViewModel: ConfigurationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_success, container, false)
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
        btnNext.text = resources.getString(R.string.Finish)
    }

    private fun handleClickEvent() {
        btnPrevious.setOnClickListener{
            findNavController().popBackStack()
        }

        btnNext.setOnClickListener{
            if (isBluetoothEnabled()) {
                context?.let {
                    // construct data to save into local file
                    var data = StringBuilder()
                    configureViewModel.userData?.let {
                        data.append("First name : ${it.firstName}, ")
                            .append("Last name : ${it.lastName}, ")
                            .append("Email : ${it.email}, ")
                            .append("Address : ${it.address}, ")
                            .append("Pincode : ${it.pincode}, ")
                    }
                    data.append("Selected setup type : ${configureViewModel.selectedSetupType}, ")
                    data.append("Selected DIY date : ${configureViewModel.selectedDIYDate}, ")
                    configureViewModel.deviceData?.let {
                        data.append("Device serial number : ${it.deviceSerialNo}, ")
                        data.append("Device version : ${it.deviceVersion}, ")
                        data.append("Device name : ${it.deviceName} ")
                    }

                    // save data into local file
                    if (data.toString().isNotEmpty()) {
                        textViewSuccessMessage.visibility = View.GONE
                        saveDataProgressBar.visibility = View.VISIBLE

                        val status = configureViewModel.saveData(it, data.toString())

                        textViewSuccessMessage.visibility = View.VISIBLE
                        saveDataProgressBar.visibility = View.GONE

                        if (status) {
                            Toast.makeText(context, resources.getString(R.string.successfully_saved_data), Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, resources.getString(R.string.unable_to_save), Toast.LENGTH_LONG).show()
                        }
                    }

                    // navigate back to welcome screen
                    configureViewModel.clearConfigurationData()
                    findNavController().navigate(R.id.goToWelcomePageWithClearStack)
                }
            } else {
                Toast.makeText(context, resources.getString(R.string.enable_bluetooth_error), Toast.LENGTH_SHORT).show()
                configureViewModel.clearConfigurationData()
                findNavController().navigate(R.id.goToWelcomePageWithClearStack)
            }
        }
    }
}