package org.roiugit.rules.gsonadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.roiugit.rules.Rule;

import java.io.IOException;

public class RuleTypeAdapter extends TypeAdapter<Rule> {

    @Override
    public void write(JsonWriter out, Rule rule) throws IOException {
        out.beginObject();
        out.name("type");
        out.value(rule.getClass().getName());
        out.endObject();
    }

    @Override
    public Rule read(JsonReader in) throws IOException {
        in.beginObject();
        String type;
        Rule rule = null;
        while (in.hasNext()) {
            if (in.nextName().equals("type")) {
                type = in.nextString();
                try {
                    rule = (Rule) Class.forName(type).getConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        in.endObject();
        return rule;
    }
}
