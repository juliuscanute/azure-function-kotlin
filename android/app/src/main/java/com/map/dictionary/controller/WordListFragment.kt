package com.map.dictionary.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.map.dictionary.R
import org.koin.android.viewmodel.ext.android.viewModel

class WordListFragment : Fragment() {
    private val meaningAdapter = MeaningAdapter()
    val mainActivityViewModel: MainActivityViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meaning_list, container, false)
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = meaningAdapter
                addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val totalItemCount = (layoutManager as LinearLayoutManager).itemCount
                        val visibleItemCount = (layoutManager as LinearLayoutManager).childCount
                        val lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        mainActivityViewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
                    }
                })
            }
        }
        return view
    }
}
