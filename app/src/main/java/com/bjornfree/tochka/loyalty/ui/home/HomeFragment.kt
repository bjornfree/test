package com.bjornfree.tochka.loyalty.ui.home

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.ui.AboutProductAdapter
import com.bjornfree.tochka.loyalty.ui.BaseFragment
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private lateinit var viewModel: MainViewModel
    private lateinit var newsAdapterHome: AboutProductAdapter
    private lateinit var exclusiveAdapterHome: AboutProductAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.imagesLiveData.observe(viewLifecycleOwner, Observer {
            initBanner(it as ArrayList<String>)
        })


        viewModel.productsLiveData.observe(viewLifecycleOwner, Observer {
            // Новинки
            val newsProducts = it.filter { product -> product.isNew }
            newsAdapterHome = AboutProductAdapter(newsProducts, this.requireContext(), childFragmentManager, false)
            rv_news.adapter = newsAdapterHome
            newsAdapterHome.notifyDataSetChanged()

            // Эксклюзивы
            val exclusiveProducts = it.filter { product -> product.isExclusive }
            exclusiveAdapterHome = AboutProductAdapter(exclusiveProducts, this.requireContext(),childFragmentManager, false)
            rv_exclusive.adapter = exclusiveAdapterHome
            exclusiveAdapterHome.notifyDataSetChanged()

        })
    }


    private fun initBanner(images: ArrayList<String>) {
        banner_ivents.adapter = IventsAdapter(images, requireContext())
        banner_ivents.indicator = CircleIndicator(context)
        banner_ivents.setIndicatorSelectedColorRes(R.color.red)
        banner_ivents.setIndicatorNormalColorRes(R.color.white)
        banner_ivents.setDelayTime(5000)
        banner_ivents.setIndicatorGravity(IndicatorConfig.Direction.CENTER)
        banner_ivents.setIndicatorWidth(10, 20)
        banner_ivents.start()
    }

}