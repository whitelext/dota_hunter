package com.whitelext.dotaHunter.domain.repository

import com.example.ItemsListQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.api.GetItemsApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsRepositoryImpl @Inject constructor(private val apiService: GetItemsApi) :
    ItemsRepository {

    override suspend fun getItems(): Resource<List<ItemsListQuery.Item>> =
        apiService.getItems()
}