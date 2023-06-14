package com.example.project.adapters;

import okhttp3.*;

public class ChatGPTClient {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static String sendRequest(String message) {
        OkHttpClient client = new OkHttpClient();

        //openAI API key
        String apiKey = "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";

        //the api url
        String url = "https://api.openai.com/v1/chat/completions";

        // Build the request body
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("model", "gpt-3.5-turbo")
                .addFormDataPart("messages", "[{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}, {\"role\": \"user\", \"content\": \"" + message + "\"}]")
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();

        try {
            // Send the request and receive the response
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return responseBody;
            } else {
                // Handle error response
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
