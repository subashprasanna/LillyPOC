package com.elililly.lillypoc.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.elililly.lillypoc.R
import com.elililly.lillypoc.viewmodel.ConfigurationViewModel
import kotlinx.android.synthetic.main.activity_main.toolbar

/**
 * Author: Prasanna B
 * Modified Date: 10-Feb-2022
 *
 * This class contains the header, footer and container for fragments
 *
 * Wher user clicks cancel, all the data will be cleared and user will be redirected to welcome page
 */
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var cancelText: TextView
    private lateinit var configureViewModel: ConfigurationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolBar()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)

        configureViewModel = ViewModelProviders.of(this).get(ConfigurationViewModel::class.java)
    }

    private fun setupToolBar() {
        cancelText = toolbar.findViewById(R.id.txtCancel)
        cancelText.setOnClickListener {
            showAlertDialog()
        }
        setSupportActionBar(toolbar)
    }

    fun showCancelButton(showCancel: Boolean) {
        if (showCancel) {
            cancelText.visibility = View.VISIBLE
        } else {
            cancelText.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun showAlertDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(resources.getString(R.string.cancel_alert))
            .setCancelable(true)
            .setPositiveButton(resources.getString(R.string.proceed)) { _, _ ->
                configureViewModel.clearConfigurationData()
                navController.navigate(R.id.goToWelcomePageWithClearStack)
            }
            .setNegativeButton(resources.getString(R.string.dismiss)) { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle(resources.getString(R.string.alert))
        alert.show()
    }
}