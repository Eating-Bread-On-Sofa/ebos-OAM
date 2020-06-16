package cn.edu.bjtu.ebosoam.model;

import java.util.Date;

public class LocalLog {
    private Date date;
    private String source;
    private String category;
    private String operation;
    private String message;
    private String gatewayName;

    public LocalLog(Date date, String source, String category, String operation, String message, String gatewayName) {
        this.date = date;
        this.source = source;
        this.category = category;
        this.operation = operation;
        this.message = message;
        this.gatewayName = gatewayName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

}
