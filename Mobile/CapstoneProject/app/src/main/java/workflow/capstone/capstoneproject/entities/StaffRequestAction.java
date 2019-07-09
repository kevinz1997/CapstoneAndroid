package workflow.capstone.capstoneproject.entities;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StaffRequestAction {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("createDate")
    @Expose
    private String createDate;

    @SerializedName("requestValues")
    @Expose
    private List<RequestValue> requestValues;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<RequestValue> getRequestValues() {
        return requestValues;
    }

    public void setRequestValues(List<RequestValue> requestValues) {
        this.requestValues = requestValues;
    }

}