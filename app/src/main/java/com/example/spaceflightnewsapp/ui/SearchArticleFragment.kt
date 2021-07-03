package com.example.spaceflightnewsapp.ui
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.adapters.ArticlesAdapter
import com.example.spaceflightnewsapp.databinding.FragmentArticlesListBinding
import com.example.spaceflightnewsapp.databinding.FragmentSearchArticleBinding
import com.example.spaceflightnewsapp.utils.Constants
import com.example.spaceflightnewsapp.utils.Constants.Companion.DELAY_TIME
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

        articlesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchArticleFragment_to_articleDisplayFragment,
                bundle
            )
        }
        // Search Articles functionality implementation
        var job : Job? = null
        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(DELAY_TIME)
                it.let {
                   if(it.toString().isNotEmpty()){
                       viewModel.getSearchArticleList(it.toString())
                       Log.d(TAG,"inside is Notempty")
                   }
                }
            }
        }
        viewModel.searchArticleList.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    Log.d(TAG,"inside success")
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let {
                        articlesAdapter.differ.submitList(it)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Log.d(TAG,"inside failure")
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading -> {
                    Log.d(TAG,"inside loading")
                    showProgressBar()
                }
            }
        })
        binding.btnRetry.setOnClickListener {
            if (binding.etSearch.text.toString().isNotEmpty()) {
                viewModel.getSearchArticleList(binding.etSearch.text.toString())
            } else {
                hideErrorMessage()
            }
        }
    }
    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
    private fun hideErrorMessage() {
        binding.itemErrorMessage.visibility = View.INVISIBLE
        isError = false
    }
    private fun showErrorMessage(message: String) {
        binding.itemErrorMessage.visibility = View.VISIBLE
        binding.tvErrorMessage.text = message
        isError = true
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            Log.d(TAG,shouldPaginate.toString())
            if(shouldPaginate) {
                viewModel.getSearchArticleList(binding.etSearch.text.toString())
                isScrolling = false
            } else {
                binding.rvSearchArticles.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }
    private fun setupRecyclerView() {
        articlesAdapter = ArticlesAdapter()
        binding.rvSearchArticles.apply {
            adapter = articlesAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchArticleFragment.scrollListener)
        }
    }
}