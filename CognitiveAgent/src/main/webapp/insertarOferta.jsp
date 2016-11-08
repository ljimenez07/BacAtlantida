<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
$(document).ready(function()
	{
		$("#limpiar-submit").click(function()
		{
			$.ajax(
			{
				url : "/cargarTablaDeOfertas",
				success : function(result)
				{
					$("#gestion-de-ofertas").html(result);
				}
			});
		});
	});
</script>
<form action="/insertarOferta" method="post" enctype="multipart/form-data" id="agregarOferta-form">
	<div id="agregarOferta">
		<div class="row">
			<div class="col-md-2">
				<div class="form-group">
					<label for="tituloOferta">Titulo de oferta</label>
					<input type="text" class="form-control" name="tituloOferta-input">
				</div>
			</div>
			<div class="col-md-2">
				<div class="form-group">
					<label for="nombreComercio">Nombre del comercio</label>
					<input type="text" class="form-control" name="nombreComercio-input">
				</div>
			</div>
			<div class="col-md-2">
				<div class="form-group">
					<label for="ciudad">Ciudad</label>
					<input type="text" class="form-control" name="ciudad-combobox">
				</div>
			</div>
			<div class="col-md-2">
				<div class="form-group">
					<label for="categoria">Categoría</label>
					<div class="btn-group">
						<select class="form-control" name="categoria-select">
							<c:forEach items="${categorias}" var="categoria">
								<option value="${categoria.id}">${categoria.nombre}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
			<div class="col-md-2">
				<div class="form-group">
					<label for="categoria">Estado</label>
					<div class="btn-group">
						<select class="form-control" name="estado-select">
							<option value="1">Activa</option>
							<option value="0">Inactiva</option>
						</select>
					</div>
				</div>
			</div>
			<div class="col-md-2">
				<div>
					<p>* Solo las ofertas con estado "activa" serán mostradas en la aplicación</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-5">
				<div class="form-group">
					<label for="descripcion">Descripción</label>
					<textarea class="form-control" rows="4" name="descripcion-textarea"></textarea>
				</div>
			</div>
			<div class="col-md-5">
				<div class="form-group">
					<label for="restricciones">Restricciones</label>
					<textarea class="form-control" rows="4"
						name="restricciones-textarea"></textarea>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-5">
				<div>
					<p>*El usuario podrá escuchar esta descripción si presiona el
						botón de audio, pero no se despliega como texto en la pantalla.</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-2">
				<div class="form-group">
					<label for="vigenciaDesde">Vigencia desde</label> <input
						type="date" class="form-control" name="vigenciaDesde-input">
				</div>
			</div>
			<div class="col-md-3">
				<div class="form-group">
					<label for="vigenciaDesde">hasta</label> <input type="date"
						class="form-control" name="vigenciaHasta-input">
				</div>
			</div>
			<div class="col-md-2">
				<div class="form-group">
					<label for="logo-label">Logo del comercio</label> <label
						class="btn btn-default btn-file">Seleccionar archivo <input
						type="file" style="display: none;" name="logo-comercio-input">
					</label>
				</div>
			</div>
			<div class="col-md-3">
				<div class="form-group">
					<label for="publicidad-label">Imagen de publicidad</label> <label
						class="btn btn-default btn-file">Seleccionar archivo <input
						type="file" style="display: none;" name="imagen-publicidad-input">
					</label>
				</div>
			</div>
		</div>
		<div class="row" align="center">
			<div class="col-md-10">
				<div class="form-group">
					<button id="limpiar-submit" type="button" class="btn btn-default"
						onclick="limpiarAgregarOfertaForm()">Limpiar</button>
					<button id="ingresar-submit" type="submit" class="btn btn-default">Ingresar</button>
				</div>
			</div>
		</div>
	</div>
</form>