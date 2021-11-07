package com.whitelext.dotaHunter.domain.repository

import com.example.ItemsListQuery
import com.whitelext.dotaHunter.common.Resource

interface ItemsRepository {
    suspend fun getItems(): Resource<List<ItemsListQuery.Item>>
}