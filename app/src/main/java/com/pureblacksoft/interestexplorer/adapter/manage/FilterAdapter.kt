package com.pureblacksoft.interestexplorer.adapter.manage

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.pureblacksoft.interestexplorer.data.manage.InterestFilter
import com.pureblacksoft.interestexplorer.databinding.PartButtonBinding

class FilterAdapter(
    private val context: Context,
    private val filterList: MutableList<InterestFilter>
) :
    BaseAdapter() {
    override fun getItemId(i: Int): Long = 0

    override fun getItem(i: Int): Any? = null

    override fun getCount(): Int = filterList.size

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(context)
        val binding = PartButtonBinding.inflate(layoutInflater, parent, false)
        binding.txtTitleBP.text = filterList[position].name

        return binding.root
    }
}

//PureBlack Software / Murat BIYIK