package com.elililly.lillypoc.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.elililly.lillypoc.R
import kotlinx.android.synthetic.main.bottom_nav_view.btnNext
import kotlinx.android.synthetic.main.bottom_nav_view.btnPrevious

/**
 * Author: Prasanna B
 * Modified Date: 10-Feb-2022
 *
 * This is the first configuration class
 * Is is the welcome screen and it helps user to start the configuration process.
 */
class WelcomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        (activity as MainActivity).showCancelButton(showCancel = false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnPrevious.visibility = View.GONE

        btnNext.setOnClickListener{
            findNavController().navigate(R.id.action_welcomeFragment_to_userDetailFragment)
        }
    }
}