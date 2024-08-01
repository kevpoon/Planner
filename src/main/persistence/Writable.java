package persistence;

import org.json.JSONObject;

//referenced from JSON demo
//All referenced off of JsonSerializationDemo for CPSC210
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
