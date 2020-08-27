package com.bjornfree.tochka.loyalty.ui.catalog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.model.CategoryItem
import com.bjornfree.tochka.loyalty.model.FieldsItem
import com.bjornfree.tochka.loyalty.ui.dialogs.FilterSheetDialog
import java.util.HashMap


class FilterItemAdapter(
    private val filterFieldsList: List<FieldsItem>,
    private val context: Context,
    private val category: CategoryItem,
    private val productAdapter: CatalogProductsAdapter
) : androidx.recyclerview.widget.RecyclerView.Adapter<FilterItemAdapter.ViewHolder>(),
    FilterSheetDialog.ItemClickListener {

    private var filterEntityMap = HashMap<String, ArrayList<String>>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val v = inflater.inflate(R.layout.row_category_filter_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return filterFieldsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filterFieldsList[position]
        bind(holder, item)
    }

    private fun bind(holder: ViewHolder, item: FieldsItem) {
        holder.btnFilter.text = item.name
        holder.btnFilter.setOnClickListener {
                openFilterFragment(holder)
        }
    }

    private fun openFilterFragment(holder: ViewHolder) {
        val filterFragment = FilterSheetDialog().newInstance()
        filterFragment.setAcceptClickListener(this)

        val bundle = Bundle()
        bundle.putSerializable("field", filterFieldsList[holder.bindingAdapterPosition])
        bundle.putSerializable("categoryName", category.name)

        if (filterEntityMap.containsKey(filterFieldsList[holder.bindingAdapterPosition].field)) {
            bundle.putSerializable(
                "filterList",
                filterEntityMap[filterFieldsList[holder.bindingAdapterPosition].field]
            )
        }

        filterFragment.arguments = bundle
        filterFragment.setOnDismissListener(DialogInterface.OnDismissListener {
            if (!filterEntityMap[filterFieldsList[holder.bindingAdapterPosition].field].isNullOrEmpty()) {
                holder.btnFilter.setBackgroundColor(context.resources.getColor(R.color.darkred))
                holder.btnFilter.setTextColor(context.resources.getColor(R.color.white))
            } else {
                holder.btnFilter.setBackgroundColor(Color.WHITE)
                holder.btnFilter.setTextColor(context.resources.getColor(R.color.black))
            }
        }
        )
        filterFragment.show(
            (context as AppCompatActivity).supportFragmentManager,
            "filter_sheet_dialog"
        )
    }


    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener,
        View.OnLongClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        val btnFilter = itemView.findViewById(R.id.btn_filter) as Button
        val cardView = itemView.findViewById(R.id.cw_item_filter) as CardView


        override fun onClick(p0: View?) {

        }

        override fun onLongClick(p0: View?): Boolean {
            TODO("Not yet implemented")
        }
    }


    override fun onItemClick(filterItems: ArrayList<String>?, field: String?) {
        if (!field.isNullOrEmpty()) {
            if (!filterItems.isNullOrEmpty()) {
                if (filterEntityMap[field].isNullOrEmpty()) {

                }
                filterEntityMap[field] = filterItems
            } else {
                filterEntityMap.remove(field)
            }
        }
        productAdapter.setFilterCategoryList(filterItems, field)
    }
}