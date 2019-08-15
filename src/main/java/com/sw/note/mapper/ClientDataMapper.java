package com.sw.note.mapper;

import com.sw.note.model.ClientData;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;


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
    String selectDetail(@Param("id") String id, @Param("date") Date date);
}

