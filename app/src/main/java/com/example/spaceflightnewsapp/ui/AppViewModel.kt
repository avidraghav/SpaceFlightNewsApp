package com.example.spaceflightnewsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceflightnewsapp.models.ArticlesResponse
import com.example.spaceflightnewsapp.repository.AppRepository
import com.example.spaceflightnewsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class AppViewModel(
    val repository : AppRepository
) : ViewModel() {

   val articlesList : MutableLiveData<Resource<ArticlesResponse>> = MutableLiveData()
    val searchArticleList : MutableLiveData<Resource<ArticlesResponse>> = MutableLiveData()
    init {
        getArticlesList()
    }

    fun getArticlesList()= viewModelScope.launch {
        articlesList.postValue(Resource.Loading())
        val response = repository.getArticles()
        articlesList.postValue(handleArticlesResponse(response))
    }
    fun getSearchArticleList(searchQuery : String) = viewModelScope.launch {
        searchArticleList.postValue(Resource.Loading())
        val response = repository.searchArticle(searchQuery)
        searchArticleList.postValue(handleSearchResponse(response))
    }

    private fun handleArticlesResponse(response: Response<ArticlesResponse>): Resource<ArticlesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
            return Resource.Error(response.message())
    }
    private fun handleSearchResponse(response: Response<ArticlesResponse>): Resource<ArticlesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


}