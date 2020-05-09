package it.polimi.ingsw.network;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import it.polimi.ingsw.network.events.Event;

/**
 *  Class that provides methods to serialize and deserialize events sent and received throughout our MVC architecture
 */


public final class JsonHelper {

    public JsonHelper(){};

    public static String serialization(Event event){

        if(event == null){
            throw new NullPointerException();}

        GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();
        Gson gson = builder.create();
        JsonElement jsonEl = gson.toJsonTree(event);
        jsonEl.getAsJsonObject().addProperty("category", event.getClass().toString().replace("class ", ""));;
        return gson.toJson(jsonEl);

    }



    public static Object deserialization(String input) throws JsonSyntaxException {
        if(input == null){
            throw new NullPointerException();
        }
        try {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(input).getAsJsonObject();
        String category = obj.get("category").getAsString();
        obj.remove("category");
        Class<?> classType = Class.forName(category);
        return classType.cast(gson.fromJson(input, classType));
        } catch (ClassNotFoundException | IllegalStateException ignored) {
        }
        return null;
    }

}
