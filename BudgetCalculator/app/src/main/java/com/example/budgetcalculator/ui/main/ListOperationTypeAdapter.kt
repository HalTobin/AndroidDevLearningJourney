package com.example.budgetcalculator.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.example.budgetcalculator.R
import com.example.budgetcalculator.domain.model.OperationType

class ListOperationTypeAdapter(
    context : Context,
    items : List<OperationType>,
    listener : OnItemClick
) : ArrayAdapter<OperationType>(context, 0, items) {

    private var operationTypes: List<OperationType>
    private val mCallback: OnItemClick?
    private var filter = NoFilter()

    init {
        operationTypes = items
        this.mCallback = listener
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_spinner_operation_type,
            parent,
            false
        )

        val nameView = view!!.findViewById<TextView>(R.id.item_spinner_operation_type_text)
        val imageView = view.findViewById<ImageView>(R.id.item_spinner_operation_type_image)

        val myOperationType = operationTypes[position]
        nameView.text = context.resources.getString(myOperationType.text)
        imageView.load(myOperationType.drawable)

        return view
    }

    override fun getFilter(): Filter {
        return filter
    }

    override fun getCount(): Int {
        return operationTypes.size
    }

    fun updateList(operationTypes: List<OperationType>) {
        this.operationTypes = operationTypes
        notifyDataSetChanged()
    }

    @Suppress("UNCHECKED_CAST")
    inner class NoFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            results.values = operationTypes
            results.count = operationTypes.size
            return results
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return context.resources.getString((resultValue as OperationType).text)
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

        }
    }

    interface OnItemClick {
        fun onClick(operationType: OperationType)
    }

}