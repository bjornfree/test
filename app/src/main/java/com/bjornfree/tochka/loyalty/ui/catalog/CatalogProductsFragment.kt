package com.bjornfree.tochka.loyalty.ui.catalog

import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.data.repository.Repository
import com.bjornfree.tochka.loyalty.model.CategoryItem
import com.bjornfree.tochka.loyalty.model.FieldsItem
import com.bjornfree.tochka.loyalty.model.Product
import com.bjornfree.tochka.loyalty.ui.BaseFragment
import com.bjornfree.tochka.loyalty.ui.dialogs.FilterSheetDialog
import com.innovattic.rangeseekbar.RangeSeekBar
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.fragment_catalog.*
import kotlinx.android.synthetic.main.fragment_products_catalog.*

class CatalogProductsFragment : BaseFragment(R.layout.fragment_products_catalog) {
    private lateinit var viewModel: CatalogViewModel
    private lateinit var productsAdapter: CatalogProductsAdapter
    private lateinit var productList: List<Product>
    private lateinit var filterItemAdapter: FilterItemAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val repository = Repository(requireContext())
        val category = requireArguments().getSerializable("category") as CategoryItem

        Picasso.with(context)
            .load(Uri.parse(category.image))
            .networkPolicy(NetworkPolicy.OFFLINE)
            .into(img_header, object : Callback {
                override fun onSuccess() {}
                override fun onError() {
                    Picasso.with(context).load(Uri.parse(category.image))
                        .into(img_header)
                }
            })

        viewModel = ViewModelProvider(this).get(CatalogViewModel::class.java)
        viewModel.productList.observe(viewLifecycleOwner, Observer {
            it?.let {
                productList = it
                productsAdapter = CatalogProductsAdapter(it.sortedBy { product -> product.price }, requireContext(), childFragmentManager, false,repository)

                category.fields?.let {
                    // Добавляем сортировку в список фильтров
                    val listCategoryItem = it as ArrayList
                    listCategoryItem.add(0, FieldsItem("sort", "Сортировка"))

                    filterItemAdapter = FilterItemAdapter(listCategoryItem,requireContext(), category, productsAdapter)
                    rv_filters.adapter = filterItemAdapter
                    filterItemAdapter.notifyDataSetChanged()
                }

            }!!

            rv_products_catalog.adapter = ScaleInAnimationAdapter(productsAdapter).apply {
                setFirstOnly(false)
            }
            productsAdapter.notifyDataSetChanged()

            rangeSeekBar.seekBarChangeListener = object : RangeSeekBar.SeekBarChangeListener {
                var max = 0
                var min = 0

                override fun onStartedSeeking() {
                }

                override fun onStoppedSeeking() {
                    productsAdapter.setProductList(productsAdapter.getCurrentProductList().filter { product -> product.price!! >= min && product.price <= max }
                        .sortedBy { product -> product.price?.let { product.price } })
                    rv_products_catalog.scrollToPosition(0)
                }

                override fun onValueChanged(minThumbValue: Int, maxThumbValue: Int) {
                    max = maxThumbValue
                    min = minThumbValue

                    tv_start_value.text = minThumbValue.toString()
                    tv_end_value.text = maxThumbValue.toString()
                }
            }

            val productWithHighestPrice = it.maxBy { product -> product.price!! }
            val productWithLowerPrices = it.minBy { product -> product.price!! }

            productWithHighestPrice?.let {
                rangeSeekBar.max = productWithHighestPrice.price!!.toInt()
                rangeSeekBar.setMaxThumbValue(productWithHighestPrice.price.toInt())
                tv_end_value.text = productWithHighestPrice.price.toInt().toString()
            }

            productWithLowerPrices?.let {
                rangeSeekBar.minRange = productWithLowerPrices.price!!.toInt()
                rangeSeekBar.setMinThumbValue(productWithLowerPrices.price.toInt())
                tv_start_value.text = productWithLowerPrices.price.toInt().toString()
            }

        })


        viewModel.getProductsByCategoryName(category.name)

    }

}