package com.whitelext.dotaHunter.common

import com.apollographql.apollo.api.Error

sealed class Resource<out T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error(val error: ResourceError) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    object Empty : Resource<Nothing>()
    object Initial : Resource<Nothing>()
}