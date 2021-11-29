package com.whitelext.dotaHunter.domain.repository

import com.example.MetaListQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.api.GetMetaApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetaRepositoryImpl @Inject constructor(private val api: GetMetaApi) : MetaRepository {
    override suspend fun getMeta(): Resource<MetaListQuery.Data> {
        return api.getMeta()
    }
}
