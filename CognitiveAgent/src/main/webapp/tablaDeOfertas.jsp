<jsp:include page="gestionDeOfertas.jsp" />

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="container">
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
		<form action="${pageContext.request.contextPath}/insertarOfertas" method="GET">
		<div class="row">
			<div class="row" align="right">
				<div class="col-md-12">
					<div class="form-group">
						<button id="mostrar-ingresar-oferta-submit" type="submit"
							class="btn btn-default">Ingresar nueva oferta</button>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</div>