package womenproject.com.mybury.presentation.mypage.dday

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.DdayTotalBucketList
import womenproject.com.mybury.data.DdayEachBucketGroup
import womenproject.com.mybury.databinding.DdayBucketListBinding

/**
 * Created by HanAYeon on 2019. 1. 22..
 */

class DdayBucketTotalListAdapter(context: Context?, bucketList: BucketList) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var context: Context = context!!
    private var ddayBucketTotalList: DdayTotalBucketList
    private lateinit var currentViewHolderTotal: DdayBucketTotalListViewHolder

    init {

        val sameDdayBucketListGroup = ArrayList<DdayEachBucketGroup>()

        val sameCountList = getSameCountBucket(bucketList)

        for(i in 0 until sameCountList.size) {

            val sameDdayBucketList = ArrayList<BucketItem>()

            for(j in 0 until bucketList.bucketlists.size) {

                if(sameCountList[i] == bucketList.bucketlists[j].dDay) {
                    sameDdayBucketList.add(bucketList.bucketlists[j])
                }

            }

            val ddayEachBucketGroup = DdayEachBucketGroup(sameCountList[i], sameDdayBucketList)
            sameDdayBucketListGroup.add(ddayEachBucketGroup)
        }

        ddayBucketTotalList = DdayTotalBucketList(sameDdayBucketListGroup)
        Log.e(this.toString(), "ayhan : ddayBucketCheck :: \n ${ddayBucketTotalList}")

    }


    private fun getSameCountBucket(bucketList: BucketList) : ArrayList<Int> {
        val sameDayList = ArrayList<Int>()
        sameDayList.add(bucketList.bucketlists[0].dDay)

        for (i in 0 until bucketList.bucketlists.size) {

            for(j in 0 until sameDayList.size) {
                if(sameDayList.get(j) != bucketList.bucketlists[i].dDay) {
                    sameDayList.add(bucketList.bucketlists[i].dDay)
                }
            }
        }

        return sameDayList

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        Log.e("ayhan:ViewType", "$viewType")



        currentViewHolderTotal = DdayBucketTotalListViewHolder(DdayBucketListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return currentViewHolderTotal

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(position==ddayBucketTotalList.ddayBucketEachListItem.size-1) {
            ddayBucketTotalList.ddayBucketEachListItem[position].isLast = true
        }
        currentViewHolderTotal.apply {
            bind(ddayBucketTotalList.ddayBucketEachListItem[position], context)
        }
    }


    override fun getItemCount(): Int {
        return ddayBucketTotalList.ddayBucketEachListItem.size
    }

}
