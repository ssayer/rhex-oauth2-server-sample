package org.mitre.rhex.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mitre.rhex.model.Client;
import org.mitre.rhex.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired(required=true)
	JdbcTemplate jdbcTemplate;
	
	private final static class ClientRowMapper implements RowMapper<Client> {

		@Override
		public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Client(rs.getString("client_id"), rs.getString("jwk_url"), rs.getString("token"));
		}
	}
	
	private static final ClientRowMapper mapper = new ClientRowMapper();

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Client getClient(String clientId) {
	  return getJdbcTemplate().queryForObject("SELECT * FROM CLIENTS WHERE CLIENT_ID = ?", mapper, clientId);
	}
	
}
