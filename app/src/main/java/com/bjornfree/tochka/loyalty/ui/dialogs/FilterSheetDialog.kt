package com.bjornfree.tochka.loyalty.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.model.FieldsItem
import com.bjornfree.tochka.loyalty.ui.catalog.CatalogViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.sheet_dialog_filter_product.*
import java.util.*
import kotlin.collections.ArrayList


class FilterSheetDialog() : BottomSheetDialogFragment() {
    lateinit var field: FieldsItem
    lateinit var categoryName: String
    private lateinit var viewModel: CatalogViewModel
    private lateinit var listView: ListView
    private lateinit var cont: Context
    private var mListener: ItemClickListener? = null
    private lateinit var itemList: List<String>
    private var checkedList = ArrayList<String>()
    private var onDismissListener: DialogInterface.OnDismissListener? = null


    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.sheet_dialog_filter_product, container, false)
        listView = view.findViewById(R.id.lv_items)
        cont = requireContext()
        return view
    }

    fun newInstance(): FilterSheetDialog {
        return FilterSheetDialog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @SuppressLint("ResourceType")
    private fun init() {

        field = requireArguments().get("field") as FieldsItem
        categoryName = requireArguments().get("categoryName") as String
        if(requireArguments().get("filterList") != null) {
            checkedList = requireArguments().get("filterList") as ArrayList<String>
        }

        viewModel = ViewModelProvider(this).get(CatalogViewModel::class.java)

        viewModel.fieldsEntity.observe(viewLifecycleOwner, Observer {
            it?.let {
                itemList = ArrayList(it)
                (itemList as ArrayList<String>).sort()
                val adapter: ArrayAdapter<String> = ArrayAdapter(cont, android.R.layout.simple_list_item_multiple_choice, itemList)
                listView.adapter = adapter
                listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
                listView.isNestedScrollingEnabled = true
                adapter.notifyDataSetChanged()

                for (i in itemList){
                    if(checkedList.contains(i)){
                        listView.setItemChecked(itemList.indexOf(i),true)
                    }
                }

                checkedList.clear()
            }
        })

        if (field.name != "Сортировка") {
            viewModel.getSearchFieldEntity(field.field.toLowerCase(Locale.ROOT), categoryName)
        } else {
            val sortList = arrayListOf("По умолчанию", "Цена по возрастанию", "По популярности", "Самые обсуждаемые")
            itemList = arrayListOf("isExclusive", "price", "rating", "comments")
            val adapter: ArrayAdapter<String> = ArrayAdapter(cont, android.R.layout.simple_list_item_single_choice, sortList)
            listView.adapter = adapter
            listView.choiceMode = ListView.CHOICE_MODE_SINGLE
            listView.dividerHeight = 0
            listView.isNestedScrollingEnabled = true
            adapter.notifyDataSetChanged()

            for (i in itemList){
                if(checkedList.contains(i)){
                    listView.setItemChecked(itemList.indexOf(i),true)
                }
            }
            checkedList.clear()
        }

        dialog?.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheetInternal = d.findViewById<View>(R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheetInternal!!).state =
                BottomSheetBehavior.STATE_EXPANDED
        }

        btn_apply.setOnClickListener {
            val checked: SparseBooleanArray = listView.checkedItemPositions
            val size = checked.size() // number of name-value pairs in the array

            for (i in 0 until size) {
                val key = checked.keyAt(i)
                val value = checked[key]
                if (value) {
                    checkedList.add(itemList[key])
                }
            }

            mListener!!.onItemClick(checkedList, field.field)

            this.dismiss()
        }

    }

    fun setAcceptClickListener(listener: ItemClickListener){
        mListener = listener
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss(dialog)
    }

    interface ItemClickListener {
        fun onItemClick(filterItems: ArrayList<String>?, field: String?)
    }

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener) {
        this.onDismissListener = listener
    }


}