package com.example.budgetcalculator.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.budgetcalculator.data.data_source.OperationDao
import com.example.budgetcalculator.domain.model.Operation

@Database(
    entities = [Operation::class],
    version = 1
)
abstract class BudgetDatabase : RoomDatabase() {

    abstract val operationDao: OperationDao

    companion object {

        const val DATABASE_NAME = "budget_db"

    }

}