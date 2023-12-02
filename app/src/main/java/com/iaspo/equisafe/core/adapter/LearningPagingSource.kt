package com.iaspo.equisafe.core.adapter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.iaspo.equisafe.core.constant.DataStoreConstant
import com.iaspo.equisafe.core.data.local.UserPreferences
import com.iaspo.equisafe.core.data.network.response.learningresponse.VideosItem
import com.iaspo.equisafe.core.data.network.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class LearningPagingSource(
    private val apiService: ApiService,
    private val dataStore: UserPreferences,
    private val query: String?
) : PagingSource<Int, VideosItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideosItem> {
        return try {
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
            }

            Log.d("LearningPagingSource", tokenUser.toString())

            val page = params.key ?: INITIAL_PAGE_INDEX

            val response = apiService.getVideoLearning(
                token = tokenUser ?: "",
                page = page,
                perPage = 2,
                search = query
            )


            val responseVideoList = response.data?.videos ?: emptyList()
            Log.d("LearningPagingSource", responseVideoList.toString())

            LoadResult.Page(
                data = responseVideoList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseVideoList.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.d("LearningPagingSource", e.message.toString())
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, VideosItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}