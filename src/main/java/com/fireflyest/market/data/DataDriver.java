package com.fireflyest.market.data;

import java.util.List;

public interface DataDriver {

    void update(Object obj);

    int insert(Object obj);

    List<?> queryList(Class<?> clazz, String key, Object value);

    List<?> queryList(Class<?> clazz);

    Object query(Class<?> clazz, String key, Object value);

    Object query(Class<?> clazz, Object value);

    void delete(Class<?> clazz, String key, Object value);

    void delete(Object obj);

    boolean contain(Class<?> clazz, String key, Object value);

    void initTable(Class<?> clazz);

}
