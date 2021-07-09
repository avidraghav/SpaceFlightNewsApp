package com.example.spaceflightnewsapp.ui.fragments

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.adapters.RemindersListAdapter
import com.example.spaceflightnewsapp.databinding.FragmentArticleDisplayBinding
import com.example.spaceflightnewsapp.databinding.FragmentRemindersListBinding
import com.example.spaceflightnewsapp.db.ReminderDatabase
import com.example.spaceflightnewsapp.db.ReminderModelClass
import com.example.spaceflightnewsapp.ui.AppViewModel
import com.example.spaceflightnewsapp.ui.MainActivity
import com.example.spaceflightnewsapp.utils.AlarmBroadCastReciever
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

        reminderListAdapter.setOnItemClickListener {
            cancelAlarm(it)
        }
        viewModel.getReminders().observe(viewLifecycleOwner, Observer {
            reminderListAdapter.differ.submitList(it)
        })
    }
    private fun cancelAlarm(reminder: ReminderModelClass) {
        val am : AlarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(activity, AlarmBroadCastReciever::class.java)
        val pi = PendingIntent.getBroadcast(activity, reminder.pendingIntentId, i, PendingIntent.FLAG_CANCEL_CURRENT)
        am.cancel(pi)
        pi.cancel()
        Log.e("info", "Reminder Cancelled")
        CoroutineScope(Dispatchers.IO).launch {
            ReminderDatabase(Application()).getRemindersDao().deleteReminder(reminder)
        }
    }
    private fun setupRecyclerView() {
        reminderListAdapter = RemindersListAdapter()
        binding.rvSavedReminders.apply {
            adapter = reminderListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}