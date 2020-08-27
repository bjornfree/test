package com.bjornfree.tochka.loyalty.ui.shops

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.model.Product
import com.bjornfree.tochka.loyalty.model.Shop
import com.bjornfree.tochka.loyalty.ui.BaseFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_about_product.iv_star1
import kotlinx.android.synthetic.main.fragment_about_product.iv_star2
import kotlinx.android.synthetic.main.fragment_about_product.iv_star3
import kotlinx.android.synthetic.main.fragment_about_product.iv_star4
import kotlinx.android.synthetic.main.fragment_about_product.iv_star5
import kotlinx.android.synthetic.main.fragment_about_shop.*


class AboutShopFragment : BaseFragment(R.layout.fragment_about_shop) {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val shop = requireArguments().getSerializable("shop") as Shop

        tv_address.text = shop.address
        tv_schedule.text = shop.schedule
        tv_station.text = shop.busStop
        btn_route.setOnClickListener {
            val uri = "geo:${shop.latitude},${shop.longitude}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            requireContext().startActivity(intent)
        }

        Picasso.with(context)
            .load(R.drawable.about_store_logo)
            .into(img_logo)


        if (shop.image.isNotEmpty()) {
            Picasso.with(context)
                .load(shop.image)
                .fit()
                .into(iv_photo)
        }


    }
/*
    private fun setRatingStars(product: Product) {
            val rating = product.rating.toDouble()

            if (rating >= 1.0) {
                iv_star1.setImageDrawable(
                    requireContext().resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                iv_star1.setImageDrawable(
                    requireContext().resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }
            if (rating >= 2.0) {
                iv_star2.setImageDrawable(
                    requireContext().resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                iv_star2.setImageDrawable(
                    requireContext().resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }
            if (rating >= 3.0) {
                iv_star3.setImageDrawable(
                    requireContext().resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                iv_star3.setImageDrawable(
                    requireContext().resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }
            if (rating >= 4.0) {
                iv_star4.setImageDrawable(
                    requireContext().resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                iv_star4.setImageDrawable(
                    requireContext().resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }
            if (rating >= 5.0) {
                iv_star5.setImageDrawable(
                    requireContext().resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                iv_star5.setImageDrawable(
                    requireContext().resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }
    }*/


}