package com.fireflyest.market.data;

import com.fireflyest.market.util.ReflectUtils;
import com.fireflyest.market.util.YamlUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YamlDriver implements DataDriver {

    public YamlDriver(){
    }

    @Override
    public void update(Object obj) {
        Class<?> clazz = obj.getClass();
        List<Field> fields = this.getClassFields(clazz);
        String priKey = this.getPriKey(clazz);
        String target = String.valueOf(ReflectUtils.invokeGet(obj, priKey));
        String table = this.getTable(clazz);

        for(Field field : fields){
            if(priKey.equalsIgnoreCase(field.getName()))continue;
            Object value = ReflectUtils.invokeGet(obj, field.getName());
            YamlUtils.setPlayerData(table, target, field.getName(), value);
        }
    }

    @Override
    public int insert(Object obj) {
        Class<?> clazz = obj.getClass();
        int id = 0;
        String table = this.getTable(clazz);
        String priKey = this.getPriKey(clazz);
        Object priValue = ReflectUtils.invokeGet(obj, priKey);
        if("id".equalsIgnoreCase(priKey) && priValue != null && 0 == ((int) priValue)){
            id = this.getNewId(table);
            ReflectUtils.invokeSet(obj, priKey, id);
        }
        update(obj);
        return id;
    }

    @Override
    public List<?> queryList(Class<?> clazz, String key, Object value) {
        List<Object> list = new ArrayList<>();
        List<Field> fields = this.getClassFields(clazz);
        String table = this.getTable(clazz);
        String priKey = this.getPriKey(clazz);

        if(priKey.equalsIgnoreCase(key)){
            Object obj = this.buildObject(clazz, fields, table, priKey, value);
            list.add(obj);
        }else {
            for (String yml : YamlUtils.getPlayerDataKeys(table)) {
                if("###".equalsIgnoreCase(yml))continue;
                if(value.equals(String.valueOf(YamlUtils.getPlayerData(table, yml).get(key)))){
                    Object obj;
                    if("id".equals(priKey)){
                        obj = this.buildObject(clazz, fields, table, priKey, Integer.parseInt(yml));
                    }else {
                        obj = this.buildObject(clazz, fields, table, priKey, yml);
                    }
                    list.add(obj);
                }
            }
        }
        return list;
    }

    @Override
    public List<?> queryList(Class<?> clazz) {
        List<Object> list = new ArrayList<>();
        List<Field> fields = this.getClassFields(clazz);
        String table = this.getTable(clazz);
        String priKey = this.getPriKey(clazz);
        for (String yml : YamlUtils.getPlayerDataKeys(table)) {
            if("###".equalsIgnoreCase(yml))continue;
            Object obj;
            if("id".equals(priKey)){
                obj = this.buildObject(clazz, fields, table, priKey, Integer.parseInt(yml));
            }else {
                obj = this.buildObject(clazz, fields, table, priKey, yml);
            }
            list.add(obj);
        }
        return list;
    }

    @Override
    public Object query(Class<?> clazz, String key, Object value) {
        return queryList(clazz, key, value).get(0);
    }

    @Override
    public Object query(Class<?> clazz, Object value) {
        return this.query(clazz, this.getPriKey(clazz), value);
    }

    @Override
    public void delete(Class<?> clazz, String key, Object value) {
        String table = this.getTable(clazz);
        String priKey = this.getPriKey(clazz);
        if(priKey.equalsIgnoreCase(key)){
            YamlUtils.deletePlayerData(table, String.valueOf(value));
        }else {
            for (String yml : YamlUtils.getPlayerDataKeys(table)) {
                if("###".equalsIgnoreCase(yml))continue;
                if(value.equals(String.valueOf(YamlUtils.getPlayerData(table, yml).get(key)))) YamlUtils.deletePlayerData(table, yml);
            }
        }
    }

    @Override
    public void delete(Object obj) {
        Class<?> clazz = obj.getClass();
        String priKey = this.getPriKey(clazz);
        Object value = String.valueOf(ReflectUtils.invokeGet(obj, priKey));
        delete(obj.getClass(), priKey, value);
    }

    @Override
    public boolean contain(Class<?> clazz, String key, Object value) {
        String table = this.getTable(clazz);
        String priKey = this.getPriKey(clazz);
        if(priKey.equalsIgnoreCase(key)){
            return YamlUtils.containsData(table, String.valueOf(value));
        }else {
            for(String yml : YamlUtils.getPlayerDataKeys(table)){
                if("###".equalsIgnoreCase(yml))continue;
                if(value.equals(String.valueOf(YamlUtils.getPlayerData(table, yml).get(key))))return true;
            }
        }
        return false;
    }

    @Override
    public void initTable(Class<?> clazz) {
        String table = this.getTable(clazz);
        YamlUtils.addTable(table);
    }

    public int getNewId(String table){
        int id = YamlUtils.getPlayerData(table, "###").getInt("id")+1;
        YamlUtils.setPlayerData(table, "###", "id", id);
        return id;
    }

    public Object buildObject(Class<?> clazz, List<Field> fields, String table, String priKey, Object value){
        try {
            Object obj = clazz.newInstance();
            for(Field field:fields){
                Object result;
                if(field.getType().equals(Long.class) || field.getType().equals(long.class)){
                    result = YamlUtils.getPlayerData(table, String.valueOf(value)).getLong(field.getName());
                }else {
                    result = YamlUtils.getPlayerData(table, String.valueOf(value)).get(field.getName());
                }
                if(result != null) ReflectUtils.invokeSet(obj, field.getName(), result);
            }
            ReflectUtils.invokeSet(obj, priKey, value);
            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Field> getClassFields(Class<?> clazz){
        List<Field> fields = new ArrayList<>();
        if (clazz.getSuperclass() != null){
            fields.addAll(getClassFields(clazz.getSuperclass()));
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

    private String getPriKey(Class<?> clazz){
        if (clazz.getSuperclass() != null && !clazz.getSuperclass().getTypeName().equals("java.lang.Object")){
            return this.getPriKey(clazz.getSuperclass());
        }
        return clazz.getDeclaredFields()[0].getName().toLowerCase();
    }

    private String getTable(Class<?> clazz){
        return clazz.getSimpleName().toLowerCase();
    }

}
