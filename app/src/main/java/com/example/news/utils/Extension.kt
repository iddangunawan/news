package com.example.news.utils

import org.json.JSONObject

fun parseErrorMessageFromJson(errorBody: String?): String {
    return try {
        val json = errorBody?.let { JSONObject(it) }
        val status = json?.optString("status", "")
        val code = json?.optString("code", "")
        val message = json?.optString("message", "Unknown error")

        "Status: $status\nCode: $code\nMessage: $message"
    } catch (e: Exception) {
        "Error parsing JSON: ${e.message}"
    }
}