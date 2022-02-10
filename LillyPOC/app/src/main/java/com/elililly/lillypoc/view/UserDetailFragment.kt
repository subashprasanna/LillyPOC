package com.elililly.lillypoc.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.elililly.lillypoc.R
import com.elililly.lillypoc.model.UserData
import com.elililly.lillypoc.model.isValidEmail
import com.elililly.lillypoc.viewmodel.ConfigurationViewModel
import kotlinx.android.synthetic.main.bottom_nav_view.btnNext
import kotlinx.android.synthetic.main.bottom_nav_view.btnPrevious
import kotlinx.android.synthetic.main.fragment_user_detail.editTextAddress
import kotlinx.android.synthetic.main.fragment_user_detail.editTextEmail
import kotlinx.android.synthetic.main.fragment_user_detail.editTextFirstName
import kotlinx.android.synthetic.main.fragment_user_detail.editTextLastName
import kotlinx.android.synthetic.main.fragment_user_detail.editTextPincode

/**
 * Author: Prasanna B
 * Modified Date: 10-Feb-2022
 *
 * This class is used to gather user details
 */
class UserDetailFragment: Fragment() {
    private lateinit var configureViewModel: ConfigurationViewModel
    private var userData: UserData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_detail, container, false)
        // show cancel button in toolbar
        val activity = (activity as MainActivity)
        activity.showCancelButton(showCancel = true)
        configureViewModel = ViewModelProviders.of(activity).get(ConfigurationViewModel::class.java)
        userData = configureViewModel.userData
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPageData()

        handleClickEvent()
    }

    private fun loadPageData() {
        userData?.let { user ->
            editTextFirstName.setText(user.firstName)
            editTextLastName.setText(user.lastName)
            editTextEmail.setText(user.email)
            editTextAddress.setText(user.address)
            editTextPincode.setText(user.pincode)
        }
    }

    private fun handleClickEvent() {
        btnPrevious.setOnClickListener{
            findNavController().popBackStack()
        }

        btnNext.setOnClickListener{
            validateInputs()
        }
    }

    private fun validateInputs() {
        val firstNameValue = editTextFirstName.text.toString()
        val lastNameValue = editTextLastName.text.toString()
        val emailValue = editTextEmail.text.toString()
        val addressValue = editTextAddress.text.toString()
        val pincodeValue = editTextPincode.text.toString()

        when {
            (firstNameValue.isEmpty()) -> {
                editTextFirstName.error = resources.getString(R.string.first_name_empty)
                editTextFirstName.requestFocus()
            }
            (lastNameValue.isEmpty()) -> {
                editTextLastName.error = resources.getString(R.string.last_name_empty)
                editTextLastName.requestFocus()
            }
            (emailValue.isEmpty()) -> {
                editTextEmail.error = resources.getString(R.string.email_empty)
                editTextEmail.requestFocus()
            }
            (!isValidEmail(emailValue)) -> {
                editTextEmail.error = resources.getString(R.string.invalid_email)
                editTextEmail.requestFocus()
            }
            (addressValue.isEmpty()) -> {
                editTextAddress.error = resources.getString(R.string.address_empty)
                editTextAddress.requestFocus()
            }
            (pincodeValue.isEmpty()) -> {
                editTextPincode.error = resources.getString(R.string.pincode_empty)
                editTextPincode.requestFocus()
            }
            else -> {
                configureViewModel.userData = UserData(firstNameValue, lastNameValue, emailValue, addressValue, pincodeValue)
                findNavController().navigate(R.id.action_userDetailFragment_to_setupOptionFragment)
            }
        }
    }
}