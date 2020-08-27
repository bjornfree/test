package com.bjornfree.tochka.loyalty.ui.shops

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.ui.BaseFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_main_shop.*

class ShopMainFragment : BaseFragment(R.layout.fragment_main_shop) {
    private lateinit var adapter: FragmentStatePagerAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }


    private fun init() {
        tab_layout.addTab(tab_layout.newTab().setText("Список"))
        tab_layout.addTab(tab_layout.newTab().setText("Карта"))
        tab_layout.tabGravity = TabLayout.GRAVITY_FILL

        view_pager.offscreenPageLimit = 3
        view_pager.setSwipePagingEnabled(false)
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                view_pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })


        adapter = object : FragmentStatePagerAdapter(childFragmentManager,FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> ShopListFragment()
                    1 -> ShopMapFragment()
                    else -> ShopListFragment()
                }
            }

            override fun getCount(): Int {
                return 2
            }
        }

        view_pager.adapter = adapter
    }

}