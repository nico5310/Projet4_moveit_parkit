package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * this class contains methods that allow interaction with the system and the database
 *
 * @author Nicolas BIANCUCCI
 */

public class ParkingSpotDAO {

    /**
     * ParkingSpotDAO Logger
     */
    private static final Logger         logger         = LogManager.getLogger("ParkingSpotDAO");
    /**
     * databaseconfig initialisation object
     */
    public               DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * ckeck if available parking slot is empty or not
     *
     * @param parkingType
     *         refers to vehicule type
     *
     * @return -1 if non parking slot is available int: parking slot id
     */
    public int getNextAvailableSlot(ParkingType parkingType) {

        Connection        con    = null;
        PreparedStatement ps     = null;
        ResultSet         rs     = null;
        int               result = -1;
        try {
            con = dataBaseConfig.getConnection();
            ps  = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, parkingType.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);

            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return result;
    }

    /**
     * update the availability of a parking slot
     *
     * @param parkingSpot
     *         parking slot will be update
     *
     * @return updateRowCount with 1 if slot is affected false if the update failed
     */
    public boolean updateParking(ParkingSpot parkingSpot) {
        // update the availability for that parking slot
        Connection        con = null;
        PreparedStatement ps  = null;
        try {
            con = dataBaseConfig.getConnection();
            ps  = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());
            int updateRowCount = ps.executeUpdate();
            return (updateRowCount == 1);
        } catch (Exception ex) {
            logger.error("Error updating parking info", ex);
            return false;
        } finally {
            dataBaseConfig.closeConnection(con);
            dataBaseConfig.closePreparedStatement(ps);
        }
    }

}
