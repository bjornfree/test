package com.bjornfree.tochka.loyalty.ui.shops

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_list_shop.*


class ShopListFragment : BaseFragment(R.layout.fragment_list_shop){
    private lateinit var viewModel: ShopViewModel
    private lateinit var adapter : ExpandableStoreAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        rv_cities.layoutManager = LinearLayoutManager(context)

        viewModel.citiesLiveData.observe(viewLifecycleOwner, Observer {
            adapter = ExpandableStoreAdapter(it as List<ExpandableGroupCity>, requireContext(), childFragmentManager)
            rv_cities.adapter = adapter
            adapter.notifyDataSetChanged()
        })
    }


}