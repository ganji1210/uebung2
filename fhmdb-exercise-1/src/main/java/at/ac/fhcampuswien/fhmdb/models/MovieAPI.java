package at.ac.fhcampuswien.fhmdb.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MovieAPI {
    // mainURL of api
    static String mainURL = "https://prog2.fh-campuswien.ac.at";

    public List<Movie> fetchMovies() {
        String url = mainURL + "/movies";

        return getMovies(url);
    }

    public List<Movie> searchMovies(Map<String, String> parameters) {
        String url = mainURL + "/movies";
        if (parameters != null) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                if (!url.contains("?"))
                    url = url.concat("?").concat(entry.getKey()).concat("=").concat(entry.getValue());
                else
                    url = url.concat("&").concat(entry.getKey()).concat("=").concat(entry.getValue());
            }
        }
        return getMovies(url);
    }

    private static List<Movie> getMovies(String url) {
        List<Movie> movies = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "MyApp/1.0")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            movies = objectMapper.readValue(responseBody, new TypeReference<List<Movie>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movies;
    }
}