package womenproject.com.mybury.presentation.mypage


import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.my_page_header.*
import kotlinx.android.synthetic.main.my_page_more_menu.view.*
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.FragmentMyPageBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.mypage.categoryedit.MyPageCategoryListAdapter
import womenproject.com.mybury.presentation.viewmodels.MyPageViewModel


/**
 * Created by HanAYeon on 2019. 4. 23..
 */

class MyPageFragment : BaseFragment<FragmentMyPageBinding, MyPageViewModel>()  {

    override val layoutResourceId: Int
        get() = R.layout.fragment_my_page

    override val viewModel: MyPageViewModel
        get() = MyPageViewModel()

    override fun initStartView() {
        viewDataBinding.viewModel = viewModel
        viewDataBinding.headerLayout.viewModel = viewModel

    }


    override fun initDataBinding() {
        viewDataBinding.mypageBottomSheet.homeClickListener = createOnClickHomeListener
        viewDataBinding.mypageBottomSheet.writeClickListener = createOnClickWriteListener
        viewDataBinding.mypageScrollLayout.ddayListClickListener = createOnClickDdayListListener
        viewDataBinding.mypageScrollLayout.categoryEditClickListener = createOnClickCategoryEditListener
        viewDataBinding.headerLayout.moreClickListener = moreButtonOnClickListener
        viewDataBinding.mypageMoreMenuLarge.appInfoClickListener = appInfoOnClickListener
        viewDataBinding.mypageMoreMenuLarge.profileEditClickListener = profileEditOnClickListener
        viewDataBinding.mypageMoreMenuLarge.loginInfoClickListener = loginInfoClickListener



        viewDataBinding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, p1 ->
            if (p1 == -384) {
                viewDataBinding.mypageScrollLayout.ddayLayout.visibility = View.GONE
            } else if (p1 == 0) {
                viewDataBinding.mypageScrollLayout.ddayLayout.visibility = View.VISIBLE
            }
        })

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


    private fun popupClickListener()  {
        if (viewDataBinding.mypageMoreMenuLarge.moreMenuLayout.visibility == View.VISIBLE) {
            viewDataBinding.mypageMoreMenuLarge.moreMenuLayout.visibility = View.GONE
        }
    }
    private val createOnClickWriteListener = View.OnClickListener {
        popupClickListener()
        val directions = MyPageFragmentDirections.actionMyPageToWrite()
        it.findNavController().navigate(directions)

    }

    private val createOnClickHomeListener = View.OnClickListener {
        popupClickListener()
        activity!!.onBackPressed()
    }

    private val createOnClickDdayListListener = View.OnClickListener {
        popupClickListener()
        val directions = MyPageFragmentDirections.actionMyPageToDday()
        it.findNavController().navigate(directions)

    }

    private val createOnClickCategoryEditListener = View.OnClickListener {
        popupClickListener()
        val directions = MyPageFragmentDirections.actionMyPageToCategoryEdit()
        it.findNavController().navigate(directions)
    }


    private val profileEditOnClickListener = View.OnClickListener {
        popupClickListener()
        val directions = MyPageFragmentDirections.actionMyPageToProfileEdit()
        it.findNavController().navigate(directions)
    }

    private val appInfoOnClickListener = View.OnClickListener {
        val directions = MyPageFragmentDirections.actionMyPageToAppInfo()
        it.findNavController().navigate(directions)
        popupClickListener()
    }

    private val loginInfoClickListener = View.OnClickListener {
        val directions = MyPageFragmentDirections.actionMyPageToLoginInfo()
        it.findNavController().navigate(directions)
        popupClickListener()
    }


    private val moreButtonOnClickListener = View.OnClickListener {

        if(viewDataBinding.mypageMoreMenuLarge.moreMenuLayout.visibility == View.GONE) {
            viewDataBinding.mypageMoreMenuLarge.moreMenuLayout.visibility = View.VISIBLE
        } else {
            viewDataBinding.mypageMoreMenuLarge.moreMenuLayout.visibility = View.GONE
        }
    }

}
