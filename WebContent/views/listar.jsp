<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <!--Este es el "nameSpace" que debemos usar en todas las páginas JSP que vayamos a usar JSTL. Siempre se coloca arriba... creo-->
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Listar Producto</title>

<style type="text/css">body{background: black; color: white}</style>

</head>
<body>
	<h1>Listar Producto</h1>
	
	<table border="1">
		<tr>
			<td>Id</td>
			<td>Nombre</td>
			<td>Cantidad</td>
			<td>Precio</td>
			<td>Fecha Creación</td>
			<td>Fecha Actualización</td>
			<td>Acción</td>
		</tr>
		
		<!-- Voy a hacer un "for" para que las listas se formen dependiendo a la cantidad de los registros que vengan desde la Base de datos -->
		<c:forEach var="producto" items="${lista}">	<!-- "var" es donde se pone la variable u objeto que vaya a recibir cada registro de la lista por cada iteración-->
												<!-- "items" hace referencia a la lista que voy a recibir desde mi Servlet; pero no se pone únicamente el nombre de la lista... sino que, por lo general, se lo pone con una sintaxis especial; y esa sintaxis es utilizando el simbolo de dólar, luego apertura de llaves, colocamos el nombre del objeto que voy a recibir, y cerramos llaves -->
			<tr>
				<td><a href="productos?opcion=meditar&id=<c:out value="${ producto.id }"></c:out>"><c:out value="${ producto.id }"></c:out></a></td>	<!-- Luego de haberle pasado la acción hacia el controlador... vamos a pasarle el ID del objeto que vamos a editar. Para ello, debemos colocar el "&" (ampersand), luego el nombre de la variable "id", seguido de su valor "=<c:out value="${ producto.id }"></c:out>" -->
				<td><c:out value="${ producto.nombre }"></c:out></td>
				<td><c:out value="${ producto.cantidad }"></c:out></td>
				<td><c:out value="${ producto.precio }"></c:out></td>
				<td><c:out value="${ producto.fechaCrear }"></c:out></td>
				<td><c:out value="${ producto.fechaActualizar }"></c:out></td>
				<td><a href="productos?opcion=eliminar&id=<c:out value="${ producto.id }"></c:out>"> Eliminar </a></td>
			</tr>
		</c:forEach>
		
	</table>
	
</body>
</html>