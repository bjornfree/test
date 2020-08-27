package com.bjornfree.tochka.loyalty.ui.shops

import com.bjornfree.tochka.loyalty.model.Shop
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import java.io.Serializable

class ExpandableGroupCity(title: String, items: List<Shop>) : ExpandableGroup<Shop>(title, items) , Serializable{
}