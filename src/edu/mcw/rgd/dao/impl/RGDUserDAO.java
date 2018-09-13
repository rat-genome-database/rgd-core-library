package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.dao.spring.RGDUserListQuery;
import edu.mcw.rgd.dao.spring.RGDUserQuery;
import edu.mcw.rgd.datamodel.RGDUser;
import edu.mcw.rgd.datamodel.RGDUserList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;

import java.sql.Types;
import java.util.Date;
import java.util.List;

public class RGDUserDAO extends AbstractDAO {

    public RGDUser getUser(int userId) throws Exception {

        String query = "select * from rgd_user where user_id=?";
        RGDUserQuery gq = new RGDUserQuery(this.getDataSource(), query);
        gq.declareParameter(new SqlParameter(Types.INTEGER));
        gq.compile();

        List<RGDUser> users = gq.execute(new Object[]{userId});

        if (users.size() == 0) {
            throw new Exception("user " + userId + " not found");
        }
        return users.get(0);

    }

    public List<RGDUserList> getUserLists(int userId) throws Exception {

        String query = "select * from rgd_user_list where user_id=?";
        RGDUserListQuery gq = new RGDUserListQuery(this.getDataSource(), query);
        gq.declareParameter(new SqlParameter(Types.INTEGER));
        gq.compile();

        return gq.execute(new Object[]{userId});

    }

    public RGDUserList getUserList(String name) throws Exception {

        String query = "select * from rgd_user_list where name=?";
        RGDUserListQuery gq = new RGDUserListQuery(this.getDataSource(), query);
        gq.declareParameter(new SqlParameter(Types.VARCHAR));
        gq.compile();

        List al = gq.execute(new Object[]{name});

        if (al.size() > 0) {
            return (RGDUserList) al.get(0);
        } else {
            throw new Exception("User LIst not found: " + name);
        }
    }

    public void deleteUserList(RGDUserList list) throws Exception{

        String sql = "delete from RGD_USER_LIST where list_id=?";
        update(sql, list.getListId());

        sql = "delete from RGD_USER_LIST_OBJECT where list_id=?";
        update(sql, list.getListId());
    }


    public List<Integer> getUserListObjects(int listId) throws Exception {

        String query = "select rgd_id from rgd_user_list_object where list_id=?";
        IntListQuery gq = new IntListQuery(this.getDataSource(), query);
        gq.declareParameter(new SqlParameter(Types.INTEGER));
        gq.compile();

        return gq.execute(new Object[]{listId});
    }

    public RGDUser insertRGDUser() throws Exception {

        //get the sequence
        String query = "select rgd_user_seq.NEXTVAL from dual";
        JdbcTemplate jt = new JdbcTemplate(getDataSource());
        int userKey = jt.queryForInt(query);

        RGDUser user = new RGDUser();
        user.setUserId(userKey);

        String sql = "insert into RGD_USER (user_id) values (?)";
        update(sql, userKey);

        return user;
    }

    public int insertRGDUserList(int userId, int objectType, int mapKey, String listName, List<Integer> rgdIds) throws Exception {

        String query = "select rgd_user_list_seq.NEXTVAL from dual";
        JdbcTemplate jt = new JdbcTemplate(getDataSource());
        int userListKey = jt.queryForInt(query);

        String sql = "insert into RGD_USER_LIST (user_id, list_id, object_type, " +
                "map_key, name, created_date) values (?,?,?,?,?,?)";
        update(sql, userId, userListKey, objectType, mapKey, listName, new java.sql.Date(new Date().getTime()));


        insertRGDUserListGenes(userListKey, rgdIds);

        return userListKey;
    }


    public void insertRGDUserListGenes(int listId, List<Integer> rgdIds) throws Exception {

        for (Integer rgdId: rgdIds) {

            String sql = "insert into RGD_USER_LIST_OBJECT (list_id, rgd_id) values (?,?)";
            update(sql, listId, rgdId);
        }
    }


}