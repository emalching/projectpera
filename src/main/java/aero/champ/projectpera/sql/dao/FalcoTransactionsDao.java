package aero.champ.projectpera.sql.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import aero.champ.projectpera.BO.Staff;
import aero.champ.projectpera.BO.TimeInOut;
import aero.champ.projectpera.sql.connection.SqlServerConnectionManager;

public class FalcoTransactionsDao {

	private static final int CARD_NO = 1;
	private static final int NAME = 2;
	private static final int DATE = 3;
	private static final int TIME_IN = 4;
	private static final int TIME_OUT = 5;

	public List<Staff> getTimeInOut(String startDate, String endDate) {
		List<Staff> staffList = new ArrayList<Staff>();

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			SqlServerConnectionManager sqlServer = new SqlServerConnectionManager();
			sqlServer.dbConnect("jdbc:sqlserver://FALCOWEB\\SQLEXPRESS;databaseName=DataDB", "sa",
					"passw@rd1234");
			conn = sqlServer.getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(getTimeInOutQuery(startDate, endDate));

			int prevCardNumber = -1;
			
			Staff staff = new Staff();
			List<TimeInOut> tioList = new ArrayList<>();
			
			while (rs.next()) {
				int currCardNumber = rs.getInt(CARD_NO);
				if (currCardNumber < 0) {
					continue;
				}
				
				if (prevCardNumber == -1) {
					prevCardNumber = currCardNumber;
				}
				
				if (prevCardNumber != currCardNumber) {
					staff.setTimeInOutList(tioList);
					staffList.add(staff);
					
					staff = new Staff();
					tioList = new ArrayList<>();
					
					prevCardNumber = currCardNumber;
				}
				
				staff.setCardNumber(currCardNumber);
				staff.setEmployeeName(rs.getString(NAME));

				TimeInOut tio = new TimeInOut();
				tio.setWorkDate(rs.getString(DATE));
				tio.setTimeIn(rs.getString(TIME_IN));
				tio.setTimeOut(rs.getString(TIME_OUT));
				tioList.add(tio);
			}
			
			if (rs.isAfterLast()) {
				staff.setTimeInOutList(tioList);
				staffList.add(staff);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); };
			try { if (stmt != null) stmt.close(); } catch (Exception e) { e.printStackTrace(); };
			try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); };
		}

		return staffList;
	}

	private String getTimeInOutQuery(String startDate, String endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT CardNo ");
		sb.append("       ,TrName ");
		sb.append("       ,TrDate ");
		sb.append("       ,MIN(TrTime) ");
		sb.append("       ,MAX(TrTime) ");
		sb.append(" FROM DataDB.dbo.tblTransaction ");
		sb.append(" WHERE TrDate BETWEEN '").append(startDate).append("' AND '").append(endDate).append("' ");
		sb.append(" AND CardNo != 'FFFFFFFFFF' ");
		sb.append(" GROUP BY CardNo, TrName, TrDate ");
		sb.append(" ORDER BY CardNo ASC; ");	

		return sb.toString();
	}

}
