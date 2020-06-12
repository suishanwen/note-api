package com.sw.note.cache;

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

    public void report(ClientDirect clientDirect) {
        long start = System.currentTimeMillis();
        redisTemplate.opsForHash().put(clientDirect.getId(), "projectName", clientDirect.getProjectName());
        redisTemplate.opsForHash().put(clientDirect.getId(), "workerId", clientDirect.getWorkerId());
        redisTemplate.opsForHash().put(clientDirect.getId(), "success", clientDirect.getSuccess());
        redisTemplate.opsForHash().put(clientDirect.getId(), "reward", clientDirect.getReward());
        redisTemplate.opsForHash().put(clientDirect.getId(), "updateTime", new Date());
        sync(clientDirect.getId(), false);
        logger.info(clientDirect.getId() + "-上报耗时:" + (System.currentTimeMillis() - start));
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

    public List<ClientDirect> selectByUserId(String userId) {
        long start = System.currentTimeMillis();
        List<ClientDirect> clientDirectList = Objects.requireNonNull(redisTemplate.opsForSet().members("ids"))
                .stream()
//                .filter(id -> {
//                    if ("root".equals(userId)) {
//                        return true;
//                    }
//                    return userId.equals(redisTemplate.opsForHash().get(id.toString(), "userId"));
//                })
                .map(id ->
                        get(id.toString())
                )
                .sorted((t1, t2) -> {
                    if (t1.getUserId().equals(t2.getUserId())) {
                        return t1.getSortNo() - t2.getSortNo();
                    } else {
                        return t1.getUserId().compareTo(t2.getUserId());
                    }
                }).collect(Collectors.toList());
        logger.info("从redis获取数据耗时:" + (System.currentTimeMillis() - start));
        return clientDirectList;
    }

    public long versions() {
        return Objects.requireNonNull(redisTemplate.opsForSet().members("ids"))
                .stream().map(id -> Objects.requireNonNull(redisTemplate.opsForHash().get(id.toString(), "version")).toString()
                ).distinct().count();
    }

    public void upgrade() {
        String updatedVersion = Objects.requireNonNull(redisTemplate.opsForSet().members("ids"))
                .stream().map(id -> Objects.requireNonNull(redisTemplate.opsForHash().get(id.toString(), "version")).toString()
                ).max(String::compareTo).orElse("");
        Objects.requireNonNull(redisTemplate.opsForSet().members("ids")).forEach(id -> {
            String version = Objects.requireNonNull(redisTemplate.opsForHash().get(id.toString(), "version")).toString();
            if (!version.equals(updatedVersion)) {
                setVal((String) id, "direct", "TASK_SYS_UPDATE");
            }
        });
    }

    public double income(String userId) {
        return Objects.requireNonNull(redisTemplate.opsForSet().members("ids"))
                .stream().filter(id -> {
                    if ("root".equals(userId)) {
                        return true;
                    }
                    return userId.equals(redisTemplate.opsForHash().get(id.toString(), "userId"));
                }).mapToDouble(id ->
                        Double.parseDouble(Objects.requireNonNull(getVal((String) id, "reward")).toString())
                ).sum();
    }
}
