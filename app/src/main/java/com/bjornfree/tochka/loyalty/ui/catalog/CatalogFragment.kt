package com.bjornfree.tochka.loyalty.ui.catalog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bjornfree.prices.utils.changeFragment
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.model.Category
import com.bjornfree.tochka.loyalty.ui.AboutProductFragment
import com.bjornfree.tochka.loyalty.ui.BaseFragment
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_catalog.*


class CatalogFragment : BaseFragment(R.layout.fragment_catalog) {
    private lateinit var viewModel: CatalogViewModel
    private lateinit var catalogAdapter: CatalogAdapter
    private lateinit var catalogProductsAdapter: CatalogProductsAdapter


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(CatalogViewModel::class.java)
        viewModel.categoriesLiveData.observe(viewLifecycleOwner, Observer {
            for (category in it) {
                addCategoryButton(category)
            }

            catalogAdapter = it[0].categoryItems?.let { it1 ->
                CatalogAdapter(it1, requireContext(), childFragmentManager)
            }!!
            rv_category.adapter = catalogAdapter
            catalogAdapter.notifyDataSetChanged()
        })

        viewModel.productArt.observe(viewLifecycleOwner, Observer {
            val aboutProductFragment = AboutProductFragment()
            val args = Bundle()
            args.putSerializable("product", it)
            aboutProductFragment.arguments = args

            changeFragment(
                childFragmentManager,
                aboutProductFragment,
                "searchProduct",
                R.id.catalog_fragment_container
            )
        })

        viewModel.productList.observe(viewLifecycleOwner, Observer {
            catalogProductsAdapter = CatalogProductsAdapter(it, requireContext(), childFragmentManager, true, null)
            rv_category.adapter = catalogProductsAdapter
            catalogProductsAdapter.notifyDataSetChanged()
        })


        et_search.addTextChangedListener(object : TextWatcher {
            val handler = Handler()
            var runnable: Runnable? = null
            override fun afterTextChanged(p0: Editable?) {
                runnable = Runnable {
                    if (p0.toString().isNotEmpty() && p0.toString().length >= 3 && p0.toString().replace(" ","").isNotEmpty()) {
                        viewModel.searchByName(p0.toString())
                    } else {
                        rv_category.adapter = catalogAdapter
                        catalogAdapter.notifyDataSetChanged()
                    }
                }
                handler.postDelayed(runnable, 500)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                handler.removeCallbacks(runnable);
            }

        })
        et_search.setOnKeyListener { v, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_BACK -> {
                        val inputManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.hideSoftInputFromWindow(requireView().windowToken, 0)
                    }

                    KeyEvent.KEYCODE_ENTER -> {
                        val inputManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.hideSoftInputFromWindow(requireView().windowToken, 0)
                        et_search.clearFocus()
                    }
                }
            }
            false
        }

        fb_scan_mark.setOnClickListener {
            scanActivity(requireActivity())
        }
    }

    private fun addCategoryButton(category: Category) {
        val cardView = CardView(requireContext())
        val radius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            10f,
            requireContext().resources.displayMetrics
        )
        cardView.cardElevation = 2f
        cardView.radius = radius

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        params.marginStart = 5
        params.marginEnd = 5
        cardView.layoutParams = params

        val btnCategory = Button(context)
        btnCategory.typeface = Typeface.create("fonts/arno_pro.ttf", Typeface.ITALIC)
        btnCategory.setTextColor(resources.getColor(R.color.darkred))
        btnCategory.setBackgroundColor(resources.getColor(R.color.white))
        btnCategory.gravity = Gravity.CENTER
        btnCategory.textSize = 12f
        btnCategory.text = category.typeName
        btnCategory.setOnClickListener {
            if (et_search.text.isNotEmpty()) {
                et_search.setText("")
            }
            category.categoryItems?.let { it1 -> catalogAdapter.setCategoryList(it1) }
        }
        cardView.addView(btnCategory)
        ll_category.addView(cardView)
    }

    private fun scanActivity(activity: Activity) {
        val integrator = IntentIntegrator(activity)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES)
        integrator.setCameraId(0)
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(false)
        integrator.setOrientationLocked(false)

        val intent = integrator.createScanIntent()
        startActivityForResult(intent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val mResult = result.contents
                viewModel.getProductByBarcode(mResult)
            } else {
                Toast.makeText(context, "Товар по штрих коду не найден", Toast.LENGTH_LONG).show()
            }
        }
    }


}