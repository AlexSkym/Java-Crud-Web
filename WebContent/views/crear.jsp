<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Crear Producto</title>
<style type="text/css">body{background: black; color: white}</style>
</head>
<body>
	<h1>Crear Producto</h1>
	
	<form action="productos" method="post">		<!-- "action" refiere hacia d�nde quiero enviar esta petici�n (y lo quiero enviar hacia "ProductoController.java"... pero est� mapeado con el nombre "productos") -->
												<!-- "method" indica c�mo voy a hacer el env�o (en este caso lo haremos con el m�todo "post") -->
	 	<input type="hidden" name="opcion" value="guardar">
	 	
	 	<table border="1">
	 		<tr>
	 			<td>Nombre:</td>
	 			<td> <input type="text" name="nombre" size="50"> </td>
	 		</tr>
	 		<tr>
	 			<td>Cantidad:</td>
	 			<td> <input type="text" name="cantidad" size="50"> </td>
	 		</tr>
			<tr>
	 			<td>Precio:</td>
	 			<td> <input type="text" name="precio" size="50"> </td>
	 		</tr>
	 	</table>
	 	
	 	<input type="submit" value="Guardar">
	 	
	</form>
</body>
</html>