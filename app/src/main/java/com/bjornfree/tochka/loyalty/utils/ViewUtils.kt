package com.bjornfree.prices.utils

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.ui.account.RegistrationFragment
import com.google.android.material.snackbar.Snackbar
import com.sdsmdg.tastytoast.TastyToast
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun Context.toast(message: String, type: Int) {
    Coroutines.main {
        TastyToast.makeText(this, message, Toast.LENGTH_SHORT, type).show()
    }
}

fun ProgressBar.show() {
    Coroutines.main {
        this.visibility = View.VISIBLE
    }
}

fun ProgressBar.hide() {
    Coroutines.main {
        this.visibility = View.GONE
    }
}

fun String.formatDateToTextView(): String {
    val sdfDate = SimpleDateFormat("dd.MM.yyyy")
    val sdfParseDate = SimpleDateFormat("yyyy-MM-dd")
    return sdfDate.format(sdfParseDate.parse(this)).toString()
}

fun String.formatDateToRequest(): String {
    return this.replace("-", "")
}

fun View.snackbar(message: String) {
    Coroutines.main {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
            snackbar.setAction("ОК") {
                snackbar.dismiss()
            }
        }.show()
    }
}

fun View.snackbar(errorMessage: String, btnMessage: String, listener: View.OnClickListener) {
    Coroutines.main {
        Snackbar.make(this, errorMessage, Snackbar.LENGTH_LONG).also { snackbar ->
            snackbar.setAction(btnMessage, listener)
        }.show()
    }
}

fun Double.formatMoney(): String {
    val formatter: DecimalFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE) as DecimalFormat
    val symbols: DecimalFormatSymbols = formatter.decimalFormatSymbols
    symbols.currencySymbol = "" // Don't use null.

    formatter.decimalFormatSymbols = symbols

    return formatter.format(this)
}

fun changeFragment(fragmentManager: FragmentManager, fragment: Fragment, tagFragmentName: String?, container: Int) {
    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
    val currentFragment: Fragment? = fragmentManager.primaryNavigationFragment
    if (currentFragment != null) {
        fragmentTransaction.hide(currentFragment)
    }
    var fragmentTemp: Fragment? = fragmentManager.findFragmentByTag(tagFragmentName)
    if (fragmentTemp == null) {
        fragmentTemp = fragment
        fragmentTransaction.add(container, fragmentTemp, tagFragmentName)
    } else {
        fragmentTransaction.show(fragmentTemp)
    }
    fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp)
    fragmentTransaction.setReorderingAllowed(true)
    fragmentTransaction.commitNowAllowingStateLoss()
}

fun addChildFragment(childFragmentManager : FragmentManager,fragment: Fragment, bundle: Bundle, container: Int) {
    val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
    fragment.arguments = bundle
    transaction.add(container, fragment, RegistrationFragment::class.simpleName).addToBackStack(
        RegistrationFragment::class.simpleName).commit()
}