package com.ecodeup.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecodeup.dao.ProductoDAO;
import com.ecodeup.model.Producto;

/**
 * Servlet implementation class ProductoController
 */
@WebServlet(description = "administra peticiones para la tabla productos", urlPatterns = { "/productos" })	// <-- Dirección y Descripción del Servlet
public class ProductoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductoController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String opcion = request.getParameter("opcion");
		
		if (opcion.equals("crear")) {
			
			System.out.println("Usted ha presionado la opcion crear");
			// Agrego la página a la que quiero redireccionar
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
			//para pasarle ya la página seleccionada tengo que hacer un "forward" del objeto tipo RequestDispatcher... pasándole el "request" y el "response"
			requestDispatcher.forward(request, response);
			
		} else if(opcion.equals("listar")){
			
			ProductoDAO productoDAO = new ProductoDAO();
			List<Producto> lista = new ArrayList<>();
			try {
				lista = productoDAO.obtenerProductos();
				for (Producto producto : lista) {
					System.out.println(producto);
				}
				//enviamos desde el servlet un parámetro hacia la vista... para eso utilizo el siguiente código:
				request.setAttribute("lista", lista);		//1er parámetro es el nombre con el que voy a enviar el objeto o variable hacia la vista. en el 2do parámetro debo colocar el nombre de la variable o parámetro que le voy a pasar a la página de tipo JSP
				
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
				requestDispatcher.forward(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Usted ha presionado la opcion listar");
		} else if (opcion.equals("meditar")) {
			int id = Integer.parseInt(request.getParameter("id"));
			System.out.println("Editar id: " + id);
			ProductoDAO productoDAO = new ProductoDAO();
			Producto p = new Producto();
			try {
				p = productoDAO.obtenerProducto(id);
				System.out.println(p);
				
				request.setAttribute("producto", p);
				
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
				requestDispatcher.forward(request, response);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (opcion.equals("eliminar")) {
			ProductoDAO productoDAO = new ProductoDAO();
			int id = Integer.parseInt(request.getParameter("id"));
			try {
				productoDAO.eliminar(id);
				System.out.println("Registro eliminado satisfactoriamente...");
				//volvemos al index.jsp
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");
				requestDispatcher.forward(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// Tras haber aclarado el "method" de tipo "post" en el formulario de la pág ".jsp", procedemos a obtener el valor del "input" de tipo "hidden" del formulario.
		String opcion = request.getParameter("opcion");
		
		

		/* ****************
		 * Guardar Producto
		 * ****************
		 */
		if (opcion.equals("guardar")) {
			
			Producto producto = new Producto();
			
			// obtener los datos del formulario para guardar en la tabla productos
			producto.setNombre(request.getParameter("nombre"));
			producto.setCantidad(Double.parseDouble(request.getParameter("cantidad")));
			producto.setPrecio(Double.parseDouble(request.getParameter("precio")));
				// obtener la fecha actual a través de código:
			Date fechaActual = new Date();
			
			producto.setFechaCrear(new java.sql.Date(fechaActual.getTime()));
			
			//vamos a persistirlo... y para ello necesito el objeto "productoDAO"
			ProductoDAO productoDAO = new ProductoDAO();
			try {
				productoDAO.guardar(producto);
				System.out.println("Registro guardado satisfactoriamente...");
				//volvemos al index.jsp
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");
				requestDispatcher.forward(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		/* ********************
		 * Actualizar Producto
		 * ********************
		 */
		else if (opcion.equals("editar")) {

			Producto producto = new Producto();
			
			// obtener los datos del formulario para guardar en la tabla productos
			producto.setId(Integer.parseInt(request.getParameter("id")));
			producto.setNombre(request.getParameter("nombre"));
			producto.setCantidad(Double.parseDouble(request.getParameter("cantidad")));
			producto.setPrecio(Double.parseDouble(request.getParameter("precio")));
				// obtener la fecha actual a través de código:
			Date fechaActual2 = new Date();
			producto.setFechaActualizar(new java.sql.Date(fechaActual2.getTime()));;
			System.out.println(new java.sql.Date(fechaActual2.getTime()));
			
			ProductoDAO productoDAO = new ProductoDAO();
			try {
				productoDAO.editar(producto);
				System.out.println("Registro editado satisfactoriamente...");
				//volvemos al index.jsp
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");
				requestDispatcher.forward(request, response);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		
		//doGet(request, response);
	}

}
