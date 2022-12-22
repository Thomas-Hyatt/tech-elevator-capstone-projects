package com.techelevator.tebucks.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class JdbcTypeDao implements TypeDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTypeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Integer, String> getTypesMap() {

        Map<Integer, String> typesMap = new HashMap<>();

        String sql = "" +
                "SELECT type_id, type_name " +
                "FROM types;";

        SqlRowSet rowset = jdbcTemplate.queryForRowSet(sql);

        while(rowset.next()) {
            typesMap.put(rowset.getInt("type_id"), rowset.getString("type_name"));
        }
        return typesMap;
    }
}
