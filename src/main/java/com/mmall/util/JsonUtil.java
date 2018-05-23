package com.mmall.util;

import com.mmall.pojo.TestPojo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;

/**
 * Created by geely
 */
@Slf4j
public class JsonUtil {



    private static ObjectMapper objectMapper = new ObjectMapper();
    static{
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);

        //取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);

        //忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);

        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }




    /**
     * 将对象转字符串
     */
    public static <T> String obj2String(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :  objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error",e);
            return null;
        }
    }

    /**
     * 将对象转字符串 返回漂亮的格式字符串
     */
    public static <T> String obj2StringPretty(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :  objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error",e);
            return null;
        }
    }


    /**
     * 将字符串转对象
     */
    public static <T> T string2Obj(String str,Class<T> clazz){
        if(StringUtils.isEmpty(str) || clazz == null){
            return null;
        }

        try {
            return clazz.equals(String.class)? (T)str : objectMapper.readValue(str,clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }


    /**
     * 字符串转 集合泛型对象列表
     * @param str  字符串
     * @param typeReference  引用类型
     * @param <T> 返回类型
     * @return
     */
    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class)? str : objectMapper.readValue(str,typeReference));
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }


    /**
     * 字符串转 集合泛型对象列表
     * @param str
     * @param collectionClass  集合类型
     * @param elementClasses    泛型对象类型
     * @return
     */
    public static <T> T string2Obj(String str,Class<?> collectionClass,Class<?>... elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try {
            return objectMapper.readValue(str,javaType);
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    public static void main(String[] args) {
        TestPojo testPojo = new TestPojo();
        testPojo.setName("Geely");
        testPojo.setId(666);

        //{"name":"Geely","id":666}
        String testPojoJson = JsonUtil.obj2String(testPojo);
        log.info("testPojoJson:{}",testPojoJson);

        String json = "{\"name\":\"Geely\",\"color\":\"blue\",\"id\":666}";
        TestPojo testPojoObject = JsonUtil.string2Obj(json,TestPojo.class);


        log.info("end");

//        User user = new User();
//        user.setId(2);
//        user.setEmail("geely@happymmall.com");
//        user.setCreateTime(new Date());
//        String userJsonPretty = JsonUtil.obj2StringPretty(user);
//        log.info("userJson:{}",userJsonPretty);


//        User u1 = new User();
//        u1.setId(1);
//        u1.setEmail("geelyu1@happymmall.com");

//        User u2 = new User();
//        u2.setId(2);
//        u2.setEmail("geelyu2@happymmall.com");



//        String user1Json = JsonUtil.obj2String(u2);

//        String user1JsonPretty = JsonUtil.obj2StringPretty(u2);

//        log.info("user1Json:{}",user1Json);

//        log.info("user1JsonPretty:{}",user1JsonPretty);


//        User strToUser = JsonUtil.string2Obj(user1Json,User.class);


//        List<User> userList = Lists.newArrayList();
//        userList.add(u1);
//        userList.add(u2);

//        String userListStr = JsonUtil.obj2StringPretty(userList);

//        log.info("==================");

//        log.info(userListStr);


//        List<User> userListObj1 = JsonUtil.string2Obj(userListStr, new TypeReference<List<User>>() { });
//        List<User> userListObj2 = JsonUtil.string2Obj(userListStr,List.class,User.class);

        System.out.println("end");

    }

}
