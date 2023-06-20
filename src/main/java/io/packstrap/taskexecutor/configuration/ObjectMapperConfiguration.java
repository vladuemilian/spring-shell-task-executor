package io.packstrap.taskexecutor.configuration;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Configuration
public class ObjectMapperConfiguration {

    @Autowired
    public void customize(ObjectMapper objectMapper) {
        objectMapper.configOverride(BigDecimal.class)
                .setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.NUMBER));
    }

    public static class LocalDateDeserializer extends JsonDeserializer<Map<LocalDate, BigDecimal>> {
        private static final ObjectMapper mapper = new ObjectMapper();

        @Override
        public Map<LocalDate, BigDecimal> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            ObjectCodec codec = jsonParser.getCodec();
            JsonNode node = codec.readTree(jsonParser);
            Map<LocalDate, BigDecimal> dates = mapper.convertValue(node, Map.class);
            return dates;
        }
    }
}
