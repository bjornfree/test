package com.bjornfree.tochka.loyalty.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.model.Product
import com.bjornfree.tochka.loyalty.ui.home.MainViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_about_product.*

class AboutProductFragment : BaseFragment(R.layout.fragment_about_product) {
    private lateinit var viewModel: MainViewModel
    private lateinit var recomendedAdapterHome: AboutProductAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val product = requireArguments().getSerializable("product") as Product

        viewModel.productsLiveData.observe(viewLifecycleOwner, Observer {
            // Рекомендуем
            val recomendedProducts =
                it.filter { pr -> pr.country == product.country && pr.type == product.type }
            recomendedAdapterHome = AboutProductAdapter(
                recomendedProducts,
                this.requireContext(),
                childFragmentManager,
                true
            )
            rv_recomended.adapter = recomendedAdapterHome
            recomendedAdapterHome.notifyDataSetChanged()

        })

        viewModel.getProducts()

        Picasso.with(context)
            .load("http://185.71.82.34:8182/Loyalty/Products/${product.art}.png")
            .into(img_bottle)

        tv_name.text = product.name
        tv_type.text = product.type
        tv_country.text = product.country
        tv_price.text = product.price.toString().split(".")[0] + " "
        tv_cent.text = ".${product.price.toString().split(".")[1]}  \u20bd"
        tv_description.text = product.description
        tv_rate.text = "  (${product.rating})"

        if (product.isEvent) {
            tv_old_price.text = product.oldPrice
            tv_price.setTextColor(resources.getColor(R.color.darkred))
            tv_cent.setTextColor(resources.getColor(R.color.darkred))
        }

        setRatingStars(product)
    }

    private fun setRatingStars(product: Product) {
        val rating = product.rating

        if (rating != null) {
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
        }
    }
}