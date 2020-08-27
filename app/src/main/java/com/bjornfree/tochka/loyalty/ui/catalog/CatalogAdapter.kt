package com.bjornfree.tochka.loyalty.ui.catalog

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.model.CategoryItem
import com.bjornfree.tochka.loyalty.model.Product
import com.bjornfree.tochka.loyalty.ui.AboutProductFragment
import com.bjornfree.tochka.loyalty.ui.home.MainActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlin.random.Random


class CatalogAdapter(private val categoryList: List<CategoryItem>, private val context : Context, private val childFragmentManager : FragmentManager) : androidx.recyclerview.widget.RecyclerView.Adapter<CatalogAdapter.ViewHolder>() {

    private var sourceList : List<CategoryItem> = categoryList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val v = inflater.inflate(R.layout.row_category, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return sourceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = sourceList[position]
        bind(holder, item)
    }

    private fun bind(holder: ViewHolder, item: CategoryItem) {
        Picasso.with(context)
            .load(Uri.parse(item.image))
            .networkPolicy(NetworkPolicy.OFFLINE)
            .fit()
            .into(holder.imageView, object : Callback {
                override fun onSuccess() {}
                override fun onError() {
                    Picasso.with(context).load(Uri.parse(item.image))
                        .fit()
                        .into(holder.imageView)
                }
            })

        holder.tvName.text = item.name

    }

    fun setCategoryList (categoryList : List<CategoryItem>){
        sourceList = categoryList
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        val tvName = itemView.findViewById(R.id.tv_category_name) as TextView
        val imageView = itemView.findViewById(R.id.iv_background) as ImageView


        override fun onClick(p0: View?) {
            val fragment = CatalogProductsFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)

            val args = Bundle()
            args.putSerializable("category", sourceList[bindingAdapterPosition])
            fragment.arguments = args

            transaction.add(R.id.catalog_fragment_container, fragment).addToBackStack("catalogProductsFragment${Random.nextInt()}").commit()
        }

        override fun onLongClick(p0: View?): Boolean {
            TODO("Not yet implemented")
        }

    }
}