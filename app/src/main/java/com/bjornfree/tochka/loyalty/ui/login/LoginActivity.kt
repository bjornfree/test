package com.bjornfree.tochka.loyalty.ui.login

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bjornfree.prices.utils.Coroutines
import com.bjornfree.prices.utils.defaultPrefs
import com.bjornfree.prices.utils.get
import com.bjornfree.prices.utils.put
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.ui.home.MainActivity
import com.rohitss.uceh.UCEHandler
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.delay
import kotlin.system.exitProcess

class LoginActivity() : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        UCEHandler.Builder(applicationContext)
            .addCommaSeparatedEmailAddresses("bjornfree@yandex.ru")
            .build()

        if(defaultPrefs(context = this).get("firstLaunch",true)){
            Coroutines.main {
                delay(400)
                val dialog = SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("Вам уже есть 18 лет ?")
                    .setContentText("")
                    .setConfirmText("Да")
                    .setCancelText("Нет")
                    .setConfirmClickListener {
                        Coroutines.main {
                            val options = ActivityOptions.makeSceneTransitionAnimation(this, imageView2, "imageStatus")
                            val bundle = options.toBundle()
                            window.exitTransition = null;

                            defaultPrefs(context = this).put("firstLaunch",false)

                            startActivity(Intent(this, MainActivity::class.java), bundle)
                            finish()
                        }
                    }
                    .setCancelClickListener {
                        exitProcess(0)
                    }


                val titleTextView =
                    dialog.findViewById<TextView>(cn.pedant.SweetAlert.R.id.content_text)
                val contentTextView =
                    dialog.findViewById<TextView>(cn.pedant.SweetAlert.R.id.content_text)

                val background =
                    dialog.findViewById<View>(cn.pedant.SweetAlert.R.id.custom_view_container)

                titleTextView?.let { it.setTextColor(Color.BLACK) }
                contentTextView?.let { it.setTextColor(Color.BLACK) }
                background?.let { it.setBackgroundColor(resources.getColor(R.color.float_transparent)) }
                dialog.show()

            }
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}