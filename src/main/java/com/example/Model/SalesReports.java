package com.example.Model;

import java.sql.Date;

public class SalesReports {
    private int report_id;
    private int month;
    private int year;
    private int total_orders;
    private double total_revenue;
    private int total_products_sold;
    private Date created_at;

    public SalesReports() {
    }

    
    
    public SalesReports(int report_id, int month, int year, int total_orders, double total_revenue, int total_products_sold, Date created_at) {
        this.report_id = report_id;
        this.month = month;
        this.year = year;
        this.total_orders = total_orders;
        this.total_revenue = total_revenue;
        this.total_products_sold = total_products_sold;
        this.created_at = created_at;
    }

    public SalesReports(int month, int year, int total_orders, double total_revenue, int total_products_sold, Date created_at) {
        this.month = month;
        this.year = year;
        this.total_orders = total_orders;
        this.total_revenue = total_revenue;
        this.total_products_sold = total_products_sold;
        this.created_at = created_at;
    }

    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTotal_orders() {
        return total_orders;
    }

    public void setTotal_orders(int total_orders) {
        this.total_orders = total_orders;
    }

    public double getTotal_revenue() {
        return total_revenue;
    }

    public void setTotal_revenue(double total_revenue) {
        this.total_revenue = total_revenue;
    }

    public int getTotal_products_sold() {
        return total_products_sold;
    }

    public void setTotal_products_sold(int total_products_sold) {
        this.total_products_sold = total_products_sold;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
    
    
}
