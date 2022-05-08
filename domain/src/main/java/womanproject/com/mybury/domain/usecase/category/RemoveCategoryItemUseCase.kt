package womanproject.com.mybury.domain.usecase.category

import womenproject.com.mybury.data.model.RemoveCategoryRequest
import womenproject.com.mybury.data.model.SimpleResponse
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class RemoveCategoryItemUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(
        token: String,
        removeCategoryRequest: RemoveCategoryRequest
    ): SimpleResponse {
        return myBuryRepository.removeCategoryItem(token, removeCategoryRequest)
    }

}