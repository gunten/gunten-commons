package org.tp.batch;

import org.springframework.jdbc.core.RowMapper;
import org.tp.transactional.entity.ReadData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResourceRowMapper implements RowMapper<ReadData> {

    @Override
    public ReadData mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReadData readData = new ReadData();
        readData.setId(rs.getInt("id"));
        readData.setMobileNo(rs.getString("mobile_no"));
        readData.setName(rs.getString("name"));
        return readData;
    }
}
