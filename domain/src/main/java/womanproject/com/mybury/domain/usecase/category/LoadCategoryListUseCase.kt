package womanproject.com.mybury.domain.usecase.category

import womenproject.com.mybury.data.model.BucketCategory
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class LoadCategoryListUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(token: String, userId: String): BucketCategory {
        return myBuryRepository.loadCategoryList(token, userId)
    }
}