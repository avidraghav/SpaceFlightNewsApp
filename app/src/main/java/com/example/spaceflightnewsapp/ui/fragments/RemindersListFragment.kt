package com.example.spaceflightnewsapp.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.databinding.FragmentArticleDisplayBinding
import com.example.spaceflightnewsapp.databinding.FragmentRemindersListBinding
import com.example.spaceflightnewsapp.ui.AppViewModel
import com.example.spaceflightnewsapp.ui.MainActivity

class RemindersListFragment : Fragment(R.layout.fragment_reminders_list) {
    lateinit var viewModel: AppViewModel
    lateinit var binding: FragmentRemindersListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRemindersListBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
    }
}