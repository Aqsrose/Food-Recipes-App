package com.example.project.controllers;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignInController {
    String responseBody;
    int responseCode;
    public SignInController(){}
    public SignInController(String responseBody, int responseCode){
        this.responseBody = responseBody;
        this.responseCode = responseCode;
    }
    public String getResponseBody() {
        return responseBody;
    }

    public int getResponseCode() {
        return responseCode;
    }

    //declaring JSON type constant for okHttp
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    //declaring http client
    final OkHttpClient client = new OkHttpClient();

    //method that will actually post the data, can also defined a get function
    public SignInController post(String url, String json) throws IOException {
        //preparing request body
        RequestBody body = RequestBody.create(json, JSON);

        //building the request and appending request body
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try(Response response = client.newCall(request).execute()){
            return new SignInController(response.body().string(), response.code());
        }
        catch(Exception exception){
            return new SignInController(null, -1);
        }
    }
    public String buildJson(String username, String password) {
        return "{" +
                " \"username\" : \"" + username + "\"," +
                " \"password\": \"" + password + "\"" +
                "}";
    }
}
