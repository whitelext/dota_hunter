package com.whitelext.dotaHunter.domain.api

import com.apollographql.apollo.api.Error
import com.whitelext.dotaHunter.common.ResourceError
import com.whitelext.dotaHunter.common.ResourceErrorType

class SearchApiError(error: Error) : ResourceError(ResourceErrorType.API_ERROR, error.message)