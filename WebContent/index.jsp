<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Men� de opciones</title>
<style type="text/css">body{background: black; color: white}</style>
</head>
<body>

	<h1>Men� de Opciones Productos</h1>
	
	<table border="1">
		
		<tr class="columna">
			<td class="fila"><a href="productos?opcion=crear">Crear un Producto</a></td>		<!-- Colocamos el nombre de la direcci�n del servlet al que quiero mapear la petici�n -->
		</tr>
		<tr class="columna">
			<td class="fila"><a href="productos?opcion=listar">Listar Productos</a></td>	
		</tr>
	
	</table>

</body>
</html>