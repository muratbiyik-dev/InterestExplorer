package com.pureblacksoft.interestexplorer.adapter.manage

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.pureblacksoft.interestexplorer.data.manage.InterestOrder
import com.pureblacksoft.interestexplorer.databinding.PartButtonBinding

class OrderAdapter(
    private val context: Context,
    private val orderList: MutableList<InterestOrder>
) :
    BaseAdapter() {
    override fun getItemId(i: Int): Long = 0

    override fun getItem(i: Int): Any? = null

    override fun getCount(): Int = orderList.size

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(context)
        val binding = PartButtonBinding.inflate(layoutInflater, parent, false)
        binding.txtTitleBP.text = orderList[position].name

        return binding.root
    }
}

//PureBlack Software / Murat BIYIK