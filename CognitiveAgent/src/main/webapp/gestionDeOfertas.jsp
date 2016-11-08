<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Gestión de Ofertas</title>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<link rel="stylesheet" href="css/bootstrap-theme.min.css">
	<script type="text/javascript" src="js/bootstrap.js"></script>
	<style>
		body
		{
			padding-top: 50px;
		}
		
		.starter-template
		{
			padding: 40px 15px;
			text-align: center;
		}
	</style>
	<script>
		function limpiarAgregarOfertaForm()
		{
			document.getElementById("agregarOferta-form").reset();
		}
		
		$(document).ready(function() {
			$("#mostrar-ingresar-oferta-submit").click(function() {
				$.ajax({
					url : "/insertarOferta.jsp",
					success : function(result) {
						$("#gestion-de-ofertas").html(result);
					}
				});
			});
		});
	</script>
</head>
<body>
	<div class="container">
		<div class="row">
			<ul class="nav nav-tabs">
				<li role="presentation" class="active"><a href="#">Gestión de ofertas</a></li>
				<li role="presentation"><a href="#">Reacción de Ofertas</a></li>
				<li role="presentation"><a href="#">Consultas Realizadas</a></li>
			</ul>
		</div>
		<div id="tabla-ofertas">
				<div class="row">
					<table class="table table-striped">
					<thead>
						<tr>
							<th>Título de Oferta</th>
							<th>Comercio</th>
							<th>Descripción</th>
							<th>Categoría</th>
							<th>Ciudad</th>
							<th>Estado</th>
							<th>Acciones</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${listaDeOfertas}" var="oferta" varStatus="status">
							<tr>
								<td>${oferta.tituloDeOferta}</td>
								<td>${oferta.comercio}</td>
								<td>${oferta.descripcion}</td>
								<td>${oferta.categoria.nombre}</td>
								<td>${oferta.ciudad}</td>
								<td>${oferta.estado}</td>
								<div>
									<td>editar</td>
									<td>eliminar</td>
								</div>
								
							</tr>
						</c:forEach>
					</tbody>
					</table>
				</div>
				<div class="row">
					<div class="row" align="right">
						<div class="col-md-10">
							<div class="form-group">
								<button id="mostrar-ingresar-oferta-submit" type="button" class="btn btn-default">Ingresar nueva oferta</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		<div id="gestion-de-ofertas">
		</div>
	</div>
</body>
</html>