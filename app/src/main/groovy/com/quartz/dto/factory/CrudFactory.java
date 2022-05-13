package com.quartz.dto.factory;

import com.quartz.dto.IConnect;

public abstract class CrudFactory {

    protected abstract IConnect createConnection();
}
