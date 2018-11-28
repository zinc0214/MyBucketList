package womenproject.com.mybury.data

import androidx.lifecycle.LiveData
import androidx.room.Dao

/**
 * Created by HanAYeon on 2018. 11. 27..
 */

@Dao
interface BucketListDao {

    fun getBucketList() : LiveData<List<BucketList>>
}