package com.excilys.computerdatabase.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.dao.CompanyDAOInterface;
import com.excilys.computerdatabase.dao.ConnectionManager;
import com.excilys.computerdatabase.exception.PersistenceException;
import com.excilys.computerdatabase.model.Company;

/**
 * @author paulr_000 d
 */
// Computer Database Access object..
public enum CompanyDAO implements CompanyDAOInterface {
	// Singleton pattern
	INSTANCE;
	private static final String UPDATE_STMT = "UPDATE company SET name=? WHERE id=? ;";
	private static final String LIST_QUERY_STMT = "SELECT * FROM company LIMIT ?,?;";
	private static final String INSERT_STMT = "INSERT INTO company(name) VALUES (?);";
	private static final String SINGLE_QUERY_STMT = "SELECT * FROM company WHERE id =?";
	private static final String QUERY_ALL = "SELECT * FROM company;";
	private static final int pageSize = 10;
	private Logger logger = LoggerFactory.getLogger(CompanyDAO.class);
	private ConnectionManager connectionManager = ConnectionManager
			.getInstance();

	/**
	 * Get instance of CompanyDAO
	 * 
	 * @return
	 */
	public static CompanyDAO getInstance() {
		return INSTANCE;
	}

	/**
	 * @param id
	 * @return Return one company
	 */
	public Company get(long id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = connectionManager.getConnection();
			stmt = conn.prepareStatement(SINGLE_QUERY_STMT);
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new Company(rs.getLong("id"), rs.getString("name"));
			} else {
				return null;// Display values
			}
		} catch (SQLException e) {
			logger.warn("Error selecting Company  id=[ %d=", id);
			throw new PersistenceException();
		} finally {
			connectionManager.close(stmt, conn);
		}

	}

	/**
	 * @param currentIndex
	 * @param pageSize
	 * @return
	 */
	public List<Company> getPage(int pageNumber) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = connectionManager.getConnection();
			stmt = conn.prepareStatement(LIST_QUERY_STMT);
			stmt.setInt(1, pageNumber * pageSize);
			stmt.setInt(2, pageSize);
			final ResultSet rs = stmt.executeQuery();
			ArrayList<Company> companyList = new ArrayList<Company>();
			while (rs.next()) {
				companyList.add(new Company(rs.getLong("id"), rs
						.getString("name")));
			}
			return companyList;
		} catch (SQLException e) {
			logger.warn("Couldn't select list of companies: %d-%d", pageNumber
					* pageSize, (pageNumber + 1) * pageSize);
			throw new PersistenceException();
		} finally {
			connectionManager.close(stmt, conn);
		}

	}

	/**
	 * @param name
	 */
	public int save(String name) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = connectionManager.getConnection();
			stmt = conn.prepareStatement(INSERT_STMT,
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			logger.warn("Couldn't save company: %s", name);
			throw new PersistenceException();
		} finally {
			connectionManager.close(stmt, conn);
		}
	}

	/**
	 * @param id
	 * @param name
	 */
	public void update(long id, String name) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = connectionManager.getConnection();
			stmt = conn.prepareStatement(UPDATE_STMT);
			stmt.setString(1, name);
			stmt.setLong(2, id);
			stmt.executeUpdate();

		} catch (SQLException e) {
			logger.warn("Couldn't update company with id=%d", id);
			throw new PersistenceException();
		} finally {
			connectionManager.close(stmt, conn);
		}
	}

	public List<Company> getAll() {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = connectionManager.getConnection();
			stmt = conn.prepareStatement(QUERY_ALL);
			ResultSet rs = stmt.executeQuery();
			List<Company> list = new ArrayList<Company>();
			while (rs.next()) {
				list.add(new Company(rs.getLong("id"), rs.getString("name")));
			}
			return list;
		} catch (SQLException e) {
			logger.warn("Error getting all Companies");
			throw new PersistenceException();
		} finally {
			connectionManager.close(stmt, conn);
		}
	}

}