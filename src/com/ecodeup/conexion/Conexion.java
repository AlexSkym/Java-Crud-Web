package com.ecodeup.conexion;

//Esto es el POOL de Conexiones

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

public class Conexion {

	//va a ser est�tico porque, cuando yo acceda al Pool de Conexiones, no voy a crear un nuevo objeto; sino que voy a utilizar directamente la clase y, voy a tener el Pool de conexiones dentro de la clase.
	//No voy a crear un objeto de clase; por ende, todos los m�todos y atributos que vayan dentro de esta clase van a ser est�ticos.
	private static BasicDataSource dataSource= null;
	
	//�ste lo vamos a hacer private para que s�lo se pueda acceder dentro de la clase. Luego creo otro m�todo GET o SET para acceder fuera de la clase.
	private static DataSource getDataSource() {
		
		if(dataSource == null) {
			
			dataSource = new BasicDataSource();
			
			//con �sto ya ceteamos el driver de conexi�n
			dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		
			//pasamos los par�metros:
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
	
	//Creo este M�todo porque necesito retornar la conexi�n en el DAO, donde voy a crear los m�todos CRUD
	//�sto va a retornar la conexi�n al Pool de Conexiones
	//�sto va a devolver un objeto de tipo "Connection"... el cual es la conexi�n misma (con "getConnection()")
	public static Connection getConnection() throws SQLException {
		
		
		
		//para retornar esto.. necesitamos agregar una excepci�n al m�todo, por si se haya un error de conexi�n
		return getDataSource().getConnection();
	}
	
	
}
