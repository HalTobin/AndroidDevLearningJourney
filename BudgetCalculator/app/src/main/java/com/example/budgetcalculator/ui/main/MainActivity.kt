package com.example.budgetcalculator.ui.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetcalculator.R
import com.example.budgetcalculator.base.BaseActivity
import com.example.budgetcalculator.databinding.ActivityMainBinding
import com.example.budgetcalculator.domain.model.Operation
import com.example.budgetcalculator.domain.model.OperationType
import com.example.budgetcalculator.domain.model.Summary
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding>(),
    MainContract.View,
    ListOperationAdapter.OnItemClick,
    ListOperationTypeAdapter.OnItemClick {

    private var mAdapter: ListOperationAdapter? = null

    @Inject
    lateinit var presenter: MainContract.Presenter

    private var currency = "€"

    private var isAnnual = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding!!.root)

        setUpAdapter()
        setUpListeners()
        setUpObservers()
    }

    private fun setUpAdapter() {
        mAdapter = ListOperationAdapter(this, ArrayList(), currency,this)
        binding!!.mainBudgetListOperation.layoutManager = LinearLayoutManager(this)
        binding!!.mainBudgetListOperation.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        binding!!.mainBudgetListOperation.adapter = mAdapter
    }

    private fun setUpListeners() {
        binding!!.mainBudgetAddOperation.setOnClickListener {
            this.showAddEditOperationDialog(null)
        }
        binding!!.mainAnnualOrMonthButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.main_annual_toggle -> isAnnual = true
                    R.id.main_monthly_toggle -> isAnnual = false
                }
            }
            presenter.refreshSummary()
        }
    }

    private fun setUpObservers() {
        presenter.observeOperations().observe(this) { operations ->
            displayOperation(operations)
        }
    }

    @SuppressLint("InflateParams")
    fun showAddEditOperationDialog(operation: Operation?) {

        var operationId: Int? = null

        var typeId = OperationType.TYPE_IN_UNDEFINED

        var isIncome = true
        var isAnnual = false

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialogLayout = View.inflate(this, R.layout.dialog_add_edit_operation, null)

        val typeSpinner = dialogLayout.findViewById<AutoCompleteTextView>(R.id.main_operation_type)
        val titleText = dialogLayout.findViewById<EditText>(R.id.main_operation_title)
        val amountText = dialogLayout.findViewById<EditText>(R.id.main_operation_amount)
        val isIncomeOrOutcome = dialogLayout.findViewById<MaterialButtonToggleGroup>(R.id.main_operation_income_or_outcome)
        val isAnnualOrMonthly = dialogLayout.findViewById<MaterialButtonToggleGroup>(R.id.main_operation_annual_or_monthly)

        if (operation != null) {
            operationId = operation.id
            titleText.setText(operation.title)
            amountText.setText(operation.amount.toString())
            typeId = operation.type

            isIncome = operation.isIncome
            isAnnual = operation.isAnnual
            if (!isIncome) isIncomeOrOutcome.check(R.id.main_operation_outcome_toggle)
            if (isAnnual) isAnnualOrMonthly.check(R.id.main_operation_annual_toggle)
        }

        var spinnerAdapter = ListOperationTypeAdapter(
            this,
            presenter.onChangeIncomeOrOutcome(isIncome),
            this
        )

        typeSpinner.setAdapter(spinnerAdapter)

        typeSpinner.setText(
            resources.getString(
                if (operation == null) OperationType.undefinedTextResource
                else OperationType.findTextByTypeAndIsIncome(operation.type, isIncome)
            )
        )

        isIncomeOrOutcome.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.main_operation_income_toggle -> isIncome = true
                    R.id.main_operation_outcome_toggle -> isIncome = false
                }
            }
            spinnerAdapter = ListOperationTypeAdapter(
                this,
                presenter.onChangeIncomeOrOutcome(isIncome),
                this
            )
            typeSpinner.setAdapter(spinnerAdapter)
            typeSpinner.setText(resources.getString(OperationType.undefinedTextResource))
        }

        isAnnualOrMonthly.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.main_operation_annual_toggle -> isAnnual = true
                    R.id.main_operation_monthly_toggle -> isAnnual = false
                }
            }
        }

        typeSpinner.doAfterTextChanged {
            typeId = OperationType.findIdByTextAndIsIncome(
                context = this,
                text = it.toString(),
                isIncome = isIncome
            )
        }

        with(builder) {
            setView(dialogLayout)
            setTitle(R.string.new_operation)

            setPositiveButton(
                if (operation == null) R.string.add_low
                else R.string.edit_low
            ) { dialog, _ ->
                presenter.onAddOperationButtonClick(
                    id = operationId,
                    type = typeId,
                    title = titleText.text.toString(),
                    amount = amountText.text.toString().toFloat(),
                    isIncome = isIncome,
                    isAnnual = isAnnual
                )
                dialog.dismiss()
            }

            if (operation != null) {
                setNegativeButton(R.string.delete_low) { dialog, _ ->
                    presenter.onDeleteOperationButtonClick(operation)
                    dialog.dismiss()
                }
            }

            show()
        }

    }

    override fun displayOperation(operations: List<Operation>) {
        mAdapter!!.updateList(operations)
    }

    override fun displayValues(values: Summary) {
        binding!!.mainBudgetIncomeRaw.text = values.getValue(Summary.INCOMES_RAW, isAnnual).plus(currency)
        binding!!.mainBudgetIncomeNet.text = values.getValue(Summary.INCOMES_NET, isAnnual).plus(currency)

        if (values.incomesAnnual < 0) setNetIncomesTextColor(R.color.red)
        else setNetIncomesTextColor(R.color.green)

        binding!!.mainBudgetOutcome.text = values.getValue(Summary.OUTCOMES, isAnnual).plus(currency)
        binding!!.mainBudgetAside.text = values.getValue(Summary.ASIDE, isAnnual).plus(currency)
    }

    private fun setNetIncomesTextColor(colorId: Int) {
        binding!!.mainBudgetIncomeRaw.setTextColor(
            ContextCompat.getColor(this, colorId)
        )
        binding!!.mainBudgetIncomeNet.setTextColor(
            ContextCompat.getColor(this, colorId)
        )
    }

    // Click on an Operation from the RecyclerView of the MainActivity
    override fun onClick(operation: Operation) {
        showAddEditOperationDialog(operation)
    }
    // Long click on an Operation from the RecyclerView of the MainActivity
    override fun onLongClick(operation: Operation) {
        presenter.onOnOffOperationClick(operation)
    }

    // Click on an OperationType from the Spinner of the AddEditOperationDialog
    override fun onClick(operationType: OperationType) {
        TODO("Not yet implemented")
    }

}