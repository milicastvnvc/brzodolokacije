package codeverse.brzodolokacije.data.paginator

data class ScreenState<Item>(
    var isLoading: Boolean = false,
    var items: MutableList<Item> = mutableListOf(),
    var error: String? = null,
    var endReached: Boolean = false,
    var pageIndex: Int = 0
)
