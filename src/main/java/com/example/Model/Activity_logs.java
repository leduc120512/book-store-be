package com.example.Model;

import java.sql.Date;

public class Activity_logs {
    private int log_id;
    private int user_id;
    private String action_type; // Using String for ENUM
    private String target_table;
    private int target_id;
    private String action_description;
    private Date action_time;

    public Activity_logs() {
    }

    
    
    public Activity_logs(int log_id, int user_id, String action_type, String target_table, int target_id, String action_description, Date action_time) {
        this.log_id = log_id;
        this.user_id = user_id;
        this.action_type = action_type;
        this.target_table = target_table;
        this.target_id = target_id;
        this.action_description = action_description;
        this.action_time = action_time;
    }
    
    
    
    
    //no id primary

    public Activity_logs(int user_id, String action_type, String target_table, int target_id, String action_description, Date action_time) {
        this.user_id = user_id;
        this.action_type = action_type;
        this.target_table = target_table;
        this.target_id = target_id;
        this.action_description = action_description;
        this.action_time = action_time;
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getTarget_table() {
        return target_table;
    }

    public void setTarget_table(String target_table) {
        this.target_table = target_table;
    }

    public int getTarget_id() {
        return target_id;
    }

    public void setTarget_id(int target_id) {
        this.target_id = target_id;
    }

    public String getAction_description() {
        return action_description;
    }

    public void setAction_description(String action_description) {
        this.action_description = action_description;
    }

    public Date getAction_time() {
        return action_time;
    }

    public void setAction_time(Date action_time) {
        this.action_time = action_time;
    }
    
    
    
    
    
}
