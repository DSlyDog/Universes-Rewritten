package net.whispwriting.universes.utils.sql;

import java.sql.ResultSet;

public class SQLResult {

    private boolean sqlBooleanResult;
    private ResultSet sqlResultSet;
    private boolean isBooleanResult;
    private int resultInt;

    public SQLResult(boolean result){
        this.sqlBooleanResult = result;
        this.isBooleanResult = true;
    }

    public SQLResult(ResultSet result){
        this.sqlResultSet = result;
        this.isBooleanResult = false;
    }

    public SQLResult(int resultInt){
        this.resultInt = resultInt;
    }

    public boolean isBooleanResult(){
        return isBooleanResult;
    }

    public ResultSet sqlResults(){
        return sqlResultSet;
    }

    public boolean booleanResults(){
        return sqlBooleanResult;
    }
}
