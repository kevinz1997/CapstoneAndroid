package workflow.capstone.capstoneproject.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import workflow.capstone.capstoneproject.entities.ActionType;
import workflow.capstone.capstoneproject.entities.Connection;
import workflow.capstone.capstoneproject.entities.Request;
import workflow.capstone.capstoneproject.entities.StaffRequestAction;
import workflow.capstone.capstoneproject.entities.UserRequestAction;

public class HandleFormRequest {
    @SerializedName("request")
    @Expose
    private Request request;
    @SerializedName("userRequestAction")
    @Expose
    private UserRequestAction userRequestAction;
    @SerializedName("staffRequestActions")
    @Expose
    private List<StaffRequestAction> staffRequestActions = null;
    @SerializedName("connections")
    @Expose
    private List<Connection> connections = null;
    @SerializedName("actionType")
    @Expose
    private ActionType actionType;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public UserRequestAction getUserRequestAction() {
        return userRequestAction;
    }

    public void setUserRequestAction(UserRequestAction userRequestAction) {
        this.userRequestAction = userRequestAction;
    }

    public List<StaffRequestAction> getStaffRequestActions() {
        return staffRequestActions;
    }

    public void setStaffRequestActions(List<StaffRequestAction> staffRequestActions) {
        this.staffRequestActions = staffRequestActions;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

}
