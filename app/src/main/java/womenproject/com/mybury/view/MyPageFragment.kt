package womenproject.com.mybury.view


import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.mypage_scroll_layout.view.*
import womenproject.com.mybury.adapter.MyPageCategoryListAdapter
import womenproject.com.mybury.base.BaseFragment
import womenproject.com.mybury.databinding.FragmentMyPageBinding
import womenproject.com.mybury.viewmodels.MyPageViewModel
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketCategory
import womenproject.com.mybury.viewmodels.BucketWriteViewModel

/**
 * Created by HanAYeon on 2019. 4. 23..
 */

class MyPageFragment : BaseFragment<FragmentMyPageBinding, MyPageViewModel>() {


    override val layoutResourceId: Int
        get() = R.layout.fragment_my_page

    override val viewModel: MyPageViewModel
        get() = MyPageViewModel()



    override fun initStartView() {
        viewDataBinding.viewModel = viewModel
        viewDataBinding.headerLayout.viewModel = viewModel

    }

    override fun initDataBinding() {
        viewDataBinding.mypageBottomSheet.homeClickListener = createOnClickHomeListener()
        viewDataBinding.mypageBottomSheet.writeClickListener = createOnClickWriteListener()
        viewDataBinding.mypageScrollLayout.ddayListClickListener = createOnClickDdayListListener()
        viewDataBinding.mypageScrollLayout.categoryEditClickListener = createOnClickCategoryEditListener()

        viewDataBinding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
                Log.e("ayhan", "$p1")
                if(p1 == -384) {
                    viewDataBinding.mypageScrollLayout.ddayLayout.visibility = View.GONE
                } else if(p1 == 0) {
                    viewDataBinding.mypageScrollLayout.ddayLayout.visibility = View.VISIBLE
                }
            }

        })

        viewDataBinding.headerLayout.constraintToolbar.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            Log.e("ayhan", "$left, $top")
        }

    }

    override fun initAfterBinding() {


        val categoryList = mutableListOf<String>()
        categoryList.add("없음")
        categoryList.add("여행")
        categoryList.add("서울 맛집")
        categoryList.add("다이어트")
        categoryList.add("스터디")
        categoryList.add("도라에몽 주머니")


        val layoutManager = LinearLayoutManager(context)

        viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.layoutManager = layoutManager
        viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.hasFixedSize()
        viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.adapter = MyPageCategoryListAdapter(context, categoryList)


        /*viewModel.getCategoryList(object : BucketWriteViewModel.GetBucketListCallBackListener {
            override fun start() {

            }

            override fun success(bucketCategory: BucketCategory) {
                for(i in 0 until bucketCategory.categoryList.size) {
                    categoryList.add(bucketCategory.categoryList[i].name)
                }
                viewDataBinding.mainScrollAppbar.addOnOffsetChangedListener(this@MyPageFragment)
                startAlphaAnimation(viewDataBinding.mainTextviewTitle, 0, View.INVISIBLE)


                val layoutManager = LinearLayoutManager(context)

                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.layoutManager = layoutManager
                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.hasFixedSize()
                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.adapter = MyPageCategoryListAdapter(context, categoryList)

                viewDataBinding.executePendingBindings()
            }

            override fun fail() {

            }

        })*/

    }


    private fun createOnClickWriteListener(): View.OnClickListener {
        return View.OnClickListener {
            val directions = MyPageFragmentDirections.actionMyPageToWrite()

            it.findNavController().navigate(directions)
        }
    }

    private fun createOnClickHomeListener() : View.OnClickListener {
        return View.OnClickListener {
            activity!!.onBackPressed()
        }
    }

    private fun createOnClickDdayListListener() : View.OnClickListener {
        return View.OnClickListener {
            val directions = MyPageFragmentDirections.actionMyPageToDday()

            it.findNavController().navigate(directions)
        }
    }

    private fun createOnClickCategoryEditListener() : View.OnClickListener {
        return View.OnClickListener {
            val directions = MyPageFragmentDirections.actionMyPageToCategoryEdit()

            it.findNavController().navigate(directions)
        }
    }
}
