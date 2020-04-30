package it.polimi.ingsw.network;

import com.google.gson.*;
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



    public static Object deserialization(String input){
        if(input == null){
            throw new NullPointerException();
        }
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(input).getAsJsonObject();
        String category = obj.get("category").getAsString();
        obj.remove("category");
        Class<?> classType = null;
        try {
            classType = Class.forName(category);
        } catch (ClassNotFoundException e) {
            System.out.println();
        }
        return classType.cast(gson.fromJson(input, classType));


    }

}
