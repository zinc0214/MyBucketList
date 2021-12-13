package womenproject.com.mybury.presentation.main.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import womenproject.com.mybury.R
import womenproject.com.mybury.data.SearchType
import womenproject.com.mybury.databinding.FragmentSearchBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.viewmodels.SearchViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_search

    override val viewModel: SearchViewModel
        get() = SearchViewModel()

    private var selectedSearchType = SearchType.All

    override fun initDataBinding() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.apply {
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
                }
                false
            }

            closeButton.setOnClickListener { this@SearchFragment.onBackPressedFragment() }
        }
    }

    fun onSelectedChipChanged(type: SearchType) {
        viewDataBinding.searchType = type
        viewDataBinding.notifyChange()
    }
}