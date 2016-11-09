<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
	function limpiarAgregarOfertaForm()
	{
		document.getElementById("agregarOferta-form").reset();
	}

	$(document).ready(function()
	{
		$("#mostrar-ingresar-oferta-submit").click(function()
		{
			$.ajax(
			{
				url : "${pageContext.request.contextPath}/cargarInsertarOfertas",
				success : function(result)
				{
					$("#gestion-de-ofertas").html(result);
				}
			});
		});
	});
	
	

</script>
<div id="tabla-ofertas">
	<div class="row">
		<table class="table table-striped">
			<thead>
				<tr>
					<th class="text-center">Título de Oferta</th>
					<th class="text-center">Comercio</th>
					<th class="text-center">Descripción</th>
					<th class="text-center">Categoría</th>
					<th class="text-center">Ciudad</th>
					<th class="text-center">Estado</th>
					<th class="text-center" colspan="2">Acciones</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${listaDeOfertas}" var="oferta">
					<tr>
						<td>${oferta.tituloDeOferta}</td>
						<td>${oferta.comercio}</td>
						<td>${oferta.descripcion}</td>
						<td>${oferta.categoria.nombre}</td>
						<td>${oferta.ciudad}
						<td>${oferta.estado ? "Activa" : "Inactiva"}</td>
						<td>editar</td>
						<td>eliminar</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="row">
		<div class="row" align="right">
			<div class="col-md-12">
				<div class="form-group">
					<button id="mostrar-ingresar-oferta-submit" type="button"
						class="btn btn-default">Ingresar nueva oferta</button>
				</div>
			</div>
		</div>
	</div>
</div>