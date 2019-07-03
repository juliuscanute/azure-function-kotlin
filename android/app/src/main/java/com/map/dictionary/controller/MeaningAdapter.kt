package com.map.dictionary.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.map.dictionary.R
import com.map.dictionary.repository.dto.Meaning
import kotlinx.android.synthetic.main.fragment_meaning.view.*

class MeaningAdapter : PagedListAdapter<Meaning, RecyclerView.ViewHolder>(REPO_COMPARATOR) {
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Meaning
            val bundle = DescriptionFragment.newBundle(item.word, item.meaning)
            v.findNavController().navigate(R.id.wordToMeaningAction, bundle)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_meaning, parent, false)
        return DictionaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dictionaryItem = getItem(position)
        if (dictionaryItem != null) {
            (holder as DictionaryViewHolder).mContentView.text = "${dictionaryItem.id} ${dictionaryItem.word}"
            with(holder.view) {
                tag = dictionaryItem
                setOnClickListener(mOnClickListener)
            }
        }
    }

    inner class DictionaryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val mContentView: TextView = view.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Meaning>() {
            override fun areItemsTheSame(oldItem: Meaning, newItem: Meaning): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Meaning, newItem: Meaning): Boolean =
                oldItem == newItem
        }
    }
}
