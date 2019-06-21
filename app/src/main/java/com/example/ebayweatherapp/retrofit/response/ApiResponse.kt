package com.example.ebayweatherapp.retrofit.response

object ApiResponse {
    data class Result(val query: Query)
    data class Query(val searchinfo: SearchInfo)
    data class SearchInfo(val totalhits: Int)
}