package com.kristianskokars.tasky.core.domain.model

sealed interface APIError {
    data object ServerError: APIError
    data object ClientError: APIError
}
