package com.map.dictionary.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.map.dictionary.R
import com.map.dictionary.repository.dto.NetworkState
import kotlinx.android.synthetic.main.fragment_meaning_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class WordListFragment : Fragment() {
    private val meaningAdapter = MeaningAdapter()
    val mainActivityViewModel: MainActivityViewModel by viewModel()
    lateinit var snackbar: Snackbar
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
            }

        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snackbar = Snackbar.make(view, R.string.app_status, Snackbar.LENGTH_LONG)
        mainActivityViewModel.words.observe(this, Observer {
            meaningAdapter.submitList(it)
        })

        mainActivityViewModel.networkState.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { state ->
                when (state.networkState) {
                    NetworkState.NETWORK_FETCH_SUCCESS -> {
                        snackbar.setText(getString(state.message, state.loaded))
                    }
                    NetworkState.NETWORK_FETCH_START -> {
                        if (!snackbar.isShown) {
                            snackbar.show()
                        }
                        snackbar.setText(state.message)
                    }
                    NetworkState.NETWORK_FETCH_ERROR -> {
                        snackbar.setText(state.message)
                    }
                }
            }
        })
    }
}
