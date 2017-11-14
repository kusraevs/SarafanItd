package ru.itd.sarafan.rest.factory;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Kusraev Soslan on 6/29/16.
 */
public class MockInterceptor implements Interceptor {
    private Context context;

    public MockInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        /*
        final String query = chain.request().url().toString();
        String responseJSONStr = null;
        if (query.contains("chats"))
            responseJSONStr = ResourceFile.readTextFromFile(context, R.raw.chats_jsonapi);
        if (responseJSONStr != null){
            Response response = new Response.Builder()
                    .code(200)
                    .message(responseJSONStr)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), responseJSONStr.getBytes()))
                    .addHeader("content-type", "application/json")
                    .build();
            return response;
        }*/

        return chain.proceed(chain.request());
    }
}
