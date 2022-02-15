package womenproject.com.mybury.presentation.main.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.data.SearchResultType
import womenproject.com.mybury.data.SearchType
import womenproject.com.mybury.databinding.FragmentSearchBinding
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.viewmodels.SearchViewModel

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var binding: FragmentSearchBinding
    private var selectedSearchType = SearchType.All
    private var resultList = listOf<SearchResultType>()
    private lateinit var searchResultListAdapter: SearchResultListAdapter
    private lateinit var imm: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpObserve()
    }

    override fun onResume() {
        super.onResume()
        if (resultList.isNotEmpty()) {
            setUpResultList()
        }
    }

    private fun setUpViews() {
        searchResultListAdapter = SearchResultListAdapter()
        binding.lifecycleOwner = this

        binding.apply {
            searchResultList.adapter = searchResultListAdapter

            searchType = selectedSearchType

            searchRadioGroup.setOnCheckedChangeListener { _, id ->
                selectedSearchType = when (id) {
                    R.id.allType -> {
                        SearchType.All
                    }
                    R.id.categoryType -> {
                        SearchType.Category
                    }
                    else -> {
                        SearchType.DDay
                    }
                }
                searchType = selectedSearchType
                searchResultListAdapter.deleteList()
                binding.searchResultList.visibility = View.GONE
                binding.searchResultIsBlankView.visibility = View.GONE
            }

            searchEditTextView.setOnEditorActionListener { textView, id, keyEvent ->
                if (id == EditorInfo.IME_ACTION_SEARCH) {
                    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                    search("${textView.text}")
                }
                false
            }

            closeButton.setOnClickListener { requireActivity().onBackPressed() }
        }
    }

    private fun search(word: String) {
        viewModel.loadAllListSearch(selectedSearchType.getLowerText(), word)
    }

    private fun setUpObserve() {
        viewModel.allBucketSearchResult.observe(viewLifecycleOwner) {
            resultList = it
            setUpResultList()
        }

        viewModel.searchSate.observe(viewLifecycleOwner) {
            when (it!!) {
                BaseViewModel.LoadState.START -> {
                    startLoading()
                }
                BaseViewModel.LoadState.SUCCESS -> {
                    stopLoading()
                }
                BaseViewModel.LoadState.FAIL -> {
                    stopLoading()
                    binding.searchResultIsBlankView.visibility = View.VISIBLE

                }
                BaseViewModel.LoadState.RESTART -> {
                    search(binding.searchEditTextView.text.toString())
                }
            }
        }
    }

    private fun setUpResultList() {
        if (resultList.isEmpty()) {
            binding.searchResultIsBlankView.visibility = View.VISIBLE
            binding.searchResultList.visibility = View.GONE
            searchResultListAdapter.deleteList()
        } else {
            binding.searchResultIsBlankView.visibility = View.GONE
            binding.searchResultList.visibility = View.VISIBLE
            searchResultListAdapter.addList(selectedSearchType, resultList)
        }
    }

    private fun startLoading() {
        if (activity is MainActivity) {
            val activity = activity as MainActivity
            activity.startLoading()
        }
    }

    private fun stopLoading() {
        if (activity is MainActivity) {
            val activity = activity as MainActivity
            activity.stopLoading()
        }
    }
}