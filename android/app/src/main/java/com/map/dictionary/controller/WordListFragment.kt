package com.map.dictionary.controller

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.map.dictionary.R
import com.map.dictionary.repository.dto.NetworkMessage
import com.map.dictionary.repository.dto.NetworkState
import kotlinx.android.synthetic.main.fragment_meaning_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class WordListFragment : Fragment() {
    private val meaningAdapter = MeaningAdapter()
    val mainActivityViewModel: MainActivityViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meaning_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = meaningAdapter
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivityViewModel.words.observe(this, Observer {
            meaningAdapter.submitList(it)
        })

        mainActivityViewModel.networkState.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { state ->
                val progressPosition = refreshLayout.height - 150
                if(progressPosition>0) {
                    refreshLayout.setProgressViewEndTarget(true, progressPosition)
                }
                when (state.networkState) {
                    NetworkState.NETWORK_FETCH_SUCCESS -> {
                        refreshLayout.isRefreshing = false
                        setSearchHits(state)
                    }
                    NetworkState.NETWORK_FETCH_START -> {
                        refreshLayout.isRefreshing = true

                    }
                    NetworkState.NETWORK_FETCH_ERROR -> {
                        refreshLayout.isRefreshing = false
                    }
                    NetworkState.NETWORK_NO_DATA -> {
                        refreshLayout.isRefreshing = false
                        setSearchHits(state)
                    }
                }
            }
        })


        refreshLayout.setOnRefreshListener {
            mainActivityViewModel.invalidateDataSource()
        }



        mainActivityViewModel.searchShown.observe(this, Observer {
            if (it == true) {
                searchLayout.setEndIconDrawable(R.drawable.ic_clear_black_24dp)
                searchLayout.setEndIconOnClickListener {
                    mainActivityViewModel.searchEnd()
                }
            } else {
                searchLayout.setEndIconDrawable(R.drawable.ic_search_black_24dp)
                searchLayout.setEndIconOnClickListener {
                    performSearch()
                }
            }
        })

        mainActivityViewModel.searchQuery.observe(this, Observer {
            searchLayout.editText?.setText(it)
        })

        searchLayout.editText?.setOnEditorActionListener { v, actionId, event ->
            if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
                performSearch()
            true
        }


    }

    private fun setSearchHits(state: NetworkMessage) {
        if (mainActivityViewModel.getSearchState()) {
            searchLayout.helperText = activity?.getString(state.message, state.loaded)
        } else {
            searchLayout.helperText = ""
        }
    }


    private fun performSearch() {
        mainActivityViewModel.searchStart()
        val word = searchLayout.editText?.text?.toString()
        if (!word.isNullOrEmpty()) {
            mainActivityViewModel.searchWord(word)
        }
    }

}
