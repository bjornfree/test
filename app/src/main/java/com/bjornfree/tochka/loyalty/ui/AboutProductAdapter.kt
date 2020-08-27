package com.bjornfree.tochka.loyalty.ui

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bjornfree.prices.utils.addChildFragment
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.model.Product
import com.bjornfree.tochka.loyalty.ui.AboutProductFragment
import com.bjornfree.tochka.loyalty.ui.home.MainActivity
import com.bjornfree.tochka.loyalty.utils.StrikeTextView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlin.random.Random


class AboutProductAdapter(
    private val productsList: List<Product>,
    private val context: Context,
    private val childFragmentManager: FragmentManager,
    private val isRecommended: Boolean
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<AboutProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val v = inflater.inflate(R.layout.product_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productsList[position]
        bind(holder, item)
    }

    private fun bind(holder: ViewHolder, item: Product) {
        Picasso.with(context)
            .load(Uri.parse("http://185.71.82.34:8182/Loyalty/Products/${item.art}.png"))
            .networkPolicy(NetworkPolicy.OFFLINE)
            .into(holder.imageView, object : Callback {
                override fun onSuccess() {}
                override fun onError() {
                    Picasso.with(context)
                        .load(Uri.parse("http://185.71.82.34:8182/Loyalty/Products/${item.art}.png"))
                        .into(holder.imageView)
                }
            })


        setRatingStars(item, holder)


        holder.tvType.text = item.type
        holder.tvCountry.text = item.country
        holder.tvName.text = item.name
        holder.tvPrice.text = item.price.toString().split(".")[0]
        holder.tvCent.text = ".${item.price.toString().split(".")[1]}  \u20bd"

        if (item.isEvent) {
            holder.tvOldPrice.text = item.oldPrice
            holder.tvPrice.setTextColor(context.resources.getColor(R.color.darkred))
            holder.tvCent.setTextColor(context.resources.getColor(R.color.darkred))
        } else {
            holder.tvOldPrice.text = ""
            holder.tvPrice.setTextColor(context.resources.getColor(R.color.black))
            holder.tvCent.setTextColor(context.resources.getColor(R.color.black))
        }


    }

    private fun setRatingStars(item: Product, holder: ViewHolder) {
        val rating = item.rating

        if (rating != null) {
            if (rating >= 1.0) {
                holder.iv_star1.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                holder.iv_star1.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }

            if (rating >= 2.0) {
                holder.iv_star2.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                holder.iv_star2.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }
            if (rating >= 3.0) {
                holder.iv_star3.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                holder.iv_star3.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }
            if (rating >= 4.0) {
                holder.iv_star4.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                holder.iv_star4.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }
            if (rating >= 5.0) {
                holder.iv_star5.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                holder.iv_star5.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }
        }

    }

    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener,
        View.OnLongClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        val tvType: TextView = itemView.findViewById(R.id.tv_type) as TextView
        val tvCountry = itemView.findViewById(R.id.tv_country1) as TextView
        val tvName = itemView.findViewById(R.id.tv_name) as TextView
        val tvPrice = itemView.findViewById(R.id.tv_price) as TextView
        val tvOldPrice = itemView.findViewById(R.id.tv_old_price) as StrikeTextView
        val tvCent = itemView.findViewById(R.id.tv_cent) as TextView
        val imageView = itemView.findViewById(R.id.imageView3) as ImageView

        val iv_star1 = itemView.findViewById(R.id.iv_star1) as ImageView
        val iv_star2 = itemView.findViewById(R.id.iv_star2) as ImageView
        val iv_star3 = itemView.findViewById(R.id.iv_star3) as ImageView
        val iv_star4 = itemView.findViewById(R.id.iv_star4) as ImageView
        val iv_star5 = itemView.findViewById(R.id.iv_star5) as ImageView


        override fun onClick(p0: View?) {
            val fragment = AboutProductFragment()
            val args = Bundle()
            args.putSerializable("product", productsList[bindingAdapterPosition])
            val container = if (isRecommended) {
                R.id.about_product_fragment_container
            } else {
                R.id.home_fragment_container
            }

            addChildFragment(childFragmentManager, fragment, args, container)
        }

        override fun onLongClick(p0: View?): Boolean {
            TODO("Not yet implemented")
        }

    }
}