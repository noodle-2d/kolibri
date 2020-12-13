package com.ran.kolibri.common.dao

import com.ran.kolibri.common.entity.FinancialAsset

interface FinancialAssetDao {
    suspend fun insertFinancialAssets(financialAssets: List<FinancialAsset>): List<FinancialAsset>
    suspend fun deleteAllFinancialAssets(): Int
    suspend fun restartIdSequence()
}
