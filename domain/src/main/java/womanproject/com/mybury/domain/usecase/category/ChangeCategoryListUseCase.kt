package womanproject.com.mybury.domain.usecase.category

import womenproject.com.mybury.data.model.ChangeCategoryStatusRequest
import womenproject.com.mybury.data.model.SimpleResponse
import womenproject.com.mybury.data.repository.MyBuryRepository
import javax.inject.Inject

class ChangeCategoryListUseCase @Inject constructor(
    private val myBuryRepository: MyBuryRepository
) {
    suspend fun invoke(token: String, categoryId: ChangeCategoryStatusRequest): SimpleResponse {
        return myBuryRepository.changeCategoryList(token, categoryId)
    }

}