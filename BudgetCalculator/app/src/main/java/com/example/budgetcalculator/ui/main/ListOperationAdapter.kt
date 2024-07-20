package com.example.budgetcalculator.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.budgetcalculator.R
import com.example.budgetcalculator.databinding.ItemOperationBinding
import com.example.budgetcalculator.domain.model.Operation
import com.example.budgetcalculator.domain.model.OperationType

class ListOperationAdapter(
    context: Context,
    items : List<Operation>,
    currency : String,
    listener : OnItemClick
) : RecyclerView.Adapter<ListOperationAdapter.ViewHolder>() {

    private val context: Context
    private var operations: List<Operation>
    private val mCallback: OnItemClick?
    private val currency: String

    init {
        operations = items
        this.context = context
        this.mCallback = listener
        this.currency = currency
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemOperationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myOperation = operations[position]

        with(holder.binding) {
            operationTitle.text = myOperation.title
            operationAmount.text = myOperation.amount.toString().plus(currency)
            operationFreq.text = if (myOperation.isAnnual) "/y" else "/m"
            operationAmount.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (!myOperation.isOn) R.color.grey
                    else {
                        if (myOperation.isIncome) R.color.green
                        else R.color.red
                    }
                )
            )
            operationImage.load(OperationType.getDrawableByOperationId(myOperation.type))
        }

        holder.itemView.setOnClickListener {
            mCallback!!.onClick(myOperation)
        }

        holder.itemView.setOnLongClickListener {
            mCallback!!.onLongClick(myOperation)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return operations.size
    }

    fun updateList(operations: List<Operation>) {
        this.operations = operations
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemOperationBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    interface OnItemClick {
        fun onClick(operation: Operation)

        fun onLongClick(operation: Operation)
    }

}