package com.example.spaceflightnewsapp.ui
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.adapters.ArticlesAdapter
import com.example.spaceflightnewsapp.databinding.FragmentArticlesListBinding
import com.example.spaceflightnewsapp.databinding.FragmentSearchArticleBinding
import com.example.spaceflightnewsapp.utils.Resource
import kotlinx.coroutines.*

class SearchArticleFragment : Fragment(R.layout.fragment_search_article) {

    lateinit var viewModel: AppViewModel
    lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var binding: FragmentSearchArticleBinding
    private val TAG ="SearchArticleFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchArticleBinding.bind(view)
        setupRecyclerView()
        viewModel = (activity as MainActivity).viewModel

        // Search Articles functionality implementation
        var job : Job? = null
        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                it.let {
                   if(it.toString().isNotEmpty()){
                       viewModel.getSearchArticleList(it.toString())
                   }
                }
            }
        }

        viewModel.searchArticleList.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        articlesAdapter.differ.submitList(it)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE

    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }
    private fun setupRecyclerView() {
        articlesAdapter = ArticlesAdapter()
        binding.rvSearchArticles.apply {
            adapter = articlesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}