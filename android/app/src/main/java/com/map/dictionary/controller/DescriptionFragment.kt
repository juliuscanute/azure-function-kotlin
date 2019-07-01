package com.map.dictionary.controller


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.map.dictionary.R
import kotlinx.android.synthetic.main.fragment_description.*


private const val WORD = "word"
private const val MEANING = "meaning"

class DescriptionFragment : Fragment() {
    private var dictionaryWord: String? = null
    private var dictionaryMeaning: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dictionaryWord = it.getString(WORD)
            dictionaryMeaning = it.getString(MEANING)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        word.text = dictionaryWord
        meaning.text = dictionaryMeaning
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
