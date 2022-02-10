package com.elililly.lillypoc.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.elililly.lillypoc.R
import com.elililly.lillypoc.model.isBluetoothEnabled
import kotlinx.android.synthetic.main.bottom_nav_view.btnNext
import kotlinx.android.synthetic.main.bottom_nav_view.btnPrevious

/**
 * Author: Prasanna B
 * Modified Date: 10-Feb-2022
 *
 * This class is used to setup the bluetooth
 * User can complete the configuration process only if they enable bluetooth
 * If bluetooth is enabled in phone setting, user can proceed further
 * If bluetooth is not enabled in phone setting, user can not proceed further
 */
class BluetoothSetupFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bluetooth_setup, container, false)
        (activity as MainActivity).showCancelButton(showCancel = true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnPrevious.setOnClickListener{
            findNavController().popBackStack()
        }

        btnNext.setOnClickListener{
            if (isBluetoothEnabled()) {
                findNavController().navigate(R.id.action_bluetoothSetupFragment_to_DIYDateSelectionFragment)
            } else {
                /**
                 * Note: As per requirement, if bluetooth is not enabled, alert dialog should be shown to user.
                 * So, Run time permission for bluetooth not done here.
                 */
                showAlertDialog()
            }
        }
    }

    private fun showAlertDialog() {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setMessage(resources.getString(R.string.enable_bluetooth_error))
            .setCancelable(true)
            .setNegativeButton(resources.getString(R.string.dismiss)) { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle(resources.getString(R.string.alert))
        alert.show()
    }
}