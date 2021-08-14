package study.core.base;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

    public static ObjectMapper getMapper() {
        JsonFactory factory = new JsonFactory();
        factory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        return new ObjectMapper(factory);
    }
}
