package com.whitelext.dotaHunter.common

sealed class Resource<out T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error(val error: ResourceError) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    object Empty : Resource<Nothing>()
    object Initial : Resource<Nothing>()
}