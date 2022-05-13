package com.quartz.dto.factory;

import com.quartz.dto.ConnectPostgres;
import com.quartz.dto.IConnect;

public class PostgresConnect extends  CrudFactory{
    @Override
    protected IConnect createConnection() {
        return new ConnectPostgres();
    }
}
