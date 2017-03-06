package dev.notify;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dream on 3/7/2017 AD.
 */

public class PostDataRegister {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    String dataToJson(String firstname, String lastname, String tel, String email, String username, String password){
        return "{" +
                "'firstname':'" + firstname + "',"+
                "'lastname' : '" + lastname + "'," +
                "'tel' : '" + tel + "',"+
                "'email' : '" + email + "',"+
                "'username' : '" + username + "'," +
                "'password' : '" + password + "',"+
                "'admin' : '0' "+
                "}";
    }
}
