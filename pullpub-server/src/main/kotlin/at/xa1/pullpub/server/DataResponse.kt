package at.xa1.pullpub.server

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

data class DataResponse<TData>(
    val statusCode: HttpStatusCode,
    val data: TData
)

suspend fun <TData: Any> ApplicationCall.respondData(dataResponse: DataResponse<TData>) {
    respond(dataResponse.statusCode, dataResponse.data)
}
