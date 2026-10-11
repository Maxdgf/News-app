package com.maxdgf.newspaper.domain.model

import com.maxdgf.newspaper.domain.model.reponse.ContentResponse
import com.maxdgf.newspaper.domain.model.reponse.ErrorResponse

sealed class ArticleResponseResult {
    data class Success(val contentResponse: ContentResponse, val page: Int) : ArticleResponseResult()
    data class Error(val errorResponse: ErrorResponse) : ArticleResponseResult()
}
