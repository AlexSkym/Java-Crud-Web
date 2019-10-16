package com.ecodeup.conexion;

//Esto es el POOL de Conexiones

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

public class Conexion {

	//va a ser estático porque, cuando yo acceda al Pool de Conexiones, no voy a crear un nuevo objeto; sino que voy a utilizar directamente la clase y, voy a tener el Pool de conexiones dentro de la clase.
	//No voy a crear un objeto de clase; por ende, todos los métodos y atributos que vayan dentro de esta clase van a ser estáticos.
	private static BasicDataSource dataSource= null;
	
	//éste lo vamos a hacer private para que sólo se pueda acceder dentro de la clase. Luego creo otro método GET o SET para acceder fuera de la clase.
	private static DataSource getDataSource() {
		
		if(dataSource == null) {
			
			dataSource = new BasicDataSource();
			
			//con ésto ya ceteamos el driver de conexión
			dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		
			//pasamos los parámetros:
			dataSource.setUsername("root");
			dataSource.setPassword("1234");
			dataSource.setUrl("jdbc:mysql://localhost:8080/crud?serverTimezone=UTC");
			
			dataSource.setInitialSize(20);
			dataSource.setMaxIdle(15);
			dataSource.setMaxTotal(20);
			dataSource.setMaxWaitMillis(5000);	//cinco segundos... cinco mil milisegundos
			
		}
		
		return dataSource;
	}
	
	//Creo este Método porque necesito retornar la conexión en el DAO, donde voy a crear los métodos CRUD
	//Ésto va a retornar la conexión al Pool de Conexiones
	//Ésto va a devolver un objeto de tipo "Connection"... el cual es la conexión misma (con "getConnection()")
	public static Connection getConnection() throws SQLException {
		
		
		
		//para retornar esto.. necesitamos agregar una excepción al método, por si se haya un error de conexión
		return getDataSource().getConnection();
	}
	
	
}
