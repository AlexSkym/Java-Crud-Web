package com.ecodeup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ecodeup.conexion.Conexion;
import com.ecodeup.model.Producto;

public class ProductoDAO {

	//vamos a crear un objeto de tipo Connection para obtener una conexi�n del Pool de Conexiones
	private Connection connection;
	private PreparedStatement statement;
	
	private boolean estadoOperacion; 	//con esta Bandera veremos cu�ndo se realiz� una sentencia SQL
	
	
	/*
	 * *******************
	 * M�todo Guardar ****
	 * *******************
	 * 		m�todo para retornar boolean para saber si se realiz� o no una operaci�n
	 */
	public boolean guardar(Producto producto) throws SQLException {
		
		String sql = null;
		estadoOperacion = false;	//indicando que la operaci�n a�n no ha sido realizado
		
		connection = obtenerConexion();		//obtenerConexion() tiene una excepci�n; por ende, tenemos que agregar aqu� tambi�n una excepci�n
		
		//Try-Catch para el RollBack de todo �sto por si no se ejecuta correctamente.
		try {
			//transacciones
			connection.setAutoCommit(false);	//Aqu� indico que mi transacci�n est� a punto de comenzar y, que todo lo que se venga de aqu� hacia adelante, va a ser una transacci�n completa.
			
			sql = "INSERT INTO productos (id, nombre, cantidad, precio, fecha_crear, fecha_actualizar) VALUES (?,?,?,?,?,?)";		/* Aqu� tendremos en cuenta el PreparedStatement
							 * Antiguamente hab�amos visto como hacer un CRUD utilizando s�lo Statement. Cuando utilizo Statement coloco en la variable "sql" toda la sentencia SQL en crudo; pero...
							 * Cuando utilizo PreparedStatement puedo parametrizar esa consulta o esa sentencia SQL. Para esto lo haremos de la siguiente manera:
							 * Ejemplo: "INSERT INTO productos (//nombre de cada columna) VALUES (Valores a ingresar, //con signo de pregunta dentro//, por cada columna mencionada)"
							 * 
							 * Ahora s�, creamos el Statement (normal) con la conexi�n y la sentencia SQL del PreparedStatement.
							 * Por �sto, no ejecutamos el "execute" sino que le pasamos los par�metros que le declaramos en el "PreparedStatement".
							 */
			statement = connection.prepareStatement(sql); 
			
			//el "set" de esta sentencia... depender� de qu� tipo de dato estemos manejando
			statement.setString(1, null);	//id
			statement.setString(2, producto.getNombre());	//nombre
			statement.setDouble(3, producto.getCantidad());	//cantidad
			statement.setDouble(4, producto.getPrecio());	//Precio
			statement.setDate(5, producto.getFechaCrear());	//fecha_crear
			statement.setDate(6, producto.getFechaActualizar());	//fecha_actualizar
			
			//Ahora voy a enviar la sentencia SQL con el "Statement" ya preparado
			//Adem�s, voy a agregar el valor booleano a estadoOperacion siempre y cuando executeUpdate() retorne un valor mayor a 0 (cero)
			estadoOperacion =  statement.executeUpdate() > 0;	//executeUpdate() retorna un INT (entero) cuando la sentencia SQL ha sido ejecutada exitosamente
			
			//Ahora voy a ejecutar un Commit...
			connection.commit();
			//cierro el Statement
			statement.close();
			//cierro la conexion  ((Aunque en realidad, la devuelvo al Pool de Conexiones))
			connection.close();
		} catch (SQLException e) {
			//En el caso de que alguna conexi�n no funcione... entonces que me haga un RollBack de todo lo que haya hecho hasta ese momento
			connection.rollback();
			e.printStackTrace();
		} 
		
		
		return estadoOperacion;
	}
	
	
	
