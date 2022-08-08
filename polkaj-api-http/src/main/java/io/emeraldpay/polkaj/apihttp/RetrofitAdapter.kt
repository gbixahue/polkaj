package io.emeraldpay.polkaj.apihttp

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.emeraldpay.polkaj.api.RpcCall
import io.emeraldpay.polkaj.api.RpcCallAdapter
import io.emeraldpay.polkaj.api.RpcCoder
import io.emeraldpay.polkaj.api.RpcException
import io.emeraldpay.polkaj.json.jackson.PolkadotModule
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.CompletableFuture

/**
 * Created by Anton Zhilenkov on 16/06/2022.
 */
class RetrofitAdapter(
	baseUrl: String,
	okHttpClient: OkHttpClient,
	private val rpcCoder: RpcCoder,
) : RpcCallAdapter {

	private var isClosed = false

	private val service: PolkadotRetrofitApi = Retrofit.Builder()
		.baseUrl(baseUrl)
		.addConverterFactory(createConverterFactory())
		.client(okHttpClient)
		.build()
		.create(PolkadotRetrofitApi::class.java)

	private fun createConverterFactory(): JacksonConverterFactory {
		val mapper = ObjectMapper().registerModule(PolkadotModule())
		return JacksonConverterFactory.create(mapper)
	}

	var onClose: (() -> Unit)? = null

	override fun <T> produceRpcFuture(call: RpcCall<T>): CompletableFuture<T> {
		if (isClosed) {
			return CompletableFuture<T>().failedFuture(IllegalStateException("Client is already closed"))
		}

		val objectMapper: ObjectMapper = rpcCoder.objectMapper
		val id: Int = rpcCoder.nextId()
		val type = call.getResultType(objectMapper.typeFactory)

		return try {
			val requestBody = RequestBody.create(MediaType.get("application/json"), rpcCoder.encode(id, call))
			val responseAny = service.sendRAny(requestBody)
			val responseRBody = service.sendRBody(requestBody)

			val result: T = rpcCoder.decode(id, responseAny.toString(), type) as T

			return CompletableFuture.completedFuture(result)

//		  try {
//            HttpRequest.Builder request = this.request.copy()
//                    .POST(HttpRequest.BodyPublishers.ofByteArray(rpcCoder.encode(id, call)));
//            return httpClient.sendAsync(request.build(), HttpResponse.BodyHandlers.ofString())
//                    .thenApply(HttpResponse::body)
//                    .thenApply(content -> rpcCoder.decode(id, content, type));
//        } catch (JsonProcessingException e) {
//            return CompletableFuture.failedFuture(
//                    new RpcException(-32600, "Unable to encode request as JSON: " + e.getMessage(), e)
//            );
//        } catch (CompletionException e){
//            return CompletableFuture.failedFuture(e);
//        }
		} catch (e: JsonProcessingException) {
			val exception = RpcException(-32600, "Unable to encode request as JSON: " + e.message, e)
			CompletableFuture<T>().failedFuture(exception)
		} catch (e: Exception) {
			CompletableFuture<T>().failedFuture(e)
		}
	}

	override fun close() {
		if (isClosed) return

		isClosed = true
		onClose?.invoke()
	}
}

private fun <T> CompletableFuture<T>.failedFuture(ex: Throwable): CompletableFuture<T> {
	this.completeExceptionally(ex)
	return this
}

private interface PolkadotRetrofitApi {
	@POST()
	fun sendRAny(@Body bytes: RequestBody): Any

	@POST()
	fun sendRBody(@Body bytes: RequestBody): ResponseBody
}