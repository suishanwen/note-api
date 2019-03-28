package com.sw.note.service;

import com.sw.note.beans.BusinessException;
import com.sw.note.mapper.CommentMapper;
import com.sw.note.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public List<Comment> get(Integer thread, String ip) {
        return commentMapper.get(thread, ip);
    }

    public void add(Comment comment) {
        comment.setCommentTime(new Date());
        int count = commentMapper.insert(comment);
        if (count == 0) {
            throw new BusinessException("添加失败!");
        }
    }


    public void edit(Comment comment) {
        comment.setEditTime(new Date());
        int count = commentMapper.updateByPrimaryKeySelective(comment);
        if (count == 0) {
            throw new BusinessException("编辑失败!");
        }
    }

    public void delete(Integer id) {
        int count = commentMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new BusinessException("删除失败!");
        }
    }
}
