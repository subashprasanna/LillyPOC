package com.elililly.lillypoc.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.elililly.lillypoc.R
import com.elililly.lillypoc.viewmodel.ConfigurationViewModel
import kotlinx.android.synthetic.main.bottom_nav_view.btnNext
import kotlinx.android.synthetic.main.bottom_nav_view.btnPrevious
import kotlinx.android.synthetic.main.fragment_setup_option.radioButtonBGM
import kotlinx.android.synthetic.main.fragment_setup_option.radioButtonBLE
import kotlinx.android.synthetic.main.fragment_setup_option.radioGroupSetupOption

/**
 * Author: Prasanna B
 * Modified Date: 10-Feb-2022
 *
 * User can choose setup option (BGM Meter / BLE Pen) and proceed further
 */
class SetupOptionFragment: Fragment() {
    private lateinit var configureViewModel: ConfigurationViewModel

    companion object {
        const val BGM_METER = "BGM Meter"
        const val BLE_PEN = "BLE Pen"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setup_option, container, false)

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
        radioButtonBGM.text = BGM_METER
        radioButtonBLE.text = BLE_PEN

        if(configureViewModel.selectedSetupType.isNotEmpty()) {
            when(configureViewModel.selectedSetupType) {
                BGM_METER -> radioButtonBGM.isChecked = true
                BLE_PEN -> radioButtonBLE.isChecked = true
                else -> Unit
            }
        } else {
            radioButtonBGM.isChecked = false
            radioButtonBLE.isChecked = false
        }
    }

    private fun handleClickEvent() {
        btnPrevious.setOnClickListener{
            findNavController().popBackStack()
        }

        btnNext.setOnClickListener{
            val checkedViewId = radioGroupSetupOption.checkedRadioButtonId
            if (checkedViewId != -1) {
                val checkedViewText = radioGroupSetupOption.findViewById<RadioButton>(checkedViewId)
                configureViewModel.selectedSetupType = checkedViewText.text.toString()
                findNavController().navigate(R.id.action_setupOptionFragment_to_bluetoothSetupFragment)
            } else {
                Toast.makeText(context, resources.getString(R.string.choose_setup_option), Toast.LENGTH_SHORT).show()
            }
        }
    }
}