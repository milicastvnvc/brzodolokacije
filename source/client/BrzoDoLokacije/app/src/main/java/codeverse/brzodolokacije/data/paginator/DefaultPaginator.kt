package codeverse.brzodolokacije.data.paginator

import codeverse.brzodolokacije.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Response<Item>,
    private inline val getNextKey: suspend (Item) -> Key,
    private inline val onError: suspend (String) -> Unit,
    private inline val onSuccess: suspend (items: Item, newKey: Key) -> Unit
) : Paginator<Key,Item> {

    private var currentKey: Key = initialKey
    private var isMakingRequest = false

    override fun loadNextItems() : Flow<Result<Item>> = flow {

        if (!isMakingRequest){
            try{
                isMakingRequest = true
                //emit(Result.Loading())

                onLoadUpdated(true)
                val response = onRequest(currentKey)
                isMakingRequest = false
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        //emit(Result.Success(response.body()!!))
                        currentKey = getNextKey(response.body()!!)
                        onSuccess(response.body()!!,currentKey)
                        onLoadUpdated(false)
                    }
                }
                else{
                    println(response.code())
                    println(response.message())
                    //emit(Result.Error("Došlo je do greške"))
                    onError("Došlo je do greške")
                    onLoadUpdated(false)
                }
            }
            catch (e: HttpException) {
                //emit(Result.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR))
                //emit(Result.Error("Došlo je do greške"))
                onError(Constants.UNEXPECTED_ERROR)
                onLoadUpdated(false)
            } catch (e: IOException) {
                //emit(Result.Error(Constants.SERVER_ERROR))
                //emit(Result.Error("Došlo je do greške"))
                onError(Constants.SERVER_ERROR)
                onLoadUpdated(false)
            }
        }

    }

    override fun reset() {
        currentKey = initialKey
    }

}