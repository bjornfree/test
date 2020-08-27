package com.bjornfree.tochka.loyalty.utils

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet


class StrikeTextView : androidx.appcompat.widget.AppCompatTextView {
    private var dividerColor = 0
    private var paint: Paint? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        init(context)
    }

    private fun init(context: Context) {
        val resources = context.resources
        //replace with your color
        dividerColor = resources.getColor(R.color.holo_red_dark)
        paint = Paint()
        paint!!.color = dividerColor
        //replace with your desired width
        /*Modification*/
        //Instead of providing static width you can pass the width of textview itself like this
        paint!!.strokeWidth = this.width.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), 0f, paint!!)
    }
}