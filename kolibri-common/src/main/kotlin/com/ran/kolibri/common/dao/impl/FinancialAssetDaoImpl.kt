package com.ran.kolibri.common.dao.impl

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dao.FinancialAssetDao
import com.ran.kolibri.common.entity.FinancialAsset
import com.ran.kolibri.common.entity.enums.Currency
import com.ran.kolibri.common.entity.enums.FinancialAssetType
import com.ran.kolibri.common.util.pgEnum
import com.ran.kolibri.common.util.runTransaction
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert

class FinancialAssetDaoImpl(kodein: Kodein) : FinancialAssetDao {

    private val db: Database = kodein.instance()

    override suspend fun insertFinancialAssets(financialAssets: List<FinancialAsset>): List<FinancialAsset> =
        runTransaction(db) {
            financialAssets.map { financialAsset ->
                val financialAssetId = FinancialAssets.insert {
                    it[name] = financialAsset.name
                    it[companyName] = financialAsset.companyName
                    it[type] = financialAsset.type
                    it[currency] = financialAsset.currency
                } get FinancialAssets.id
                financialAsset.copy(id = financialAssetId.value)
            }
        }

    override suspend fun deleteAllFinancialAssets(): Int =
        runTransaction(db) {
            FinancialAssets.deleteAll()
        }
}

object FinancialAssets : LongIdTable("financial_asset") {
    val name = varchar("name", 100)
    val companyName = varchar("company_name", 100)
    val type = pgEnum("type", FinancialAssetType)
    val currency = pgEnum("currency", Currency)
}
