package com.example.spaceflightnewsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.spaceflightnewsapp.models.spaceflightapi.ArticlesResponse
import com.example.spaceflightnewsapp.repository.AppRepository
import com.example.spaceflightnewsapp.utils.AppApplication
import com.example.spaceflightnewsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class AppViewModel(
    app : Application,
    val repository : AppRepository
) : AndroidViewModel(app) {

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
       safeGetArticleApiCall()
    }
    fun getSearchArticleList(searchQuery : String) = viewModelScope.launch {
       safeSearchArticleApiCall(searchQuery)
    }

    private suspend fun safeSearchArticleApiCall(searchQuery: String){
        try {
            if(hasInternetConnection()){
                searchArticleList.postValue(Resource.Loading())
                val response = repository.searchArticle(searchQuery,skipSearchArticle)
                searchArticleList.postValue(handleSearchResponse(response))
            }
            else{
                searchArticleList.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t : Throwable){
            when(t) {
                is IOException -> searchArticleList.postValue(Resource.Error("Network Failure"))
                else -> searchArticleList.postValue(Resource.Error("Conversion Error"))
            }
        }

    }
    private suspend fun safeGetArticleApiCall(){
        try {
            if(hasInternetConnection()){
                articlesList.postValue(Resource.Loading())
                val response = repository.getArticles(skipArticle)
                articlesList.postValue(handleArticlesResponse(response))
            }
            else{
                articlesList.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t : Throwable){
            when(t) {
                is IOException -> articlesList.postValue(Resource.Error("Network Failure"))
                else -> articlesList.postValue(Resource.Error("Conversion Error"))
            }
        }

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


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<AppApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
    }


}