	/*
	 * *******************
	 * M�todo Editar  ****
	 * *******************
	 * 		m�todo para Editar un registro
	 */
	public boolean editar(Producto producto) throws SQLException {
		
		String sql = null;
		estadoOperacion = false;	//indicando que la operaci�n a�n no ha sido realizado
		
		connection = obtenerConexion();		//obtenerConexion() tiene una excepci�n; por ende, tenemos que agregar aqu� tambi�n una excepci�n
		
		try {
			connection.setAutoCommit(false);
			sql = "UPDATE productos SET nombre=?, cantidad=?, precio=?, fecha_actualizar=? WHERE id=?";		/* Colocamos luego de "SET" el nombre de la columna a modificar, continuado de un " = " (igual) y seguido de un " ? " (signo de interrogaci�n)
																															 * Luego, WHERE id=? para poder cambiar los datos dependiendo del ID solicitado
																															 */
			statement = connection.prepareStatement(sql);	//pasamos la sentencia SQL antes de pasarle los par�metros...
			
			statement.setString(1, producto.getNombre());		//modificar nombre
			statement.setDouble(2, producto.getCantidad());		//modificar cantidad
			statement.setDouble(3, producto.getPrecio());		//modificar precio
			//statement.setDate(4, producto.getFechaCrear());		//modificar fecha_crear
			statement.setDate(4, producto.getFechaActualizar());	//modificar fecha_actualizar
			statement.setInt(5, producto.getId());		//obtener Id
			
			estadoOperacion = statement.executeUpdate() > 0;
			connection.commit();
			statement.close();
			connection.close();
			
			
		} catch (SQLException e) {
			
			connection.rollback();
			e.printStackTrace();
		}
		
		
		return estadoOperacion;
	}
	
	
	/*
	 * ********************
	 * M�todo Eliminar ****
	 * ********************
	 * 		m�todo para Eliminar un registro
	 * 		Aqu� s�lo necesitamos el Id del producto
	 */
	public boolean eliminar(int idProducto) throws SQLException {
		
		String sql = null;
		estadoOperacion = false;	//indicando que la operaci�n a�n no ha sido realizado
		
		connection = obtenerConexion();		//obtenerConexion() tiene una excepci�n; por ende, tenemos que agregar aqu� tambi�n una excepci�n
		
		try {
			connection.setAutoCommit(false);
			sql = "DELETE FROM productos WHERE id=?";
			
			
			statement=connection.prepareStatement(sql);	//sentencia SQL para eliminar una Fila de la tabla productos dependiendo de su Id
			
			statement.setInt(1, idProducto);
			

			estadoOperacion = statement.executeUpdate() > 0;
			connection.commit();
			statement.close();
			connection.close();
			
			
		} catch (SQLException e) {
			
			connection.rollback();
			e.printStackTrace();
		}
		
		
		return estadoOperacion;
	
	}



