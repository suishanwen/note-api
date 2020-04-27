package com.sw.note.service;

import com.sw.note.model.BusinessException;
import com.sw.note.model.VoteSystem;
import com.sw.note.mapper.VoteMapper;
import com.sw.note.middleware.CtrlDeliverSocket;
import com.sw.note.model.entity.CtrlClient;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VoteService {
    private VoteMapper voteMapper;
    private VoteSystem voteSystem;

    public VoteService(VoteMapper voteMapper, VoteSystem voteSystem) {
        this.voteMapper = voteMapper;
        this.voteSystem = voteSystem;
    }


    public List<CtrlClient> queryList(String user) {
        return voteMapper.queryByUser(user);
    }

    public CtrlClient query(String identity) {
        return voteMapper.queryByIdentity(identity);
    }

    public void add(CtrlClient ctrlClient) {
        int count = voteMapper.insert(ctrlClient);
        if (count == 0) {
            throw new BusinessException("添加失败!");
        }
    }

    public void report(CtrlClient ctrlClient) {
        int count = voteMapper.updateByPrimaryKeySelective(ctrlClient);
        if (count == 0) {
            throw new BusinessException("上传失败!");
        } else {
            CtrlDeliverSocket socket = CtrlDeliverSocket.wsClientMap.get(ctrlClient.getIdentity() + "_mobi");
            if (socket != null) {
                try {
                    socket.sendMessage("REFRESH_STATE");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
