package com.sw.note.cache;

import com.sw.note.mapper.ClientDirectMapper;
import com.sw.note.model.entity.ClientDirect;
import com.sw.note.util.ObjectUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.BeanUtilsHashMapper;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ClientDirectCache {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    BeanUtilsHashMapper<ClientDirect> mapper = new BeanUtilsHashMapper<>(ClientDirect.class);


    public void init(List<ClientDirect> clientDirectList) {
        clientDirectList.forEach(this::put);
    }

    public ClientDirect get(String id) {
        ObjectUtil objectUtil = new ObjectUtil();
        ClientDirect clientDirect = new ClientDirect();
        Map<Object, Object> map = redisTemplate.opsForHash().entries(id);
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", java.util.Locale.US);
        map.forEach((k, v) -> {
            if ("sortNo".equals(k)) {
                clientDirect.setSortNo(Integer.parseInt(v.toString()));
            } else if ("updateTime".equals(k)) {
                clientDirect.setUpdateTime(new Date(Long.parseLong(v.toString())));
//                try {
//                    clientDirect.setUpdateTime(sdf.parse(v.toString()));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
            } else {
                objectUtil.setFieldValue(k.toString(), v.toString(), clientDirect);
            }
        });
        return clientDirect;
    }

    public Object getVal(String id, String field) {
        return redisTemplate.opsForHash().get(id, field);
    }

    public void setVal(String id, String field, Object val) {
        redisTemplate.opsForHash().put(id, field, val);
        sync(id, false);
    }

    public void sync(String id, boolean state) {
        redisTemplate.opsForHash().put(id, "$synchronized", state);
    }

    public void report(String id, Map<String, Object> map) {
        long start = System.currentTimeMillis();
        map.put("updateTime", new Date());
        map.put("$synchronized", false);
        redisTemplate.opsForHash().putAll(id, map);
        logger.info(id + "-上报耗时:" + (System.currentTimeMillis() - start));
    }

    public void setValAll(String field, String val) {
        Objects.requireNonNull(redisTemplate.opsForSet().members("ids")).forEach(id -> {
            setVal(id.toString(), field, val);
        });
    }

    public void put(ClientDirect clientDirect) {
        redisTemplate.opsForHash().putAll(clientDirect.getId(), mapper.toHash(clientDirect));
        redisTemplate.opsForSet().add("ids", clientDirect.getId());
        sync(clientDirect.getId(), true);
    }

    public void remove(String id) {
        redisTemplate.delete(id);
        redisTemplate.opsForSet().remove("ids", id);
    }

    public List<ClientDirect> unSynchronized() {
        return Objects.requireNonNull(redisTemplate.opsForSet().members("ids"))
                .stream().filter(id -> {
                    Object val = redisTemplate.opsForHash().get(id.toString(), "$synchronized");
                    return val == null || !(boolean) val;
                }).map(id -> get(id.toString())).collect(Collectors.toList());
    }
}
