package com.bjornfree.tochka.loyalty.ui.shops

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.model.City
import com.bjornfree.tochka.loyalty.model.Shop
import com.bjornfree.tochka.loyalty.ui.home.MainActivity
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlin.random.Random


class ExpandableStoreAdapter(groups: List<ExpandableGroupCity>, private val context : Context, private val childFragmentManager: FragmentManager) : ExpandableRecyclerViewAdapter<ExpandableStoreAdapter.CityViewHolder, ExpandableStoreAdapter.ShopViewHolder>(groups) {


    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.row_city,parent,false)
        return CityViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.row_shop,parent,false)
        return ShopViewHolder(view)
    }

    override fun onBindChildViewHolder(holder: ShopViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        val shop = group.items[childIndex] as Shop
        holder.bind(shop)

        holder.itemView.setOnClickListener {
            val fragment = AboutShopFragment()
            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)

            val args = Bundle()
            args.putSerializable("shop", shop)
            fragment.arguments = args

            transaction.add(R.id.child_shop_fragment_container, fragment, AboutShopFragment::class.simpleName).addToBackStack(AboutShopFragment::class.simpleName).commit()
        }
    }

    override fun onBindGroupViewHolder(holder: CityViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        val city = City(group.title,group.items as ArrayList<Shop>)
        holder.bind(city)
    }


    inner class CityViewHolder(itemView: View) : GroupViewHolder(itemView) {
        private var tvName : TextView = itemView.findViewById(R.id.tv_city_name)
        private var tvAmount : TextView = itemView.findViewById(R.id.tv_amount)

        fun bind(city : City){
            tvName.text = city.name
            tvAmount.text = "[${city.shops!!.count()}]"
        }
    }

    inner class ShopViewHolder(itemView: View) : ChildViewHolder(itemView) {
        private var tvName : TextView = itemView.findViewById(R.id.tv_shop_name)

        fun bind(shop : Shop){
            tvName.text = shop.name
        }
    }
}