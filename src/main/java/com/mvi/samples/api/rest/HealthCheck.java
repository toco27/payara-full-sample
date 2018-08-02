package com.mvi.samples.api.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("")
@Stateless
public class HealthCheck {

	@GET
	@Path("readiness")
	public Response readiness() {
		return Response.ok().entity("Readiness ok").build();
	}
	
	@GET
	@Path("liveness")
	public Response liveness() throws ClassNotFoundException, SQLException {
		
		Class.forName("org.postgresql.Driver");
		
		String dbHost = System.getenv("DB_HOST");
		String dbUser = System.getenv("DB_USER");
		String dbPassword = System.getenv("DB_PASSWORD");
		String dbDatabase = System.getenv("DB_DATABASE");
		
		String url = "jdbc:postgresql://" + dbHost + "/" + dbDatabase;
		Properties props = new Properties();
		props.setProperty("user", dbUser);
		props.setProperty("password", dbPassword);
		Connection conn = null;
		try {
			 conn = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.ok().entity(e.getMessage()).build();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		if (conn != null) {
			return Response.ok().entity("Database access successful").build();
		}
		return Response.ok().entity("Database access failed").build();
	}
}
