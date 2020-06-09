package chj.idler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.util.HashSet;
import java.util.Set;

public class JedisClusterUtil {
    private static JedisCluster jedisCluster;
    private static ObjectMapper objectMapper;
    static {
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("192.168.124.25", 7000));
        nodes.add(new HostAndPort("192.168.124.24", 7004));
        nodes.add(new HostAndPort("192.168.124.23", 7002));
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(2000);
        config.setMaxIdle(100);

        jedisCluster = new JedisCluster(nodes, config);
        objectMapper = new ObjectMapper();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
                if (jedisCluster != null) {
                    jedisCluster.close();
                }
        }));
    }


    public static String set(String key,Object value)throws JsonProcessingException{
        if(key==null)return null;
        String result;
        result=jedisCluster.set(key, objectMapper.writeValueAsString(value));
        return result;
    }
    public static Long setnx(String key,Object value)throws JsonProcessingException{
        if(key==null)return null;
        Long result;
        result=jedisCluster.setnx(key, objectMapper.writeValueAsString(value));
        return result;
    }
    public  static String setAndExpire(String key,Object value,int seconds)throws JsonProcessingException{
        if(key==null)return null;
        String result;
        //非原子性
        result=jedisCluster.set(key, objectMapper.writeValueAsString(value), SetParams.setParams().nx().ex(seconds));
        return result;
    }
    public static String get(String key) {
        if(key==null)return null;
        String value;
        value=jedisCluster.get(key);
        return value;
    }
    public static <T>T get(String key,Class<T> tClass)throws JsonProcessingException{
        if(key==null)return null;
        String value=jedisCluster.get(key);
        if(StringUtils.isEmpty(value))return null;
        T result;
        result=objectMapper.readValue(value,tClass);
        return result;
    }

    public static Long expire(String key,int seconds){
        Long result=null;
        if(key!=null)
        result=jedisCluster.expire(key,seconds);
        return result;
    }
    public static Long del(String key){
        Long result=null;
        if(key!=null)
            result=jedisCluster.del(key);
        return result;
    }
    public static Long hset(String key,String field, Object value)throws JsonProcessingException{
        if(key==null)return null;
        Long result;
        result=jedisCluster.hset(key, field,objectMapper.writeValueAsString(value));

        return result;
    }
    public static String hget(String key,String field) {
        if(key==null)return null;
        String value;
        value=jedisCluster.hget(key,field);
        return value;
    }
    public static <T>T hget(String key,String field,Class<T> tClass)throws JsonProcessingException{
        if(key==null)return null;
        String value=jedisCluster.hget(key,field);
        if(StringUtils.isEmpty(value))return null;
        T result=objectMapper.readValue(value,tClass);
        return result;
    }


//    public static JedisCluster getCluster() {
//        return jedisCluster;
//    }
}