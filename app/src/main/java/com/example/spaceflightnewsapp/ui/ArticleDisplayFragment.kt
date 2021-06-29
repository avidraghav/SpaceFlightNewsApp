package com.example.spaceflightnewsapp.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.databinding.FragmentArticleDisplayBinding


class ArticleDisplayFragment : Fragment(R.layout.fragment_article_display) {
    lateinit var viewModel: AppViewModel
    lateinit var binding: FragmentArticleDisplayBinding
    val args : ArticleDisplayFragmentArgs by navArgs()

    private val TAG ="ArticleDisplayFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleDisplayBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel

        val article = args.article
        binding.webView.apply {
            try {
                webViewClient = WebViewClient()
                loadUrl(article.url)
            }catch (e : Exception){
                Toast.makeText(activity,e.toString(),Toast.LENGTH_SHORT).show()
            }

        }


    }


}