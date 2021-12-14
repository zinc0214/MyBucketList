package womenproject.com.mybury.presentation.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.SearchType
import womenproject.com.mybury.databinding.FragmentSearchBinding
import womenproject.com.mybury.presentation.main.bucketlist.MainBucketListAdapter
import womenproject.com.mybury.presentation.viewmodels.SearchViewModel

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: FragmentSearchBinding
    private var selectedSearchType = SearchType.All

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        viewModel = SearchViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpObserve()
    }

    private fun setUpViews() {
        binding.lifecycleOwner = this
        binding.apply {
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
            }

            searchEditTextView.setOnEditorActionListener { textView, id, keyEvent ->
                if (id == EditorInfo.IME_ACTION_SEARCH) {
                    Toast.makeText(
                        context,
                        "Type : ${selectedSearchType.getText()}",
                        Toast.LENGTH_SHORT
                    ).show()

                    search("${textView.text}")
                }
                false
            }

            closeButton.setOnClickListener { requireActivity().onBackPressed() }
        }
    }

    private fun search(word: String) {
        viewModel.loadAllListSearch()
    }

    private fun setUpObserve() {
        viewModel.allBucketSearchResult.observe(viewLifecycleOwner, allResultObserve)
    }

    private val allResultObserve = Observer<List<BucketItem>> {
        binding.searchResultList.adapter =
            MainBucketListAdapter(it, {})
    }
}