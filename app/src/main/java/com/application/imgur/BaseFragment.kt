package com.application.imgur

import android.app.Activity
import android.app.ProgressDialog
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    /**
     * Method to hide the soft keyboard from the UI.
     */
    fun hideSoftKeyboard() {

        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) {
            view = View(requireActivity())
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private var mProgressDialog: ProgressDialog? = null

    /**
     * Method to show progress dialog with given message.
     */
    fun showProgressDialog(message: String, title: String = "") {

        dismissProgressDialog()
        mProgressDialog = ProgressDialog.show(requireActivity(), title, message)
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.show()
    }

    /**
     * Method to dismiss the progress dialog in case if it exists.
     */
    fun dismissProgressDialog() {
        if (mProgressDialog != null) mProgressDialog!!.dismiss()
    }

    /**
     * Method to show toast.
     *
     * @param msg String message to show toast.
     */
    fun toast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    /**
     * Method to show toast from string resource Id.
     *
     * @param strResId String resource Id message to show toast.
     */
    fun toast(strResId: Int) = Toast.makeText(context, strResId, Toast.LENGTH_SHORT).show()
}