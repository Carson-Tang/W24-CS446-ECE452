package com.backend.application.response

import kotlinx.serialization.Serializable

@Serializable
data class ListResponse<T>(val list: List<T>)