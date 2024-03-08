package photo

import kotlinx.serialization.Serializable

@Serializable
data class ListResponse<T>(val list: List<T>)