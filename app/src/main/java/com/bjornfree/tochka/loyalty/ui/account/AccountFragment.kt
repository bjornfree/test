package com.bjornfree.tochka.loyalty.ui.account

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bjornfree.prices.utils.addChildFragment
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.ui.BaseFragment
import com.bjornfree.tochka.loyalty.ui.shops.ShopViewModel
import kotlinx.android.synthetic.main.fragment_account.*
import org.angmarch.views.OnSpinnerItemSelectedListener


class AccountFragment : BaseFragment(R.layout.fragment_account) {
    private lateinit var viewModel: ShopViewModel
    private lateinit var cityName: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ShopViewModel::class.java)

        viewModel.citiesLiveData.observe(viewLifecycleOwner, Observer {
            val listCity = ArrayList<String>()
            for (item in it) {
                listCity.add(item.title)
            }
            sp_city.attachDataSource(listCity)
            sp_city.setArrowTintColor(resources.getColor(R.color.colorPrimaryDark))
            sp_city.onSpinnerItemSelectedListener = OnSpinnerItemSelectedListener { parent, view, position, id ->
                    cityName = parent.getItemAtPosition(position) as String
                }
            cityName = sp_city.selectedItem.toString()
        })

        btn_start.setOnClickListener {
            val fragment = RegistrationFragment()
            val bundle = Bundle()
            bundle.putSerializable("cityName", cityName)
            val container = R.id.account_fragment_container
            addChildFragment(childFragmentManager,fragment, bundle, container)
        }
    }
}