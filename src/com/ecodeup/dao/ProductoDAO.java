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

	//vamos a crear un objeto de tipo Connection para obtener una conexión del Pool de Conexiones
	private Connection connection;
	private PreparedStatement statement;
	
	private boolean estadoOperacion; 	//con esta Bandera veremos cuándo se realizó una sentencia SQL
	
	
	/*
	 * *******************
	 * Método Guardar ****
	 * *******************
	 * 		método para retornar boolean para saber si se realizó o no una operación
	 */
	public boolean guardar(Producto producto) throws SQLException {
		
		String sql = null;
		estadoOperacion = false;	//indicando que la operación aún no ha sido realizado
		
		connection = obtenerConexion();		//obtenerConexion() tiene una excepción; por ende, tenemos que agregar aquí también una excepción
		
		//Try-Catch para el RollBack de todo ésto por si no se ejecuta correctamente.
		try {
			//transacciones
			connection.setAutoCommit(false);	//Aquí indico que mi transacción está a punto de comenzar y, que todo lo que se venga de aquí hacia adelante, va a ser una transacción completa.
			
			sql = "INSERT INTO productos (id, nombre, cantidad, precio, fecha_crear, fecha_actualizar) VALUES (?,?,?,?,?,?)";		/* Aquí tendremos en cuenta el PreparedStatement
							 * Antiguamente habíamos visto como hacer un CRUD utilizando sólo Statement. Cuando utilizo Statement coloco en la variable "sql" toda la sentencia SQL en crudo; pero...
							 * Cuando utilizo PreparedStatement puedo parametrizar esa consulta o esa sentencia SQL. Para esto lo haremos de la siguiente manera:
							 * Ejemplo: "INSERT INTO productos (//nombre de cada columna) VALUES (Valores a ingresar, //con signo de pregunta dentro//, por cada columna mencionada)"
							 * 
							 * Ahora sí, creamos el Statement (normal) con la conexión y la sentencia SQL del PreparedStatement.
							 * Por ésto, no ejecutamos el "execute" sino que le pasamos los parámetros que le declaramos en el "PreparedStatement".
							 */
			statement = connection.prepareStatement(sql); 
			
			//el "set" de esta sentencia... dependerá de qué tipo de dato estemos manejando
			statement.setString(1, null);	//id
			statement.setString(2, producto.getNombre());	//nombre
			statement.setDouble(3, producto.getCantidad());	//cantidad
			statement.setDouble(4, producto.getPrecio());	//Precio
			statement.setDate(5, producto.getFechaCrear());	//fecha_crear
			statement.setDate(6, producto.getFechaActualizar());	//fecha_actualizar
			
			//Ahora voy a enviar la sentencia SQL con el "Statement" ya preparado
			//Además, voy a agregar el valor booleano a estadoOperacion siempre y cuando executeUpdate() retorne un valor mayor a 0 (cero)
			estadoOperacion =  statement.executeUpdate() > 0;	//executeUpdate() retorna un INT (entero) cuando la sentencia SQL ha sido ejecutada exitosamente
			
			//Ahora voy a ejecutar un Commit...
			connection.commit();
			//cierro el Statement
			statement.close();
			//cierro la conexion  ((Aunque en realidad, la devuelvo al Pool de Conexiones))
			connection.close();
		} catch (SQLException e) {
			//En el caso de que alguna conexión no funcione... entonces que me haga un RollBack de todo lo que haya hecho hasta ese momento
			connection.rollback();
			e.printStackTrace();
		} 
		
		
		return estadoOperacion;
	}
	
	
	
	/*
	 * *******************
	 * Método Editar  ****
	 * *******************
	 * 		método para Editar un registro
	 */
	public boolean editar(Producto producto) throws SQLException {
		
		String sql = null;
		estadoOperacion = false;	//indicando que la operación aún no ha sido realizado
		
		connection = obtenerConexion();		//obtenerConexion() tiene una excepción; por ende, tenemos que agregar aquí también una excepción
		
		try {
			connection.setAutoCommit(false);
			sql = "UPDATE productos SET nombre=?, cantidad=?, precio=?, fecha_actualizar=? WHERE id=?";		/* Colocamos luego de "SET" el nombre de la columna a modificar, continuado de un " = " (igual) y seguido de un " ? " (signo de interrogación)
																															 * Luego, WHERE id=? para poder cambiar los datos dependiendo del ID solicitado
																															 */
			statement = connection.prepareStatement(sql);	//pasamos la sentencia SQL antes de pasarle los parámetros...
			
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
	 * Método Eliminar ****
	 * ********************
	 * 		método para Eliminar un registro
	 * 		Aquí sólo necesitamos el Id del producto
	 */
	public boolean eliminar(int idProducto) throws SQLException {
		
		String sql = null;
		estadoOperacion = false;	//indicando que la operación aún no ha sido realizado
		
		connection = obtenerConexion();		//obtenerConexion() tiene una excepción; por ende, tenemos que agregar aquí también una excepción
		
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
	 * Método Obtener todos los Productos****
	 * **************************************
	 * 		método para obtener productos
	 * 		Aquí no necesitaremos obtener previamente algún objeto
	 *		Aquí vamos a crear un método de tipo "List<>" del paquete de clases "java.util", ya que vamos a traer una lista de objetos de tipo Producto. Con ésto vamos a obtener la lista de todos los productos de la base de datos
	 * 		
	 */
	public List<Producto> obtenerProductos() throws SQLException {
		
		//En éste caso Utilizaremos el ResultSet
		ResultSet resultSet = null;		//Este objeto ResultSet me obtiene todos los registros de la consulta que yo voy a hacer hacia la base de datos. En este caso, de la tabla "productos"
		
		List<Producto> listaProductos = new ArrayList<>();	//Creé una lista de tipo producto a la que voy a llamar... "listaProductos"; y es la que me va a almacenar cada registro (cada Fila) devuelto por el "ResultSet"
		
		String sql = null;
		estadoOperacion = false;	//indicando que la operación aún no ha sido realizado
		
		connection = obtenerConexion();		//obtenerConexion() tiene una excepción; por ende, tenemos que agregar aquí también una excepción
		
		try {
			sql = "SELECT * FROM productos";
			
			statement = connection.prepareStatement(sql);	//creamos el canal de conexión ... el statement
			resultSet = statement.executeQuery(sql);	/* Éste "statement.executeQuery()" me devuelve un "ResultSet"
														 * Ahora que tengo el "ResultSet", se podría decir que tengo los datos de la tabla en "una Matris"
														 * Tengo que, cada registro, poderlo obtener a través de un objeto; y, a su vez, poner este objeto en una lista, ...como la que vemos al inicio del método "List<Producto>" 
														 */
			while(resultSet.next()) {	//Este "while" (colocamos while porque son varios registros {filas})  se va a ejecutar siempre y cuando el ResultSet tenga un registro que mostrar...
				
				Producto p = new Producto();	//Creo un objeto para ir almacenando ahí cada uno de los registro
				p.setId(resultSet.getInt(1));	//tengo que indicarle que está, en la columna 1 (uno) de la base de datos, el "id".
				p.setNombre(resultSet.getNString(2));	//indico nombre
				p.setCantidad(resultSet.getDouble(3));	//indico cantidad
				p.setPrecio(resultSet.getDouble(4));	//indico precio
				p.setFechaCrear(resultSet.getDate(5));	//indico fecha_crear
				p.setFechaActualizar(resultSet.getDate(6));	//indico fecha_actualizar

				//ahora indico que todos esos objetos "p" obtenidos sean añadidos a la "listaProductos"
				listaProductos.add(p);	//En cada ciclo del "while" se va ir añadiendo los productos a la lista.
				
				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return listaProductos;	//devolvemos la lista para luego ser mostrada en pantalla con el visor
		//Nota!= Como se verá... en este método no hemos usado "RollBack()" ni nada, porque eso no era necesario. Ya que sólo obtengo los datos de los productos de la tabla de mi base de datos.
	}

	/*
	 * **********************************
	 * Método Obtener un único producto *
	 * **********************************
	 * 		Método para obtener un único producto antes de ser editado. No va a venir una lista, sino que únicamente un sólo objeto de tipo producto
	 */
	public Producto obtenerProducto(int idProducto) throws SQLException {
		
		
		//En éste caso Utilizaremos el ResultSet
		ResultSet resultSet = null;		//Este objeto ResultSet me obtiene todos los registros de la consulta que yo voy a hacer hacia la base de datos. En este caso, de la tabla "productos"

		Producto p = new Producto();	//Creo un objeto para ir almacenando ahí cada uno de los registro de cada columna
		
		String sql = null;
		estadoOperacion = false;	//indicando que la operación aún no ha sido realizado
		
		connection = obtenerConexion();		//obtenerConexion() tiene una excepción; por ende, tenemos que agregar aquí también una excepción
		
		try {
			sql = "SELECT * FROM productos WHERE id=?";
			
			statement = connection.prepareStatement(sql);
			statement.setInt(1, idProducto);
			
			resultSet = statement.executeQuery();	// Éste "statement.executeQuery()" me devuelve un "ResultSet"
			
			if(resultSet.next()) {	//Este "if" (colocamos if porque es un solo registro {fila}) se va a ejecutar siempre y cuando el ResultSet tenga un registro que mostrar...
				
				p.setId(resultSet.getInt(1));	//tengo que indicarle que está, en la columna 1 (uno) de la base de datos, el "id".
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
		//Nota!= Como se verá... en este método no hemos usado "RollBack()" ni nada, porque eso no era necesario. Ya que sólo obtengo los datos de los productos de la tabla de mi base de datos.
	}
	
	/*
	 * *******************************
	 * Método Conexión desde el Pool *
	 * *******************************
	 * Éste método va a ser privado, de modo que sólo se va a poder acceder desde la clase "productoDAO"
	 * Éste método va a retornar una conexión hacia la base de datos.
	 */
	private Connection obtenerConexion() throws SQLException {
		
		//como la clase "Conexion" tiene sólo métodos estáticos entonces lo vamosa  retornar aquí. Además de eso añado una excepción al método para que me la trate.
		return Conexion.getConnection();
	}

}
