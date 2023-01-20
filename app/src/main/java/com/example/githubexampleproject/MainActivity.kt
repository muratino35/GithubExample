package com.example.githubexampleproject

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubexampleproject.R.*
import com.example.githubexampleproject.data.api.ApiHelper
import com.example.githubexampleproject.data.api.RetrofitBuilder
import com.example.githubexampleproject.data.model.User
import com.example.githubexampleproject.ui.base.ViewModelFactory
import com.example.githubexampleproject.ui.main.adapter.MainAdapter
import com.example.githubexampleproject.ui.main.viewmodel.MainViewModel
import com.example.githubexampleproject.utils.Status.ERROR
import com.example.githubexampleproject.utils.Status.LOADING
import com.example.githubexampleproject.utils.Status.SUCCESS
import kotlinx.android.synthetic.main.activity_main.progressBar
import kotlinx.android.synthetic.main.activity_main.recyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setupViewModel()
        setupUI()
        setupObservers()
    }


    private fun setupViewModel() {
        viewModel = ViewModelProvider(this,ViewModelFactory(ApiHelper(RetrofitBuilder.apiService)))
            .get(MainViewModel::class.java)
    }


    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.getUsers().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { users -> retrieveList(users) }
                    }
                    ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(users: List<User>) {
        adapter.apply {
            addUsers(users)
            notifyDataSetChanged()
        }
    }
}