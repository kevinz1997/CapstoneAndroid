package workflow.capstone.capstoneproject.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ActionValue implements Serializable {

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("value")
    @Expose
    private String value;

    public ActionValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
