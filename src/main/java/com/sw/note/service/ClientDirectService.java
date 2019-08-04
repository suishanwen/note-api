package com.sw.note.service;

import com.sw.note.mapper.ClientDirectMapper;
import com.sw.note.model.ClientDirect;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class ClientDirectService {
    private ClientDirectMapper clientDirectMapper;

    public ClientDirectService(ClientDirectMapper clientDirectMapper) {
        this.clientDirectMapper = clientDirectMapper;
    }

    public String load(String userId, int sortNo) {
        String id = clientDirectMapper.selectIdByUser(userId, sortNo);
        if (StringUtils.isEmpty(id)) {
            id = UUID.randomUUID().toString().split("-")[0];
            clientDirectMapper.insertClient(id, userId, sortNo);
        }
        return id;
    }

    public String direct(ClientDirect clientDirect) {
        clientDirect.setReportTime(new Date());
        clientDirectMapper.updateByPrimaryKeySelective(clientDirect);
        return clientDirectMapper.selectDirectById(clientDirect.getId());
    }

    public int confirm(String id, String direct) {
        clientDirectMapper.confirmDirect(id, direct);
        return 1;
    }

    public void deleteClient(String userId, int sortNo) {
        clientDirectMapper.deleteByUser(userId, sortNo);
    }


    public List<ClientDirect> selectByUserId(String userId) {
        return clientDirectMapper.selectByUserId(userId);
    }

    public void updateDirect(String ids, String direct) {
        String [] idArray = ids.split(",");
        for(String id:idArray){
            clientDirectMapper.updateDirect(id, direct);
        }
    }
}
