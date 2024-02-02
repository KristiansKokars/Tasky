package com.kristianskokars.tasky.core.domain

sealed interface APIError {
    data object ServerError: APIError
    data object ClientError: APIError
}
