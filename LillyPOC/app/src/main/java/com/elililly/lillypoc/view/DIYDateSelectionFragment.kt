package com.elililly.lillypoc.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.elililly.lillypoc.R
import com.elililly.lillypoc.model.isBluetoothEnabled
import com.elililly.lillypoc.viewmodel.ConfigurationViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.android.synthetic.main.bottom_nav_view.btnNext
import kotlinx.android.synthetic.main.bottom_nav_view.btnPrevious
import kotlinx.android.synthetic.main.fragment_diy_date_selection.buttonChooseDIYDate
import kotlinx.android.synthetic.main.fragment_diy_date_selection.textViewDIYDate

/**
 * Author: Prasanna B
 * Modified Date: 10-Feb-2022
 *
 * This class is used to get DIY date
 *
 * User will be allowed to proceed further if bluetooth is on
 * If bluetooth is off, user will be redirected to welcome page by clearing all data
 */
class DIYDateSelectionFragment: Fragment() {

    private var cal = Calendar.getInstance()
    private lateinit var configureViewModel: ConfigurationViewModel

    companion object {
        const val DATE_FORMAT = "dd/MM/yyyy"
        const val EMPTY_DATE = "--"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diy_date_selection, container, false)
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
        if (configureViewModel.selectedDIYDate.isNotEmpty()) {
            textViewDIYDate.text = configureViewModel.selectedDIYDate
        } else {
            textViewDIYDate.text = EMPTY_DATE
        }
    }

    private fun handleClickEvent() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        context?.let { context ->
            buttonChooseDIYDate.setOnClickListener {
                DatePickerDialog(context,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        btnPrevious.setOnClickListener{
            findNavController().popBackStack()
        }

        btnNext.setOnClickListener{
            if (isBluetoothEnabled()) {
                val dateValue = textViewDIYDate.text.toString()
                if (dateValue == EMPTY_DATE) {
                    Toast.makeText(context, resources.getString(R.string.diy_date_empty), Toast.LENGTH_SHORT).show()
                } else {
                    configureViewModel.selectedDIYDate = dateValue
                    findNavController().navigate(R.id.action_DIYDateSelectionFragment_to_deviceDetailFragment)
                }
            } else {
                Toast.makeText(context, resources.getString(R.string.enable_bluetooth_error), Toast.LENGTH_SHORT).show()
                configureViewModel.clearConfigurationData()
                findNavController().navigate(R.id.goToWelcomePageWithClearStack)
            }
        }
    }

    private fun updateDateInView() {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        textViewDIYDate.text = sdf.format(cal.time)
    }
}