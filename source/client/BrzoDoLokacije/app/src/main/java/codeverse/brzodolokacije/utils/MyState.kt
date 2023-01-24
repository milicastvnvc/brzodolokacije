package codeverse.brzodolokacije.utils


data class MyState<T>(val success : Boolean = false,
                      val data : T? = null,
                      val isLoading: Boolean = false,
                      val error: String = "")