	/*
	 * **************************************
	 * M�todo Obtener todos los Productos****
	 * **************************************
	 * 		m�todo para obtener productos
	 * 		Aqu� no necesitaremos obtener previamente alg�n objeto
	 *		Aqu� vamos a crear un m�todo de tipo "List<>" del paquete de clases "java.util", ya que vamos a traer una lista de objetos de tipo Producto. Con �sto vamos a obtener la lista de todos los productos de la base de datos
	 * 		
	 */
	public List<Producto> obtenerProductos() throws SQLException {
		
		//En �ste caso Utilizaremos el ResultSet
		ResultSet resultSet = null;		//Este objeto ResultSet me obtiene todos los registros de la consulta que yo voy a hacer hacia la base de datos. En este caso, de la tabla "productos"
		
		List<Producto> listaProductos = new ArrayList<>();	//Cre� una lista de tipo producto a la que voy a llamar... "listaProductos"; y es la que me va a almacenar cada registro (cada Fila) devuelto por el "ResultSet"
		
		String sql = null;
		estadoOperacion = false;	//indicando que la operaci�n a�n no ha sido realizado
		
		connection = obtenerConexion();		//obtenerConexion() tiene una excepci�n; por ende, tenemos que agregar aqu� tambi�n una excepci�n
		
		try {
			sql = "SELECT * FROM productos";
			
			statement = connection.prepareStatement(sql);	//creamos el canal de conexi�n ... el statement
			resultSet = statement.executeQuery(sql);	/* �ste "statement.executeQuery()" me devuelve un "ResultSet"
														 * Ahora que tengo el "ResultSet", se podr�a decir que tengo los datos de la tabla en "una Matris"
														 * Tengo que, cada registro, poderlo obtener a trav�s de un objeto; y, a su vez, poner este objeto en una lista, ...como la que vemos al inicio del m�todo "List<Producto>" 
														 */
			while(resultSet.next()) {	//Este "while" (colocamos while porque son varios registros {filas})  se va a ejecutar siempre y cuando el ResultSet tenga un registro que mostrar...
				
				Producto p = new Producto();	//Creo un objeto para ir almacenando ah� cada uno de los registro
				p.setId(resultSet.getInt(1));	//tengo que indicarle que est�, en la columna 1 (uno) de la base de datos, el "id".
				p.setNombre(resultSet.getNString(2));	//indico nombre
				p.setCantidad(resultSet.getDouble(3));	//indico cantidad
				p.setPrecio(resultSet.getDouble(4));	//indico precio
				p.setFechaCrear(resultSet.getDate(5));	//indico fecha_crear
				p.setFechaActualizar(resultSet.getDate(6));	//indico fecha_actualizar

				//ahora indico que todos esos objetos "p" obtenidos sean a�adidos a la "listaProductos"
				listaProductos.add(p);	//En cada ciclo del "while" se va ir a�adiendo los productos a la lista.
				
				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return listaProductos;	//devolvemos la lista para luego ser mostrada en pantalla con el visor
		//Nota!= Como se ver�... en este m�todo no hemos usado "RollBack()" ni nada, porque eso no era necesario. Ya que s�lo obtengo los datos de los productos de la tabla de mi base de datos.
	}

	/*
	 * **********************************
	 * M�todo Obtener un �nico producto *
	 * **********************************
	 * 		M�todo para obtener un �nico producto antes de ser editado. No va a venir una lista, sino que �nicamente un s�lo objeto de tipo producto
	 */
	public Producto obtenerProducto(int idProducto) throws SQLException {
		
		
		//En �ste caso Utilizaremos el ResultSet
		ResultSet resultSet = null;		//Este objeto ResultSet me obtiene todos los registros de la consulta que yo voy a hacer hacia la base de datos. En este caso, de la tabla "productos"

		Producto p = new Producto();	//Creo un objeto para ir almacenando ah� cada uno de los registro de cada columna
		
		String sql = null;
		estadoOperacion = false;	//indicando que la operaci�n a�n no ha sido realizado
		
		connection = obtenerConexion();		//obtenerConexion() tiene una excepci�n; por ende, tenemos que agregar aqu� tambi�n una excepci�n
		
		try {
			sql = "SELECT * FROM productos WHERE id=?";
			
			statement = connection.prepareStatement(sql);
			statement.setInt(1, idProducto);
			
			resultSet = statement.executeQuery();	// �ste "statement.executeQuery()" me devuelve un "ResultSet"
			
			if(resultSet.next()) {	//Este "if" (colocamos if porque es un solo registro {fila}) se va a ejecutar siempre y cuando el ResultSet tenga un registro que mostrar...
				
				p.setId(resultSet.getInt(1));	//tengo que indicarle que est�, en la columna 1 (uno) de la base de datos, el "id".
				p.setNombre(resultSet.getNString(2));	//indico nombre
				p.setCantidad(resultSet.getDouble(3));	//indico cantidad
				p.setPrecio(resultSet.getDouble(4));	//indico precio
				p.setFechaCrear(resultSet.getDate(5));	//indico fecha_crear
				p.setFechaActualizar(resultSet.getDate(6));	//indico fecha_actualizar				
				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return p;
		//Nota!= Como se ver�... en este m�todo no hemos usado "RollBack()" ni nada, porque eso no era necesario. Ya que s�lo obtengo los datos de los productos de la tabla de mi base de datos.
	}
	
	/*
	 * *******************************
	 * M�todo Conexi�n desde el Pool *
	 * *******************************
	 * �ste m�todo va a ser privado, de modo que s�lo se va a poder acceder desde la clase "productoDAO"
	 * �ste m�todo va a retornar una conexi�n hacia la base de datos.
	 */
	private Connection obtenerConexion() throws SQLException {
		
		//como la clase "Conexion" tiene s�lo m�todos est�ticos entonces lo vamosa  retornar aqu�. Adem�s de eso a�ado una excepci�n al m�todo para que me la trate.
		return Conexion.getConnection();
	}

}
