package com.example.Model.Order;

import java.sql.Date;

public class Order_status_history {
    private int history_id;
    private int order_id;
    private String previous_status; // Using String for ENUM
    private String new_status; // Using String for ENUM
    private Date updated_at;

    public Order_status_history(int history_id, int order_id, String previous_status, String new_status, Date updated_at) {
        this.history_id = history_id;
        this.order_id = order_id;
        this.previous_status = previous_status;
        this.new_status = new_status;
        this.updated_at = updated_at;
    }

    public Order_status_history(int order_id, String previous_status, String new_status, Date updated_at) {
        this.order_id = order_id;
        this.previous_status = previous_status;
        this.new_status = new_status;
        this.updated_at = updated_at;
    }

    public Order_status_history() {
    }

    public int getHistory_id() {
        return history_id;
    }

    public void setHistory_id(int history_id) {
        this.history_id = history_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getPrevious_status() {
        return previous_status;
    }

    public void setPrevious_status(String previous_status) {
        this.previous_status = previous_status;
    }

    public String getNew_status() {
        return new_status;
    }

    public void setNew_status(String new_status) {
        this.new_status = new_status;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
    
    
}
