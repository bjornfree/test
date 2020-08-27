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
import androidx.sqlite.db.SimpleSQLiteQuery
import com.bjornfree.prices.utils.Coroutines
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.data.repository.Repository
import com.bjornfree.tochka.loyalty.model.Product
import com.bjornfree.tochka.loyalty.ui.AboutProductFragment
import com.bjornfree.tochka.loyalty.utils.StrikeTextView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class CatalogProductsAdapter(
    private var productsList: List<Product>,
    private val context: Context,
    private val childFragmentManager: FragmentManager,
    private val isSearchAdapter: Boolean,
    private val repository: Repository?
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<CatalogProductsAdapter.ViewHolder>() {

    private var sourceList: List<Product> = productsList
    private var filterEntityMap = HashMap<String, ArrayList<String>>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val v = inflater.inflate(R.layout.row_catalog_product_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return sourceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = sourceList[position]
        bind(holder, item)
    }

    private fun bind(holder: ViewHolder, item: Product) {
        Picasso.with(context)
            .load(Uri.parse("http://185.71.82.34:8182/Loyalty/Products/${item.art}.png"))
            .networkPolicy(NetworkPolicy.OFFLINE)
            .into(holder.imageView, object : Callback {
                override fun onSuccess() {}
                override fun onError() {
                    Picasso.with(context)
                        .load(Uri.parse("http://185.71.82.34:8182/Loyalty/Products/${item.art}.png"))
                        .into(holder.imageView)
                }
            })


        setRatingStars(item, holder)


        holder.tvType.text = item.type
        holder.tvCountry.text = item.country
        holder.tvName.text = item.name
        holder.tvPrice.text = item.price.toString().split(".")[0]
        holder.tvCent.text = ".${item.price.toString().split(".")[1]}  \u20bd"
        holder.tvRate.text = "  " + item.rating

        if (item.isEvent) {
            holder.tvOldPrice.text = item.oldPrice
            holder.tvPrice.setTextColor(context.resources.getColor(R.color.darkred))
            holder.tvCent.setTextColor(context.resources.getColor(R.color.darkred))
        } else {
            holder.tvOldPrice.text = ""
            holder.tvPrice.setTextColor(context.resources.getColor(R.color.black))
            holder.tvCent.setTextColor(context.resources.getColor(R.color.black))
        }

    }

    fun setProductList(productList: List<Product>) {
        sourceList = productList
        notifyDataSetChanged()
    }

    fun getCurrentProductList(): List<Product> {
        return sourceList
    }

    fun setFilterCategoryList(filterList: ArrayList<String>?, categoryName: String?) {
        if (!categoryName.isNullOrEmpty()) {
            if (!filterList.isNullOrEmpty()) {
                filterEntityMap[categoryName] = filterList
            } else {
                filterEntityMap.remove(categoryName)
            }
        }

        if (filterEntityMap.isNotEmpty()) {
            // Убираем сортировку из sql запроса
            val tempSort = filterEntityMap["sort"]
            filterEntityMap.remove("sort")

            // Критерии сортировки
            val stringBuilder = StringBuilder()
            if (filterEntityMap.isEmpty()) {
                stringBuilder.append("SELECT * FROM Product WHERE category = '${productsList[0].category}'")
            } else {
                stringBuilder.append("SELECT * FROM Product WHERE category = '${productsList[0].category}' AND (")
            }

            val keyIterator = filterEntityMap.keys.iterator()

            while (keyIterator.hasNext()) {
                val key = keyIterator.next()
                filterEntityMap[key]?.let { queryString ->
                    val iterator = queryString.listIterator()
                    while (iterator.hasNext()) {
                        val field = iterator.next()
                        stringBuilder.append("${key.toLowerCase(Locale.ROOT)} = '$field'")
                        if (iterator.hasNext()) {
                            stringBuilder.append(" OR ")
                        }
                    }
                }
                stringBuilder.append(")")

                if (keyIterator.hasNext()) {
                    stringBuilder.append(" AND (")
                }
            }

            if (!tempSort.isNullOrEmpty()) {
                var desc = ""
                if(tempSort[0] == "rating") {
                    desc = "DESC"
                }
                stringBuilder.append(" ORDER BY ${tempSort[0]} $desc")
            }

            Coroutines.io {
                val query = SimpleSQLiteQuery(stringBuilder.toString(), null)
                val temp = repository!!.searchByCustomQuery(query)
                temp?.let {
                    Coroutines.main {
                        setProductList(temp)
                    }
                }
            }

            // Возвращаем сортировку
            tempSort?.let {
                filterEntityMap.put("sort", tempSort)
            }
        } else if (filterEntityMap.isEmpty() || (filterEntityMap.count() == 1 && (filterEntityMap.containsKey(
                "sort"
            )))
        ) {
            setProductList(productsList)
        }

        /*  if(filterEntityMap.isNotEmpty() && filterEntityMap.containsKey("sort")) {
              var fieldString: String = ""
              for (entry in filterEntityMap.values) {
                  fieldString = entry[0]
              }

              when (fieldString) {
                  "default" -> setProductList(sourceList.sortedByDescending { product -> product.isExclusive })
                  "price" ->  setProductList(sourceList.sortedBy { product -> product.price })
                  "rating" ->  setProductList(sourceList.sortedByDescending {
                      if (it.rating.isNotEmpty()) {
                          it.rating.toDouble()
                      } else {
                          0.0
                      }
                  })
                  "comments" ->  setProductList(sourceList.sortedBy {
                      if (it.rating.isNotEmpty()) {
                          it.rating.toDouble()
                      } else {
                          0.0
                      }
                  })
              }
          }*/
    }

    private fun setRatingStars(item: Product, holder: ViewHolder) {
        val rating = item.rating

        if (rating != null) {
            if (rating >= 1.0) {
                holder.iv_star1.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                holder.iv_star1.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }
            if (rating >= 2.0) {
                holder.iv_star2.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                holder.iv_star2.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }
            if (rating >= 3.0) {
                holder.iv_star3.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                holder.iv_star3.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }
            if (rating >= 4.0) {
                holder.iv_star4.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                holder.iv_star4.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }
            if (rating >= 5.0) {
                holder.iv_star5.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_gold,
                        null
                    )
                )
            } else {
                holder.iv_star5.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.star_empty,
                        null
                    )
                )
            }
        }
    }

    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener,
        View.OnLongClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        val tvType: TextView = itemView.findViewById(R.id.tv_type) as TextView
        val tvCountry = itemView.findViewById(R.id.tv_country1) as TextView
        val tvName = itemView.findViewById(R.id.tv_name) as TextView
        val tvPrice = itemView.findViewById(R.id.tv_price) as TextView
        val tvCent = itemView.findViewById(R.id.tv_cent) as TextView
        val tvOldPrice = itemView.findViewById(R.id.tv_old_price) as StrikeTextView
        val tvRate = itemView.findViewById(R.id.tv_rate) as TextView
        val imageView = itemView.findViewById(R.id.imageView3) as ImageView

        val iv_star1 = itemView.findViewById(R.id.iv_star1) as ImageView
        val iv_star2 = itemView.findViewById(R.id.iv_star2) as ImageView
        val iv_star3 = itemView.findViewById(R.id.iv_star3) as ImageView
        val iv_star4 = itemView.findViewById(R.id.iv_star4) as ImageView
        val iv_star5 = itemView.findViewById(R.id.iv_star5) as ImageView


        override fun onClick(p0: View?) {
            val fragment = AboutProductFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )

            val args = Bundle()
            args.putSerializable("product", sourceList[bindingAdapterPosition])
            fragment.arguments = args

            if (isSearchAdapter) {
                transaction.add(R.id.catalog_fragment_container, fragment)
                    .addToBackStack("aboutProductFragment${Random.nextInt()}").commit()
            } else {
                transaction.add(R.id.products_catalog_fragment_container, fragment)
                    .addToBackStack("aboutProductFragment${Random.nextInt()}").commit()
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            TODO("Not yet implemented")
        }

    }
}