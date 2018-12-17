package com.mdc.servicelayer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@Component
public class Utilities {

    //protected final Log logger = LogFactory.getLog(getClass());
    Logger logger = java.util.logging.Logger.getLogger(Utilities.class.getName());


    //private String tempJWT = "eyJraWQiOiJFOHdQSkVqTW43TnBzdmtVb0VwOTRWM3hoUWgzcUNlS284cHlDUGl4ME5vIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiIwMHVlNWFuYzlzalRGaHQwOTBoNyIsIm5hbWUiOiJtaWNoYWVsIGNvb2xleSIsImVtYWlsIjoibWljaGFlbC5jb29sZXlAZGF1Z2hlcnR5LmNvbSIsInZlciI6MSwiaXNzIjoiaHR0cHM6Ly9jYXJnaWxsY3VzdG9tZXItdWF0Lm9rdGFwcmV2aWV3LmNvbSIsImF1ZCI6IjBvYWR3cTc4YWZQdXVtS0ZxMGg3IiwiaWF0IjoxNTE5NzQ0Mjk3LCJleHAiOjE1MTk3NDc4OTcsImp0aSI6IklELmZIYjRPSUQ5YTR6b2xQYmxmLWNEN2V4Nm1BXy1aZ3AxS0NLNDB1TUd0VFkiLCJhbXIiOlsicHdkIl0sImlkcCI6IjAwb2MzeHBqeG1SZUoxRjZaMGg3Iiwibm9uY2UiOiJwbDNKUFBxTnlXMWVDU0YzVGUycEZxZmdoMjlLWE1kc2pNQUE4QkNHZjd2ZjhJMGF1TmtlVVhMZmVXSVpZNkhkIiwicHJlZmVycmVkX3VzZXJuYW1lIjoibWljaGFlbC5jb29sZXlAZGF1Z2hlcnR5LmNvbSIsImF1dGhfdGltZSI6MTUxOTc0NDI5NywiYXRfaGFzaCI6IlFUeXVzUG1wbWJ5Y24wR3VrdkZhbmcifQ.AeIeRF0xZ1TdbG9Kus3If4slKAvBJdnJuRp7mbMpQGizlY7WPVygiOtYeJddX5EZWHMcSXbZh5s2mH1zc7jpyHtLXSAnIBAr96oKumtM1Jd5Hex3JyFlk1fxr9YrhSaXn169yZ2W3PVCn7oJDOAj_ZrpWejQpiECaQMl1VIHoSxzkw94";

    private static String cssJWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJDYXJnaWxsIENTUyIsImlhdCI6MTU0MDQwODY2NiwiZXhwIjoxNTcxOTQ0Nzk4LCJhdWQiOiJjc3NzZXJ2aWNlLmRldi5jZ2xjbG91ZC5pbiIsInN1YiI6ImNzc3VzZXJAY2FyZ2lsLmNvbSIsIkdpdmVuTmFtZSI6ImNzc3VzZXItZmlyc3RuYW1lIiwiU3VybmFtZSI6ImNzc3VzZXItbGFzdG5hbWUiLCJFbWFpbCI6ImNzc3VzZXJAY2FyZ2lsLmNvbSIsIlJvbGUiOiJ1c2VyIn0.tjFqQA1tRkyeUla5xw8Eimddv_EmkKwkm457YUfsJRo";
    private static String invalidLoginErrorMsg = "Invalid username or password";

    public static String getInvalidLoginErrorMsg() {
        return invalidLoginErrorMsg;
    }

    public static String getCssJWT() {
        return cssJWT;
    }


    public JSONArray convertToJSONArray(ResultSet rs ) throws SQLException, JSONException
    {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();

        while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i=1; i<numColumns+1; i++) {
                String column_name = rsmd.getColumnName(i);

                if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
                    obj.put(column_name, rs.getArray(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
                    obj.put(column_name, rs.getInt(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
                    obj.put(column_name, rs.getBoolean(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
                    obj.put(column_name, rs.getBlob(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
                    obj.put(column_name, rs.getDouble(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
                    obj.put(column_name, rs.getFloat(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
                    obj.put(column_name, rs.getInt(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
                    obj.put(column_name, rs.getNString(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
                    obj.put(column_name, rs.getString(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
                    obj.put(column_name, rs.getInt(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
                    obj.put(column_name, rs.getInt(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
                    obj.put(column_name, rs.getDate(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
                    obj.put(column_name, rs.getTimestamp(column_name));
                }
                else{
                    obj.put(column_name, rs.getObject(column_name));
                }
            }

            json.put(obj);
        }

        return json;
    }

    public List<HashMap<String,Object>> convertToListOfHashMaps(ResultSet rs ) throws SQLException, JSONException
    {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();

        logger.info("convert to list of hash maps. columns: " + columns);

        List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

        while (rs.next()) {

            HashMap<String,Object> row = new HashMap<String, Object>(columns);
            for(int i=1; i<=columns; ++i) {
                row.put(md.getColumnName(i),rs.getObject(i));
            }

            list.add(row);
        }

        return list;
    }

    public static java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }

    public String convertResultSetToJSONString(ResultSet resultSet) {

        String jsonResult = "";
        try {

            JSONArray json = convertToJSONArray(resultSet);

            jsonResult = json.toString();

            logger.info(jsonResult);

        } catch (Exception e) {

            logger.warning("Error: " + e.getMessage());
        }

        return jsonResult;

    }

    public String createJSONResultString(Boolean result, String trueDescription, String falseDescription) {

        String jsonResult = "";

        if (result) {

            jsonResult = "{\"result\":\"" + trueDescription + "\"}";
            logger.info(trueDescription);

        } else {

            jsonResult = "{\"result\":\"" + falseDescription + "\"}";
            logger.info(falseDescription);
        }

        return jsonResult;
    }

}
