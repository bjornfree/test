package com.bjornfree.prices.utils


import android.app.Dialog
import android.content.Context
import android.os.Environment
import android.view.Window
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ConsoleLog(private val msgNum: Int, private val fileName: String, private val mContext: Context) {

    private var msgPointer = 0
    private val messages: Array<String?>
    private val times: Array<Long?>
    private val pathFile: String = mContext.getExternalFilesDir(null).toString() + "/Prices/"
    private var dialog: Dialog? = null
    private val file: File


    private var lists = ArrayList<LogAddListener>()
    //Writer writer;

    private var cal = Calendar.getInstance()
    private var sdf = SimpleDateFormat("[kk:mm:ss]")

    val countStrings: Int
        get() {
            var count = 0
            val msgs = getMessages()
            var i = 0
            while (i < msgs.size && msgs[i] != null) {
                count++
                i++
            }
            return count
        }

    init {
        /*  val fileWorker = FileWorker(mContext)*/
        file = File(pathFile, fileName)

        messages = arrayOfNulls(msgNum)
        times = arrayOfNulls(msgNum)

        if (!file.exists()) {
            try {
                File(pathFile).mkdir()
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        /* val countStrings = fileWorker.getStringCount(file)*/

        if (countStrings > 500) {
            file.delete()
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun addOnAddListener(listn: LogAddListener) {
        lists.add(listn)
    }

    private fun getMessages(): Array<String?> {
        val strs = arrayOfNulls<String>(msgNum)

        val sdf = SimpleDateFormat("[kk:mm:ss]")

        var i = 0
        i = 0
        while (i < msgPointer) {

            cal.timeInMillis = times[i]!!
            val d = cal.time

            strs[i] = sdf.format(d) + messages[i]
            i++
        }

        if (messages[msgPointer] != null) {
            for (j in msgNum - 1 downTo msgPointer) {
                cal.timeInMillis = times[i]!!
                val d = cal.time

                strs[i++] = sdf.format(d) + messages[j]
            }
        }
        return strs
    }

    fun add(msg: String) {
        val ct = System.currentTimeMillis()

        messages[msgPointer] = msg
        times[msgPointer] = ct

        msgPointer++

        if (msgPointer == msgNum) msgPointer = 0

        cal.timeInMillis = ct
        val d = cal.time

        if (lists.size != 0) {
            for (lis in lists) lis.onAdd(sdf.format(d) + "  " + msg + " \n")
        }

        try {
            val writer = FileWriter(file, true)
            writer.write(sdf.format(d) + "  " + msg + "\n")
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    interface LogAddListener {
        fun onAdd(msg: String)
    }

    fun showConsoleLog(context: Context) {
        dialog = Dialog(context)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val sc = ScrollView(context)
        dialog!!.setContentView(sc)
        val dl = LinearLayout(context)
        dl.orientation = LinearLayout.VERTICAL
        sc.addView(dl)
        val msgs = getMessages()

        var i = 0
        while (i < msgs.size && msgs[i] != null) {
            val tv = TextView(context)
            tv.text = msgs[i]
            dl.addView(tv)
            i++
        }
        sc.fullScroll(ScrollView.FOCUS_DOWN)
        dialog!!.show()
    }

}