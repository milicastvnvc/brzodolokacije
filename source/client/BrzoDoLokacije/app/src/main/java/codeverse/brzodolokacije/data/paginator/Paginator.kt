package codeverse.brzodolokacije.data.paginator

import kotlinx.coroutines.flow.Flow
import codeverse.brzodolokacije.utils.*

interface Paginator<Key, Item> {
    fun loadNextItems() : Flow<Result<Item>>
    fun reset()
}