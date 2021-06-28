package com.example.spaceflightnewsapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceflightnewsapp.models.ArticlesResponse
import com.example.spaceflightnewsapp.models.ArticlesResponseItem
import com.example.spaceflightnewsapp.repository.AppRepository
import com.example.spaceflightnewsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class AppViewModel(
    val repository : AppRepository
) : ViewModel() {

   val articlesList : MutableLiveData<Resource<ArticlesResponse>> = MutableLiveData()
    init {
        getArticlesList()
    }
    fun getArticlesList()= viewModelScope.launch {
        articlesList.postValue(Resource.Loading())
        val response = repository.getArticles()
        articlesList.postValue(getList(response))
    }

    private fun getList(response: Response<ArticlesResponse>): Resource<ArticlesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
            return Resource.Error(response.message())
    }


}