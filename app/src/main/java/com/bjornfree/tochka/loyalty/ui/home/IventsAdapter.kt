package com.bjornfree.tochka.loyalty.ui.home

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.youth.banner.adapter.BannerAdapter


class IventsAdapter(mDatas: List<String?>?, val context: Context) : BannerAdapter<String?, IventsAdapter.BannerViewHolder?>(mDatas) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        return BannerViewHolder(imageView)
    }


    override fun onBindView(holder: BannerViewHolder?, data: String?, position: Int, size: Int) {
        Picasso.with(context).load(data).fit().into(holder?.imageView)
    }

    inner class BannerViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView
        init {
            imageView = view
        }
    }

}