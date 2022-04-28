package womanproject.com.mybury.domain.usecase.category

import womenproject.com.mybury.data.model.AddCategoryRequest
import womenproject.com.mybury.data.model.SimpleResponse
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class AddCategoryItemUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(token: String, categoryId: AddCategoryRequest): SimpleResponse {
        return myBuryRepository.addNewCategoryItem(token, categoryId)
    }

}