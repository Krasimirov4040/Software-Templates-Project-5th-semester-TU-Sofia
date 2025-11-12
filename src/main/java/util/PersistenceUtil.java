package util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.student.media_library.model.Book;
import com.student.media_library.model.Film;
import com.student.media_library.model.Media;
import com.student.media_library.model.Music;
import patterns.BookAdapter;
import patterns.LegacyBook;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersistenceUtil {
    private static final String FILE_PATH = "library.json";
    private static final Gson gson = new Gson();


    public static void save(List<Media> mediaList) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(mediaList, writer);
            System.out.println("Library saved to " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error saving library: " + e.getMessage());
        }
    }

    public static List<Media> load() {
        List<Media> mediaList = new ArrayList<>();
        try (FileReader reader = new FileReader(FILE_PATH)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                for (JsonElement elem : jsonArray) {
                    if (elem.isJsonObject()) {
                        JsonObject obj = elem.getAsJsonObject();
                        Media media = createMediaFromJson(obj);
                        if (media != null) {
                            mediaList.add(media);
                        }
                    }
                }
            }
            System.out.println("Library loaded from " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("No library file found or error loading: " + e.getMessage() + ". Starting empty.");
        }
        return mediaList;
    }

    private static Media createMediaFromJson(JsonObject obj) {

        if (obj.has("legacyBook")) {
            // For BookAdapter, assume legacyBook is nested
            JsonObject legacyObj = obj.get("legacyBook").getAsJsonObject();
            LegacyBook legacy = gson.fromJson(legacyObj, LegacyBook.class);
            return new BookAdapter(legacy);
        }


        if (!obj.has("title") || !obj.has("genre") || !obj.has("rating") || !obj.has("year")) {
            System.err.println("Missing required fields in JSON object: " + obj + ". Skipping.");
            return null;
        }
        String title = obj.get("title").getAsString();
        String genre = obj.get("genre").getAsString();
        double rating = obj.get("rating").getAsDouble();
        int year = obj.get("year").getAsInt();

        if (obj.has("author") && obj.has("pageCount")) {
            String author = obj.get("author").getAsString();
            int pageCount = obj.get("pageCount").getAsInt();
            return new Book(title, genre, rating, year, author, pageCount);
        } else if (obj.has("director") && obj.has("durationMinutes")) {
            String director = obj.get("director").getAsString();
            int durationMinutes = obj.get("durationMinutes").getAsInt();
            return new Film(title, genre, rating, year, director, durationMinutes);
        } else if (obj.has("artist") && obj.has("album") && obj.has("trackLengthSeconds")) {
            String artist = obj.get("artist").getAsString();
            String album = obj.get("album").getAsString();
            int trackLengthSeconds = obj.get("trackLengthSeconds").getAsInt();
            return new Music(title, genre, rating, year, artist, album, trackLengthSeconds);
        }
        System.err.println("Unknown media type for: " + title + ". Skipping.");
        return null;
    }
}