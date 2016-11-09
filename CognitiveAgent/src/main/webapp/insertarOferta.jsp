<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>

	$(document).ready(function()
	{
		$("#imagen-publicidad-input").on("change", subirImagenPublicidad);
		$("#logo-comercio-input").on("change", subirImagenComercio);
		
		$("#limpiar-submit").click(function()
		{
			$.ajax(
			{
				url : "${pageContext.request.contextPath}/cargarTablaDeOfertas",
				success : function(result)
				{
					$("#gestion-de-ofertas").html(result);
				}
			});
		});
	});
	
	function subirImagenPublicidad()
	{
		$.ajax(
		{
			url: "/subirImagenPublicidad",
			type: "POST",
			data: new FormData($("#agregarOferta-form")[0]),
			enctype: 'multipart/form-data',
			processData: false,
			contentType: false,
			cache: false,
			success: function ()
			{
			}
		});
	}
		
	function subirImagenComercio()
	{
		$.ajax(
		{
			url: "/subirImagenComercio",
			type: "POST",
			data: new FormData($("#agregarOferta-form")[0]),
			enctype: 'multipart/form-data',
			processData: false,
			contentType: false,
			cache: false,
			success: function ()
			{
			}
		});
	}

</script>
<form action="/insertarOferta" id="agregarOferta-form" method="POST">
	<div id="agregarOferta">
		<div class="row">
			<div class="col-md-2">
				<div class="form-group">
					<label for="tituloOferta">Título de oferta</label>
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
					<label for="categoria">Estado</label><br>
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
					<p>* Solo las ofertas con estado "activa" serán mostradas en la aplicación.</p>
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
					<label for="logo-label">Logo del comercio</label>
					<input id="logo-comercio-input" type="file" name="logo-comercio-input" accept="*"/>
					<!--  <label class="btn btn-default btn-file">Seleccionar archivo
					<input type="file" style="display: none;" id="logo-comercio-input">
					</label>-->
				</div>
			</div>
			<div class="col-md-3">
				<div class="form-group">
					<label for="publicidad-label">Imagen de publicidad</label> 
					<input id="imagen-publicidad-input" type="file" name="imagen-publicidad-input" accept="*"/>
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