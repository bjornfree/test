package com.bjornfree.tochka.loyalty.ui.account

import android.os.Bundle
import android.widget.EditText
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.ui.BaseFragment
import com.bjornfree.tochka.loyalty.utils.PhoneMaskWatcher
import kotlinx.android.synthetic.main.fragment_registration.*
import java.lang.ref.WeakReference


class RegistrationFragment : BaseFragment(R.layout.fragment_registration) {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        et_phone_number.useEditText { editText ->
            val addLineNumberFormatter = PhoneMaskWatcher(WeakReference(editText))
            editText.addTextChangedListener(addLineNumberFormatter)
        }
    }
}