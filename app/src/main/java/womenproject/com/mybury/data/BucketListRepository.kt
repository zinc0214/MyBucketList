package womenproject.com.mybury.data

/**
 * Created by HanAYeon on 2018. 11. 27..
 */

class BucketListRepository {

    private var nextItem = 1

    fun getItemsPage(pageSize: Int = 20): List<String> {

        val items = mutableListOf<String>()
        val lastItem = nextItem + pageSize - 1

        for (i in nextItem..lastItem) {
            items.add("Item $i")
        }

        nextItem = lastItem + 1

        return items
    }
}

