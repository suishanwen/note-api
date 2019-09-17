package com.sw.note.mapper;

import com.sw.note.model.VoteProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface VoteProjectMapper extends tk.mybatis.mapper.common.Mapper<VoteProject> {
    @Select("<script> \n" +
            "SELECT\n" +
            "    *\n" +
            "FROM\n" +
            "    vote_project\n" +
            "ORDER BY price\n" +
            "desc\n" +
            "</script>")
    List<VoteProject> query();


    @Select("<script> \n" +
            "delete\n" +
            "FROM\n" +
            "    vote_project\n" +
            "</script>")
    void empty();
}
