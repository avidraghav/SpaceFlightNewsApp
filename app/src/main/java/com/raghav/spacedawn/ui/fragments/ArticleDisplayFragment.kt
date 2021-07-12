package com.raghav.spacedawn.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.raghav.spacedawn.R
import com.raghav.spacedawn.databinding.FragmentArticleDisplayBinding
import com.raghav.spacedawn.ui.AppViewModel
import com.raghav.spacedawn.ui.MainActivity


class ArticleDisplayFragment : Fragment(R.layout.fragment_article_display) {
    lateinit var viewModel: AppViewModel
    lateinit var binding: FragmentArticleDisplayBinding
    val args: ArticleDisplayFragmentArgs by navArgs()

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
            webViewClient = object : WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    binding.progressBar.visibility=View.VISIBLE
                    super.onPageStarted(view, url, favicon)
                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.progressBar.visibility=View.GONE
                    super.onPageFinished(view, url)
                }
            }

        }


    }


}