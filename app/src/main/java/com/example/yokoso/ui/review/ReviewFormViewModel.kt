package com.example.yokoso.ui.review

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yokoso.data.repository.ReviewsRepository
import kotlinx.coroutines.launch

class ReviewFormViewModel(
    private val placeId: String,
    private val repository: ReviewsRepository = ReviewsRepository()
) : ViewModel() {

    var rating by mutableStateOf(0)
        private set
    var text by mutableStateOf("")
        private set
    var ratingMissing by mutableStateOf(false)
        private set
    var isSubmitting by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var isDone by mutableStateOf(false)
        private set

    fun onRatingChange(value: Int) {
        rating = value
        ratingMissing = false
    }

    fun onTextChange(value: String) {
        text = value
    }

    fun submit() {
        // оценка обязательна, комментарий — нет
        if (rating == 0) {
            ratingMissing = true
            return
        }
        viewModelScope.launch {
            isSubmitting = true
            error = null
            try {
                repository.addReview(placeId, rating, text.trim())
                isDone = true
            } catch (e: Exception) {
                error = e.message ?: "Не удалось отправить отзыв"
            }
            isSubmitting = false
        }
    }
}