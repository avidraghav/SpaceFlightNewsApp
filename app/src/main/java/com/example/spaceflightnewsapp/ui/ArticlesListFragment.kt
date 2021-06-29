package com.example.spaceflightnewsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.adapters.ArticlesAdapter
import com.example.spaceflightnewsapp.databinding.FragmentArticlesListBinding
import com.example.spaceflightnewsapp.network.RetrofitInstance
import com.example.spaceflightnewsapp.repository.AppRepository
import com.example.spaceflightnewsapp.utils.Resource

class ArticlesListFragment : Fragment(R.layout.fragment_articles_list) {

    lateinit var viewModel: AppViewModel
    lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var binding: FragmentArticlesListBinding
    private val TAG ="ArticlesListFragment"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticlesListBinding.bind(view)
        setupRecyclerView()

        viewModel = (activity as MainActivity).viewModel

        articlesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_articlesListFragment_to_articleDisplayFragment,
                bundle
            )
        }


        viewModel.articlesList.observe(viewLifecycleOwner, Observer { response ->
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
        binding.rvArticles.apply {
            adapter = articlesAdapter
            layoutManager = LinearLayoutManager(activity)
            //addOnScrollListener(this@ArticlesFragment.scrollListener)
        }
    }
}