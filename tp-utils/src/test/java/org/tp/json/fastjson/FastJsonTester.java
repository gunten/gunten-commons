package org.tp.json.fastjson;

import com.alibaba.fastjson.JSONReader;
import com.fasterxml.jackson.core.JsonFactory;

import java.io.FileReader;
import java.io.IOException;


public class FastJsonTester {
    public static void main(String args[]){
        FastJsonTester tester = new FastJsonTester();
        try {
            JsonFactory jsonFactory = new JsonFactory();

            /*JsonGenerator jsonGenerator = jsonFactory.createGenerator(new File("student.json"), JsonEncoding.UTF8);
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
            jsonGenerator.close();*/

            //result student.json
            //{
            //  "name":"Mahesh Kumar",
            //  "age":21,
            //  "verified":false,
            //  "marks":[100,90,85]
            //}

            /*JsonParser jsonParser = jsonFactory.createParser(new File("student.json"));

            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                //get the current token
                String fieldname = jsonParser.getCurrentName();
                if ("name".equals(fieldname)) {
                    //move to next token
                    jsonParser.nextToken();
                    System.out.println(jsonParser.getText());
                }
                if("age".equals(fieldname)){
                    //move to next token
                    jsonParser.nextToken();
                    System.out.println(jsonParser.getNumberValue());
                }
                if("verified".equals(fieldname)){
                    //move to next token
                    jsonParser.nextToken();
                    System.out.println(jsonParser.getBooleanValue());
                }
                if("marks".equals(fieldname)){
                    //move to [
                    jsonParser.nextToken();
                    // loop till token equal to "]"
                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        System.out.println(jsonParser.getNumberValue());
                    }
                }
            }*/

            JSONReader reader = new JSONReader(new FileReader("student.json"));
            reader.startArray();
            while (reader.hasNext()) {
//                String key = reader.readString();
//                Person vo = reader.readObject(Person.class);
//                System.out.println(key + "：" + vo);
                Person vo = reader.readObject(Person.class);
                System.out.println(vo);
            }
            reader.endArray();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
