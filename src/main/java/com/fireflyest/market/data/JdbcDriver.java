package com.fireflyest.market.data;

import com.fireflyest.market.util.ConvertUtils;
import com.fireflyest.market.util.JdbcUtils;
import com.fireflyest.market.util.ReflectUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JdbcDriver implements DataDriver{

    private final String url;
    private final String user;
    private final String password;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public JdbcDriver(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        JdbcUtils.init();
    }

    /**
     * 更新数据
     * @param obj 对象
     */
    @Override
    public void update(Object obj){
        Class<?> clazz = obj.getClass();
        StringBuilder update = new StringBuilder();
        List<Field> fields = this.getClassFields(clazz);
        String priKey = this.getPriKey(clazz);
        int amount = 0;
        for(Field field : fields){
            if(amount > 0) update.append(",");
            if(priKey.equalsIgnoreCase(field.getName()))continue;
            update.append(field.getName()).append("=").append("'").append(ReflectUtils.invokeGet(obj, field.getName())).append("'");
            amount++;
        }
        String table = this.getTable(clazz);
        String value = String.valueOf(ReflectUtils.invokeGet(obj, priKey));
        String data = update.toString().replace("'true'", "1").replace("'false'", "0");
        String sql = String.format("update %s set %s where %s='%s'", table, data, priKey, value);
        //执行指令
        try {
            this.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(statement, connection);
        }
    }

    /**
     * 插入数据
     * @param obj 对象
     */
    @Override
    public int insert(Object obj){
        //拼接sql指令
        Class<?> clazz = obj.getClass();
        StringBuilder fieldString = new StringBuilder();
        StringBuilder dataString = new StringBuilder();
        int amount = 0;
        for(Field field : this.getClassFields(clazz)){
            if(amount > 0){
                fieldString.append(",");
                dataString.append(",");
            }
            fieldString.append(field.getName());
            dataString.append("'").append(ReflectUtils.invokeGet(obj, field.getName())).append("'");
            amount++;
        }
        String data = dataString.toString().replace("'true'", "1").replace("'false'", "0");
        String table = this.getTable(clazz);
        String sql = String.format("insert into %s (%s) values (%s)", table, fieldString.toString(), data);
        //执行指令
        try {
            return this.executeInsert(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(resultSet, statement, connection);
        }
        return 0;
    }

    @Override
    public List<?> queryList(Class<?>clazz, String key, Object value){
        List<Object> list = new ArrayList<>();
        String table = this.getTable(clazz);
        String data = value.toString();
        if(data.equals("true") || data.equals("false")){
            data = data.replace("true", "1").replace("false", "0");
        }
        String sql = String.format("select * from %s where %s='%s'", table, key, data);

        //执行指令
        try {
            this.executeQuery(sql);
            while (resultSet.next()){
                Object obj = clazz.newInstance();
                for(Field field : this.getClassFields(clazz)){
                    if("java.sql.Date".equalsIgnoreCase(field.getType().getTypeName())){
                        ReflectUtils.invokeSet(obj, field.getName(), resultSet.getDate(field.getName()));
                    }else {
                        ReflectUtils.invokeSet(obj, field.getName(), resultSet.getObject(field.getName()));
                    }
                }
                list.add(obj);
            }
            return list;
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(resultSet, statement, connection);
        }
        return list;
    }

    @Override
    public List<?> queryList(Class<?> clazz) {
        List<Object> list = new ArrayList<>();
        String table = this.getTable(clazz);
        String sql = String.format("select * from %s", table);

        //执行指令
        try {
            this.executeQuery(sql);
            while (resultSet.next()){
                Object obj = clazz.newInstance();
                for(Field field : this.getClassFields(clazz)){
                    if("java.sql.Date".equalsIgnoreCase(field.getType().getTypeName())){
                        ReflectUtils.invokeSet(obj, field.getName(), resultSet.getDate(field.getName()));
                    }else {
                        ReflectUtils.invokeSet(obj, field.getName(), resultSet.getObject(field.getName()));
                    }
                }
                list.add(obj);
            }
            return list;
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(resultSet, statement, connection);
        }
        return list;
    }

    /**
     *
     * 查询数据
     * @param clazz 对象的class
     * @param value 查询对象
     * @param key 查询健
     * @return 查询数据
     */
    @Override
    public Object query(Class<?>clazz, String key, Object value){
        List<?> objects = this.queryList(clazz, key, value);
        return objects.isEmpty() ? null : objects.get(0);
    }

    /**
     * 查询数据
     * @param clazz 对象的class
     * @param value 查询对象
     * @return 查询数据
     */
    @Override
    public Object query(Class<?>clazz, Object value){
        String priKey = this.getPriKey(clazz);
        return this.query(clazz, priKey, value);
    }

    /**
     * 删除数据
     * @param clazz 对象的class
     * @param value 删除对象
     */
    @Override
    public void delete(Class<?>clazz, String key, Object value){
        //拼接sql指令
        String table = this.getTable(clazz);
        String sql = String.format("delete from %s where %s='%s'", table, key, value);
        //执行指令
        try {
            this.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(statement, connection);
        }
    }

    @Override
    public void delete(Object obj) {
        Class<?> clazz = obj.getClass();
        String priKey = this.getPriKey(clazz);
        String value = String.valueOf(ReflectUtils.invokeGet(obj, priKey));
        delete(clazz, priKey, value);
    }

    /**
     * 判断是否存在对象的数据
     * @param clazz 对象的class
     * @param value 查询对象
     * @return 是否存在
     */
    @Override
    public boolean contain(Class<?>clazz, String key, Object value){
        //拼接sql指令
        String table = this.getTable(clazz);
        String sql = String.format("select * from %s where %s='%s'", table, key, value);
//        System.out.println("sql = " + sql);
        //执行指令
        try {
            this.executeQuery(sql);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(resultSet, statement, connection);
        }
        return false;
    }

    public void execute(String sql) throws SQLException {
        this.connection();
        statement.execute(sql);
    }

    public void executeQuery(String sql) throws SQLException {
        this.connection();
        resultSet = statement.executeQuery(sql);
    }

    public int executeInsert(String sql) throws SQLException {
        this.connection();
        int id = 0;
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.executeUpdate();
        resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()){
            id = resultSet.getInt(1);//返回主键值
        }
        preparedStatement.close();
        return id;
    }

    public void connection() throws SQLException {
        connection = JdbcUtils.getConnection(url, user, password);
        statement = connection.createStatement();
    }

    /**
     * 若未创建表则创建
     * @param clazz 对象class
     */
//    @Override
    public void initTable(Class<?> clazz) {
        //建表指令
        String tableName = this.getTable(clazz);
        StringBuilder builder = new StringBuilder("create table if not exists ").append(tableName).append("(");
        List<Field>fields = this.getClassFields(clazz);

        //获取所有成员变量
        for (int i = 0; i < fields.size(); i++) {
            String fieldName = fields.get(i).getName();
            String type = ConvertUtils.javaType2SQLType(fields.get(i).getType().getTypeName());
            builder.append(fieldName).append(" ").append(type);
            if ("id".equals(this.getPriKey(clazz))){
                if(fieldName.equals("id")){
                    builder.append(" primary key not null auto_increment");
                }
            }else {
                if(i == 0){
                    builder.append(" primary key not null");
                }
            }
            if(i != fields.size()-1)builder.append(",");
        }
        builder.append(");");
        String sql = builder.toString();
//        System.out.println("sql = " + sql);
        //执行指令
        try {
            this.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtils.close(statement, connection);
        }

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
