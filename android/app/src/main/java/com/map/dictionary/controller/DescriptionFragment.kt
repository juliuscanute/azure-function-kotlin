package com.map.dictionary.controller


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.map.dictionary.R


private const val WORD = "word"
private const val MEANING = "meaning"

class DescriptionFragment : Fragment() {
    private var word: String? = null
    private var meaning: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            word = it.getString(WORD)
            meaning = it.getString(MEANING)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_description, container, false)
    }

    companion object {
        @JvmStatic
        fun newBundle(word: String, meaning: String) =
            Bundle().apply {
                putString(WORD, word)
                putString(MEANING, meaning)
            }
    }
}
