package com.bjornfree.tochka.loyalty.ui.dialogs

import android.app.Activity
import android.app.ActivityOptions
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.ui.home.MainActivity
import kotlinx.android.synthetic.main.dialog_ask_age.*
import pl.droidsonroids.gif.GifImageView
import kotlin.system.exitProcess

class AskAgeDialog(context: Context, private val imageView: GifImageView) : Dialog(context) {
    private val mContext = context
    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_ask_age)


        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btn_yes.setOnClickListener {
            val options =  ActivityOptions.makeSceneTransitionAnimation(mContext as Activity, imageView, "imageStatus")
            val bundle = options.toBundle()
            context.startActivity(Intent(context,MainActivity::class.java),bundle)
        }

        btn_no.setOnClickListener {
            exitProcess(0)
        }



    }


}