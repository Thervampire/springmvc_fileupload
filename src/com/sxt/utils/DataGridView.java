package com.sxt.utils;

import java.util.List;

public class DataGridView {
    private Integer total;
    private List rows;

    public DataGridView() {
    }

    public DataGridView(Integer total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
