package com.whitelext.dotaHunter.domain.repository

import com.example.MetaListQuery
import com.whitelext.dotaHunter.common.Resource

interface MetaRepository {
    suspend fun getMeta(): Resource<MetaListQuery.Data>
}