package com.example.spaceflightnewsapp.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.adapters.RemindersListAdapter
import com.example.spaceflightnewsapp.databinding.FragmentArticleDisplayBinding
import com.example.spaceflightnewsapp.databinding.FragmentRemindersListBinding
import com.example.spaceflightnewsapp.db.ReminderDatabase
import com.example.spaceflightnewsapp.ui.AppViewModel
import com.example.spaceflightnewsapp.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RemindersListFragment : Fragment(R.layout.fragment_reminders_list) {
    lateinit var viewModel: AppViewModel
    lateinit var binding: FragmentRemindersListBinding
    lateinit var reminderListAdapter: RemindersListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRemindersListBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        CoroutineScope(Dispatchers.IO).launch {
            val reminderes =ReminderDatabase(requireContext()).getRemindersDao().getAllReminders()
            withContext(Dispatchers.Main){
                reminderListAdapter.differ.submitList(reminderes)
            }
        }

        reminderListAdapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        reminderListAdapter = RemindersListAdapter()
        binding.rvSavedReminders.apply {
            adapter = reminderListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}