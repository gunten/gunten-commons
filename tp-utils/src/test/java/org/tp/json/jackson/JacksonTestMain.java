package org.tp.json.jackson;


import com.fasterxml.jackson.core.*;

import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2018/11/20
 */
public class JacksonTestMain {

    /**
     * 测试报文
     * {
     * "id": 1,
     * "name": "zuzhi1",
     * "address":{
     * "id": 11,
     * "road": "地址"
     * },
     * "users":[
     * {
     * "id": 12,
     * "username":"用户12"
     * },
     * {
     * "id": 13,
     * "username":"用户13"
     * }
     * ]
     * }
     */


    void streamAPIDemo() {

        JsonFactory jsonFactory = new JsonFactory();

        try {
            JsonGenerator jsonGenerator = jsonFactory.createGenerator(new File("student.json"), JsonEncoding.UTF8);
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", "Mahesh Kumar");
            jsonGenerator.writeNumberField("age", 21);
            jsonGenerator.writeBooleanField("verified", false);
            jsonGenerator.writeFieldName("marks");
            jsonGenerator.writeStartArray(); // [
            jsonGenerator.writeNumber(100);
            jsonGenerator.writeNumber(90);
            jsonGenerator.writeNumber(85);
            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();
            jsonGenerator.close();

            //result student.json
            //{
            //  "name":"Mahesh Kumar",
            //  "age":21,
            //  "verified":false,
            //  "marks":[100,90,85]
            //}

            JsonParser jsonParser = jsonFactory.createParser(new File("student.json"));

            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                //get the current token
                String fieldname = jsonParser.getCurrentName();
                if ("name".equals(fieldname)) {
                    //move to next token
                    jsonParser.nextToken();
                    System.out.println(jsonParser.getText());
                }
                if ("age".equals(fieldname)) {
                    //move to next token
                    jsonParser.nextToken();
                    System.out.println(jsonParser.getNumberValue());
                }
                if ("verified".equals(fieldname)) {
                    //move to next token
                    jsonParser.nextToken();
                    System.out.println(jsonParser.getBooleanValue());
                }
                if ("marks".equals(fieldname)) {
                    //move to [
                    jsonParser.nextToken();
                    // loop till token equal to "]"
                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        System.out.println(jsonParser.getNumberValue());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /* NOTE:
        Jackson JSON Trees are singly-linked, there is no parent linkage. This has the benefit of reduced memory usage (since many leaf-level nodes can be shared) and slightly more efficient building,but downside of not being able to traverse up and down the hierarchy.
        So you will need to keep track of that yourself, or use your own tree model
     */
    public static void main(String[] args) throws IOException {
        String json = "{\"id\":1,\"name\":\"zuzhi1\",\"address\":{\"id\":11,\"road\":\"地址\"},\"users\":[{\"id\":12,\"username\":\"用户12\"},{\"id\":13,\"username\":\"用户13\"}]}";

       /* JsonNode jsonNode = new ObjectMapper().readTree(json);
        Iterator<JsonNode> it = jsonNode.elements();
        while (it.hasNext()) {
            JsonNode node = it.next();
            System.out.println(node.getNodeType());
            if (node.getNodeType().equals(JsonNodeType.OBJECT)) {
                Iterator<Map.Entry<String, JsonNode>> fieldIter = node.fields();
                while (fieldIter.hasNext())
                {
                    Map.Entry<String, JsonNode> entry = fieldIter.next();
                    System.out.println(entry.getKey() + ":" + entry.getValue());
                }
            }
            if (node.isArray())
            {
                Iterator<JsonNode> listIter = node.iterator();
                while (listIter.hasNext())
                {
                    System.out.println(listIter.next());
                }
            }
        }*/

        new JacksonTestMain().streamAPIDemo();
    }

}
