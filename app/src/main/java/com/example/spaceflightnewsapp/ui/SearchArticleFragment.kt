package com.example.spaceflightnewsapp.ui
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
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
                delay(500L)
                it.let {
                   if(it.toString().isNotEmpty()){
                       viewModel.getSearchArticleList(it.toString())
                   }
                    if(it.toString().isEmpty()){
                        viewModel.searchArticleResponse?.clear()
                        viewModel.skipSearchArticle=0
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
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
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