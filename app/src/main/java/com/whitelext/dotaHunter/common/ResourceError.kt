package com.whitelext.dotaHunter.common

enum class ResourceErrorType {
    NO_INTERNET,
    API_ERROR,
    UNKNOWN,
}

open class ResourceError(
    val type: ResourceErrorType = ResourceErrorType.UNKNOWN,
    val message: String = "",
)