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
	$(document).ready(function(){
	$.ajax({ url: "/cargarTablaDeOfertas",
		context: document.body,
		success : function(result)
		{
			$("#gestion-de-ofertas").html(result);
		}
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
		
		<div id="gestion-de-ofertas">
		</div>
	</div>
</body>
</html>