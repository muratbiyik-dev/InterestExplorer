package com.pureblacksoft.interestexplorer.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.adapter.manage.OrderAdapter
import com.pureblacksoft.interestexplorer.data.manage.InterestOrder
import com.pureblacksoft.interestexplorer.databinding.DialogOrderBinding
import com.pureblacksoft.interestexplorer.fragment.list.ListFragment

class OrderDialog(context: Context) : Dialog(context), AdapterView.OnItemSelectedListener {
    companion object {
        var currentOrder = 0

        var onCancel: (() -> Unit)? = null
        var onApply: (() -> Unit)? = null
    }

    private lateinit var binding: DialogOrderBinding
    private var criteriaOrderList = mutableListOf<InterestOrder>()
    private var styleOrderList = mutableListOf<InterestOrder>()
    private var selectedCriteriaId = 0
    private var selectedStyleId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //region Order List
        addCriteria(0, R.string.Order_Criteria_Addition_Date)
        addCriteria(1, R.string.Order_Criteria_Release_Year)
        addCriteria(2, R.string.Order_Criteria_Rating)

        addStyle(0, R.string.Order_Style_New_To_Old)
        addStyle(1, R.string.Order_Style_Old_To_New)
        //endregion

        //region Interest Control
        if (ListFragment.currentListId == ListFragment.ID_LIST_NOVEL) {
            criteriaOrderList.removeLast()
        }
        //endregion

        //region Spinner
        binding.spnCriteriaOD.onItemSelectedListener = this
        val criteriaAdapter = OrderAdapter(context, criteriaOrderList)
        binding.spnCriteriaOD.adapter = criteriaAdapter

        binding.spnStyleOD.onItemSelectedListener = this
        val styleAdapter = OrderAdapter(context, styleOrderList)
        binding.spnStyleOD.adapter = styleAdapter
        //endregion

        //region Button
        binding.txtCriteriaOD.setOnClickListener {
            binding.spnCriteriaOD.performClick()
        }

        binding.txtStyleOD.setOnClickListener {
            binding.spnStyleOD.performClick()
        }

        binding.txtCancelOD.setOnClickListener {
            onCancel?.invoke()
        }

        binding.txtApplyOD.setOnClickListener {
            currentOrder = (2 * selectedCriteriaId) + selectedStyleId

            onApply?.invoke()
        }
        //endregion
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedCriteria = criteriaOrderList[binding.spnCriteriaOD.selectedItemPosition]
        selectedCriteriaId = selectedCriteria.id
        binding.txtCriteriaOD.text = selectedCriteria.name

        //region Criteria Control
        styleOrderList.clear()
        if (selectedCriteriaId == 2) {
            addStyle(0, R.string.Order_Style_High_To_Low)
            addStyle(1, R.string.Order_Style_Low_To_High)
        } else {
            addStyle(0, R.string.Order_Style_New_To_Old)
            addStyle(1, R.string.Order_Style_Old_To_New)
        }
        //endregion

        val selectedStyle = styleOrderList[binding.spnStyleOD.selectedItemPosition]
        selectedStyleId = selectedStyle.id
        binding.txtStyleOD.text = selectedStyle.name
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun show() {
        super.show()

        when (currentOrder) {
            1 -> {
                selectedCriteriaId = 0
                selectedStyleId = 1
            }
            2 -> {
                selectedCriteriaId = 1
                selectedStyleId = 0
            }
            3 -> {
                selectedCriteriaId = 1
                selectedStyleId = 1
            }
            4 -> {
                selectedCriteriaId = 2
                selectedStyleId = 0
            }
            5 -> {
                selectedCriteriaId = 2
                selectedStyleId = 1
            }
        }

        binding.spnCriteriaOD.setSelection(selectedCriteriaId)
        binding.spnStyleOD.setSelection(selectedStyleId)
    }

    private fun addCriteria(id: Int, name: Int) {
        criteriaOrderList.add(InterestOrder(id, context.getString(name)))
    }

    private fun addStyle(id: Int, name: Int) {
        styleOrderList.add(InterestOrder(id, context.getString(name)))
    }
}

//PureBlack Software / Murat BIYIK