package com.sw.note.mapper;

import com.sw.note.model.ClientDirect;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface ClientDirectMapper extends tk.mybatis.mapper.common.Mapper<ClientDirect> {


    @Select("<script> \n" +
            "SELECT id\n" +
            "FROM client_direct\n" +
            "WHERE \n" +
            "    user_id = #{userId}\n" +
            "AND sort_no = #{sortNo}\n" +
            "</script>")
    String selectIdByUser(@Param("userId") String userId, @Param("sortNo") int sortNo);


    @Select("<script> \n" +
            "SELECT direct\n" +
            "FROM client_direct\n" +
            "WHERE \n" +
            "    id = #{id}\n" +
            "</script>")
    String selectDirectById(@Param("id") String id);

    @Insert("<script> \n" +
            "INSERT INTO\n" +
            " client_direct(id,user_id,sort_no)\n" +
            "values (#{id},#{userId},#{sortNo}) \n" +
            "</script>")
    void insertClient(@Param("id") String id, @Param("userId") String userId, @Param("sortNo") int sortNo);

    @Update("<script> \n" +
            "UPDATE \n" +
            "    client_direct\n" +
            "SET  direct=''\n" +
            "WHERE \n" +
            "    id = #{id}\n" +
            "AND direct = #{direct}\n" +
            "</script>")
    void confirmDirect(@Param("id") String id, @Param("direct") String direct);


    @Select("<script> \n" +
            "SELECT\n" +
            "   cd.*\n" +
            "FROM\n" +
            "    client_direct cd\n" +
            "WHERE \n" +
            "    cd.user_id = #{userId}\n" +
            "ORDER BY\n" +
            "    cd.sort_no\n" +
            "</script>")
    List<ClientDirect> selectByUserId(@Param("userId") String userId);

    @Select("<script> \n" +
            "SELECT\n" +
            "   cd.*\n" +
            "FROM\n" +
            "    client_direct cd\n" +
            "ORDER BY\n" +
            "    cd.user_id,cd.sort_no\n" +
            "</script>")
    List<ClientDirect> selectAllCient();


    @Update("<script> \n" +
            "UPDATE\n" +
            "    client_direct\n" +
            "SET direct = #{direct}\n" +
            "WHERE \n" +
            "    id = #{id}\n" +
            "</script>")
    void updateDirect(@Param("id") String id, @Param("direct") String direct);


    @Delete("<script> \n" +
            "DELETE FROM\n" +
            "    client_direct\n" +
            "WHERE \n" +
            "    user_id = #{userId}\n" +
            "AND sort_no = #{sortNo}\n" +
            "</script>")
    void deleteByUser(@Param("userId") String userId, @Param("sortNo") int sortNo);

    @Select("SELECT\n" +
            "\tcount( id ) \n" +
            "FROM\n" +
            "\tclient_direct \n" +
            "WHERE\n" +
            "\treport_time >= CURRENT_TIMESTAMP - INTERVAL 15 MINUTE;")
    int selectActive();
}

