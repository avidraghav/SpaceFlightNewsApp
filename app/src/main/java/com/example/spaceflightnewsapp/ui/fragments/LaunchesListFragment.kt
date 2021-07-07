package com.example.spaceflightnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.adapters.LaunchesAdapter
import com.example.spaceflightnewsapp.databinding.FragmentLaunchesListBinding
import com.example.spaceflightnewsapp.repository.AppRepository
import com.example.spaceflightnewsapp.ui.AppViewModel
import com.example.spaceflightnewsapp.ui.MainActivity
import com.example.spaceflightnewsapp.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.example.spaceflightnewsapp.utils.Resource

class LaunchesListFragment : Fragment(R.layout.fragment_launches_list) {

    lateinit var viewModel: AppViewModel
    lateinit var launchesAdapter: LaunchesAdapter
    private lateinit var binding: FragmentLaunchesListBinding
    private val TAG ="LaunchesListFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLaunchesListBinding.bind(view)
        setupRecyclerView()

        viewModel = (activity as MainActivity).viewModel

//        articlesAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable("article", it)
//            }
//            findNavController().navigate(
//                R.id.action_articlesListFragment_to_articleDisplayFragment,
//                bundle
//            )
//        }

        viewModel.launchesList.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let {
                        launchesAdapter.differ.submitList(it.results.toList())
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
                    showProgressBar()
                }
            }
        })


        binding.btnRetry.setOnClickListener {
            viewModel.getLaunchesList()
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
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getLaunchesList()
                isScrolling = false
            } else {
                binding.rvArticles.setPadding(0, 0, 0, 0)
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
        launchesAdapter = LaunchesAdapter()
        binding.rvArticles.apply {
            adapter = launchesAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@LaunchesListFragment.scrollListener)
        }
    }
}