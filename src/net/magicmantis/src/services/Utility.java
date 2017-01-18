package net.magicmantis.src.services;

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Properties;

/**
 * Utility Class: Class for extra functions
 */
public class Utility {

    public static String getIP() {

        Properties prop = new Properties();
        InputStream input = null;

        try {
            if (!new File("config.properties").exists()) {
                try {
                    FileOutputStream output = new FileOutputStream("config.properties");
                    prop.setProperty("ip", "localhost");
                    prop.store(output, null);
                    output.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            input = new FileInputStream("config.properties");
            prop.load(input);
            input.close();
            return prop.getProperty("ip");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Gson getClassGson() {
        return new GsonBuilder().registerTypeAdapter(Class.class, new ClassTypeAdapter()).setPrettyPrinting().create();
    }

    public static class ClassTypeAdapter implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

        @Override
        public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getName());
        }

        @Override
        public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                return Class.forName(json.getAsString());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
