package womanproject.com.mybury.domain.usecase.category

import womenproject.com.mybury.data.model.EditCategoryNameRequest
import womenproject.com.mybury.data.model.SimpleResponse
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class EditCategoryItemNameUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(token: String, categoryId: EditCategoryNameRequest): SimpleResponse {
        return myBuryRepository.editCategoryItemName(token, categoryId)
    }
}