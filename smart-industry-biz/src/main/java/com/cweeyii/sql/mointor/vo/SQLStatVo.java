package com.cweeyii.sql.mointor.vo;

/**
 * Created by cweeyii on 2/7/16 ${EMAIL}.
 */
public class SQLStatVo {
    private String sql;
    private Integer executeCount;
    private Integer errorCount;


    public SQLStatVo(String sql){
        this.sql=sql;
        executeCount=0;
        errorCount=0;
    }

    public String getSql() {
        return sql;
    }

    public Integer getExecuteCount() {
        return executeCount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void incrementExecuteCount(){
        executeCount++;
    }

    public void incrementErrorCount(){
        errorCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SQLStatVo sqlStatVo = (SQLStatVo) o;

        return sql.equals(sqlStatVo.sql);

    }

    @Override
    public int hashCode() {
        return sql.hashCode();
    }
}
