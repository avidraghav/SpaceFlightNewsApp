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
    var articlesResponse : ArticlesResponse? = null
    var skipArticle =0

    val searchArticleList : MutableLiveData<Resource<ArticlesResponse>> = MutableLiveData()
    var skipSearchArticle =0
    var searchArticleResponse : ArticlesResponse? = null

    init {
        getArticlesList()
    }

    fun getArticlesList()= viewModelScope.launch {
        articlesList.postValue(Resource.Loading())
        val response = repository.getArticles(skipArticle)
        articlesList.postValue(handleArticlesResponse(response))
    }
    fun getSearchArticleList(searchQuery : String) = viewModelScope.launch {
        searchArticleList.postValue(Resource.Loading())
        val response = repository.searchArticle(searchQuery,skipSearchArticle)
        searchArticleList.postValue(handleSearchResponse(response))
    }

    private fun handleArticlesResponse(response: Response<ArticlesResponse>): Resource<ArticlesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                skipArticle+=10
                if(articlesResponse == null){
                    articlesResponse = it
                }
                else{
                    val oldArticles = articlesResponse
                    val newArticles =it
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(articlesResponse?: it)
            }
        }
            return Resource.Error(response.message())
    }
    private fun handleSearchResponse(response: Response<ArticlesResponse>): Resource<ArticlesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                skipSearchArticle+=10
                if(searchArticleResponse == null){
                    searchArticleResponse = it
                }
                else{
                    val oldArticles = searchArticleResponse
                    val newArticles =it
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchArticleResponse?: it)
            }
        }
        return Resource.Error(response.message())
    }


}