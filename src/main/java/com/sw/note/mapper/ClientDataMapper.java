package com.sw.note.mapper;

import com.sw.note.model.ClientData;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
@Mapper
public interface ClientDataMapper extends tk.mybatis.mapper.common.Mapper<ClientData> {


    @Select("SELECT\n" +
            "\tdetail\n" +
            "FROM\n" +
            "\tclient_data\n" +
            "WHERE\n" +
            "\tid = #{id}\n" +
            "AND date = #{date}")
    String selectDetail(@Param("id") String id, @Param("date") String date);

    @Select("SELECT\n" +
            "\ta.sort_no,\n" +
            "\tb.reward\n" +
            "FROM\n" +
            "\tclient_direct a\n" +
            "INNER JOIN client_data b ON a.id = b.id\n" +
            "AND b.date = #{date}\n" +
            "WHERE\n" +
            "\ta.user_id = #{userId}\n" +
            "ORDER BY\n" +
            "\ta.sort_no ASC")
    List<ClientData> selectDataByUserId(@Param("userId") String userId, @Param("date") String date);
}

