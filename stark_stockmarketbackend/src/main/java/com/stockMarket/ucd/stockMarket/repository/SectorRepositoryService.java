package com.stockMarket.ucd.stockMarket.repository;

import com.stockMarket.ucd.stockMarket.domain.Sector;
import com.stockMarket.ucd.stockMarket.domain.SummarySector;
import com.stockMarket.ucd.stockMarket.service.SectorService;
import com.stockMarket.ucd.stockMarket.service.daoService.SectorDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("mysqlSRS")
public class SectorRepositoryService implements SectorDaoService {
    private  final Logger log = LoggerFactory.getLogger(SectorService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public static class  SectorRowMapper implements RowMapper<Sector>
    {
        @Override
        public Sector mapRow(ResultSet resultSet, int i) throws SQLException {
            Sector sector = new Sector();
            sector.setSectorId(resultSet.getString("sectorId"));
            sector.setName(resultSet.getString("name"));
            return sector;
        }
    }

    private static class SummarySectorRowMapper implements RowMapper<SummarySector> {

        @Override
        public SummarySector mapRow(ResultSet resultSet, int i) throws SQLException {
            SummarySector sector = new SummarySector();
            sector.setSectorId(resultSet.getString("sectorId"));
            sector.setName(resultSet.getString("name"));
            sector.setTotalAmount(resultSet.getDouble("totalAmount"));
            sector.setTotalQuantity(resultSet.getInt("totalQuantity"));
            return sector;
        }
    }

    @Override
    public List<Sector> getAllSectors() {
        final String sql = "SELECT * FROM sector";
        List<Sector> sectorList = jdbcTemplate.query(sql, new SectorRepositoryService.SectorRowMapper());
        return sectorList;
    }

    @Override
    public void deleteSectorToDb(String name)
    {
        try {
            final String sql = "delete from sector where name=name";
            jdbcTemplate.update(sql, new SectorRepositoryService.SectorRowMapper());
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ user Repository @@@@@@@@@@" + e);
        }
    }
    @Override
    public void updateSectorToDb(Sector sector)
    {
        try {
            final String sql = "SELECT count(*) FROM sector WHERE sectorId = ?";
            int count = (Integer) jdbcTemplate.queryForObject(sql, new Object[]{sector.getSectorId()}, Integer.class);

            String sql2=null;
            if (count > 0) {
                sql2 = "UPDATE sector set name=?  where sectorId =?";
            }else{
                sql2= "INSERT INTO sector (name,sectorId) VALUES (?,?)";
            }

            final String sectorId = sector.getSectorId();
            final String name = sector.getName();
            jdbcTemplate.update(sql2, new Object[]{name,sectorId});

        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ Sector Repository Exception @@@@@@@@@@" + e);
        }
    }
    @Override
    public List<SummarySector> fetchSummarySectorValues()
    {
        List<SummarySector> sectorList=null;
        try {
            final String sql="select z.sectorId ,z.name ,sum(s.quantity) as totalQuantity,sum(s.quantity*s.currentPrice) as totalAmount\n" +
               "from share s ,company c, sector z where s.companyId=c.companyId\n" +
               "and c.sectorId=z.sectorId\n" +
               "group by z.sectorId";
             sectorList = jdbcTemplate.query(sql, new SummarySectorRowMapper());
        } catch (Exception e) {
            log.info("log info @@@@@@@@@@ Sector Repository Exception @@@@@@@@@@" + e);
        }
        return  sectorList;
    }

}
