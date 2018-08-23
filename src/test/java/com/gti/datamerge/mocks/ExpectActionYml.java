/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.mocks;

import com.gti.datamerge.Action;
import com.gti.datamerge.database.Row;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Xachman
 */
public class ExpectActionYml {
    private String type;
    private String table;
    private Map<String, String> row;    

    public ExpectActionYml(String type, String table, Map<String, String> row) {
        this.type = type;
        this.table = table;
        this.row = row;
    }

    public ExpectActionYml() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Map<String, String> getRow() {
        return row;
    }

    public void setRow(Map<String, String> row) {
        this.row = row;
    }

    public Action getAction() {
        Row row = new Row();

        for(String key: getRow().keySet()) {
            row.put(key, getRow().get(key));
        }
        
        return new Action(Action.INSERT, row, getTable());
    }
}