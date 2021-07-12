package com.raghav.spacedawn.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raghav.spacedawn.R
import com.raghav.spacedawn.adapters.LaunchesAdapter
import com.raghav.spacedawn.databinding.FragmentLaunchesListBinding
import com.raghav.spacedawn.db.ReminderDatabase
import com.raghav.spacedawn.db.ReminderModelClass
import com.raghav.spacedawn.models.launchlibrary.LaunchLibraryResponseItem
import com.raghav.spacedawn.ui.AppViewModel
import com.raghav.spacedawn.ui.MainActivity
import com.raghav.spacedawn.utils.AlarmBroadCastReciever
import com.raghav.spacedawn.utils.AppApplication
import com.raghav.spacedawn.utils.Constants
import com.raghav.spacedawn.utils.Constants.Companion.MinutestoMiliseconds
import com.raghav.spacedawn.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.raghav.spacedawn.utils.Constants.Companion.STATUS_SET
import com.raghav.spacedawn.utils.Helpers.Companion.formatTo
import com.raghav.spacedawn.utils.Helpers.Companion.toDate
import com.raghav.spacedawn.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LaunchesListFragment : Fragment(R.layout.fragment_launches_list) {

    lateinit var viewModel: AppViewModel
    lateinit var launchesAdapter: LaunchesAdapter
    private lateinit var binding: FragmentLaunchesListBinding
    private val TAG = "LaunchesListFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLaunchesListBinding.bind(view)
        setupRecyclerView()

        viewModel = (activity as MainActivity).viewModel

        launchesAdapter.setOnItemClickListener {
            val dateTime = it.net.toDate(Constants.LAUNCH_DATE_INPUT_FORMAT)
            CoroutineScope(Dispatchers.IO).launch {
                if(ReminderDatabase(AppApplication()).getRemindersDao().exists(it.id)) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(activity, "Already Set", Toast.LENGTH_LONG).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        val timeToSetAlarm: Long = dateTime.time - MinutestoMiliseconds
                        setAlarm(timeToSetAlarm, System.currentTimeMillis().toInt(), it)
                    }
                }
            }
        }

        viewModel.launchesList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let {
                        launchesAdapter.differ.submitList(it.results.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Log.d(TAG, "inside failure")
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
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

    private fun setAlarm(
        timeInMilliseconds: Long,
        pendingIntentId: Int,
        launch: LaunchLibraryResponseItem
    ) {
        val nameOfLaunch = launch.name
        val idOfLaucnh = launch.id
        val imageUrl = launch.image
        val dateTimeOfLaunch = launch.net.toDate(Constants.LAUNCH_DATE_INPUT_FORMAT).formatTo(
            Constants.DATE_OUTPUT_FORMAT
        )

        val am: AlarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(activity, AlarmBroadCastReciever::class.java)
        val pi = PendingIntent.getBroadcast(
            activity,
            pendingIntentId,
            i,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val reminder = ReminderModelClass(
            idOfLaucnh, nameOfLaunch, dateTimeOfLaunch, pendingIntentId,
            STATUS_SET,imageUrl
        )
        lifecycleScope.launch {
            ReminderDatabase(AppApplication()).getRemindersDao().saveReminder(reminder)
        }
        am.setExact(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pi)
        Toast.makeText(
            activity,
            "Reminder set for 15 minutes prior to launch time",
            Toast.LENGTH_LONG
        ).show()


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
            if (shouldPaginate) {
                viewModel.getLaunchesList()
                isScrolling = false
            } else {
                binding.rvArticles.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
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