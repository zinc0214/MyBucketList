package womenproject.com.mybury.presentation.detail

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import womenproject.com.mybury.R
import womenproject.com.mybury.ui.ShowImgWideFragment


/**
 * Created by HanAYeon on 2019-07-15.
 */

class BucketDetailImageViewPageAdapter(private val context: Context, private val images : ArrayList<String>,
                                       private var showWideImg: (String) -> Unit) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        val view = layoutInflater!!.inflate(R.layout.detail_image_item, null)
        val imageView = view.findViewById(R.id.background_image) as ImageView
        imageView.setOnClickListener { v -> showWideImg.invoke(images[position]) }

        try {
            Glide.with(view).load(images[position]).centerCrop().placeholder(R.drawable.gradient_background).into(imageView)
        } catch (ex: IllegalArgumentException) {
            Log.e("Glide-tag", imageView.tag.toString())
        }

        val vp = container as ViewPager
        vp.addView(view, 0)

        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

  /*      val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)

        Glide.with(view).clear(view)
*/
        container.removeView(`object` as View?)

    }



}



