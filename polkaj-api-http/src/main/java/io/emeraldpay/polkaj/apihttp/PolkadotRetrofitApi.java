package io.emeraldpay.polkaj.apihttp;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Anton Zhilenkov on 30/08/2022.
 */
interface PolkadotRetrofitApi {
    @POST(".")
    Call<ResponseBody> post(@Body RequestBody requestBody);
}
