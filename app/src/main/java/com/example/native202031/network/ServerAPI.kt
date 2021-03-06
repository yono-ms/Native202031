package com.example.native202031.network

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

class ServerAPI {

    private val logger = LoggerFactory.getLogger(javaClass.simpleName)

    private val urlBase = "https://api.github.com"
    private val urlUsers = "$urlBase/users/"
    private val urlRepos = "$urlBase/repos/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    fun getUsersUrl(login: String?): String {
        return "$urlUsers$login"
    }

    fun getCommitsUrl(login: String?, repo: String?): String {
        return "$urlRepos$login/$repo/commits"
    }

    suspend fun <T> getDecode(urlString: String, deserializer: DeserializationStrategy<T>): T {
        val text = get(urlString)
        return json.decodeFromString(deserializer, text)
    }

    suspend fun get(urlString: String): String {
        logger.info("request START")
        val (request, response, result) = Fuel.get(urlString).awaitStringResponseResult()
        logger.info("$request")
        logger.info("$response")
        return result.get()
    }
}