package com.techelevator.tebucks.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class JdbcStatusDao implements StatusDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcStatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Integer, String> getStatusMap() {
        Map<Integer, String> statusMap = new HashMap<>();

        String sql = "" +
                "SELECT status_id, status_name " +
                "FROM statuses;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

        while (rowSet.next()) {
            statusMap.put(rowSet.getInt("status_id"), rowSet.getString("status_name"));
        }
        return statusMap;
    }

}
