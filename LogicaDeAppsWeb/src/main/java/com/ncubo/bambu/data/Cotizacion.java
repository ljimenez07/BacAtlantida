package com.ncubo.bambu.data;

public class Cotizacion
{
	private int idCotizacion;
	private int idLlamada;
	private String nombreOportunidad;
	private String fechaCotizacion;
	private int idCompania;
	private String descripcion;
	private String fechaCierre;
	private boolean mantenimiento;
	private String mesCierre;
	private float monto;
	private int probabilidad;
	private int idTipoCotizacion;
	private String noFactura;
	private int idDivisa;
	private String centroCostos;
	private float mesesGarantia;
	private boolean sterilAire;
	private float montoSterilAire;
	private int idZona;
	private int idEtapaCotizacion;
	private int idAsesor;
	private int idTipoProducto;
	private int idTipoAprobacion;
	private int idTipoDescuento;
	private int creadoPor;
	private int modificadoPor;
	private String fechaCreacion;
	private String fechaModificacion;
	private int idPropietario;
	private String numeroReferancia;
	private String ordenCompra;
	private int idDenegadoPorQue;
	private int idDenegadoContraQuien;
	private int idDenegadoQueMarca;
	private boolean registroBorrado;
	private int borradoPor;
	private String fechaBorrado;
	private int numeroVersion;
	private String fechaEnvio;
	private String fechaCompromisoCliente;
	private int idSegmento;
	private int idDepartamento;
	private String fechaFin;
	private float duracionEnMillisegundos;
	private int idPersona;
	private String numeroPedido;
	private String codigoChat;
	private String fechaDeAprovacion;
	
	public int obtenerIdCotizacion()
	{
		return idCotizacion;
	}
	public void establecerIdCotizacion(int idCotizacion)
	{
		this.idCotizacion = idCotizacion;
	}
	public int obtenerIdLlamada()
	{
		return idLlamada;
	}
	public void establecerIdLlamada(int idLlamada)
	{
		this.idLlamada = idLlamada;
	}
	public String obtenerNombreOportunidad()
	{
		return nombreOportunidad;
	}
	public void establecerNombreOportunidad(String nombreOportunidad)
	{
		this.nombreOportunidad = nombreOportunidad;
	}
	public String obtenerFechaCotizacion()
	{
		return fechaCotizacion;
	}
	public void establecerFechaCotizacion(String fechaCotizacion)
	{
		this.fechaCotizacion = fechaCotizacion;
	}
	public int obtenerIdCompania()
	{
		return idCompania;
	}
	public void establecerIdCompania(int idCompania)
	{
		this.idCompania = idCompania;
	}
	public String obtenerDescripcion()
	{
		return descripcion;
	}
	public void establecerDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}
	public String obtenerFechaCierre()
	{
		return fechaCierre;
	}
	public void establecerFechaCierre(String fechaCierre)
	{
		this.fechaCierre = fechaCierre;
	}
	public boolean esMantenimiento()
	{
		return mantenimiento;
	}
	public void establecerMantenimiento(boolean mantenimiento)
	{
		this.mantenimiento = mantenimiento;
	}
	public String obtenerMesCierre()
	{
		return mesCierre;
	}
	public void establecerMesCierre(String mesCierre)
	{
		this.mesCierre = mesCierre;
	}
	public float obtenerMonto()
	{
		return monto;
	}
	public void establecerMonto(float monto)
	{
		this.monto = monto;
	}
	public int obtenerProbabilidad()
	{
		return probabilidad;
	}
	public void establecerProbabilidad(int probabilidad)
	{
		this.probabilidad = probabilidad;
	}
	public int obtenerIdTipoCotizacion()
	{
		return idTipoCotizacion;
	}
	public void establecerIdTipoCotizacion(int idTipoCotizacion)
	{
		this.idTipoCotizacion = idTipoCotizacion;
	}
	public String obtenerNoFactura()
	{
		return noFactura;
	}
	public void establecerNoFactura(String noFactura)
	{
		this.noFactura = noFactura;
	}
	public int obtenerIdDivisa()
	{
		return idDivisa;
	}
	public void establecerIdDivisa(int idDivisa)
	{
		this.idDivisa = idDivisa;
	}
	public String obtenerCentroCostos()
	{
		return centroCostos;
	}
	public void establecerCentroCostos(String centroCostos)
	{
		this.centroCostos = centroCostos;
	}
	public float obtenerMesesGarantia()
	{
		return mesesGarantia;
	}
	public void establecerMesesGarantia(float mesesGarantia)
	{
		this.mesesGarantia = mesesGarantia;
	}
	public boolean esSterilAire()
	{
		return sterilAire;
	}
	public void establecerSterilAire(boolean sterilAire)
	{
		this.sterilAire = sterilAire;
	}
	public float obtenerMontoSterilAire()
	{
		return montoSterilAire;
	}
	public void establecerMontoSterilAire(float montoSterilAire)
	{
		this.montoSterilAire = montoSterilAire;
	}
	public int obtenerIdZona()
	{
		return idZona;
	}
	public void establecerIdZona(int idZona)
	{
		this.idZona = idZona;
	}
	public int obtenerIdEtapaCotizacion()
	{
		return idEtapaCotizacion;
	}
	public void establecerIdEtapaCotizacion(int idEtapaCotizacion)
	{
		this.idEtapaCotizacion = idEtapaCotizacion;
	}
	public int obtenerIdAsesor()
	{
		return idAsesor;
	}
	public void establecerIdAsesor(int idAsesor)
	{
		this.idAsesor = idAsesor;
	}
	public int obtenerIdTipoProducto()
	{
		return idTipoProducto;
	}
	public void establecerIdTipoProducto(int idTipoProducto)
	{
		this.idTipoProducto = idTipoProducto;
	}
	public int obtenerIdTipoAprobacion()
	{
		return idTipoAprobacion;
	}
	public void establecerIdTipoAprobacion(int idTipoAprobacion)
	{
		this.idTipoAprobacion = idTipoAprobacion;
	}
	public int obtenerIdTipoDescuento()
	{
		return idTipoDescuento;
	}
	public void establecerIdTipoDescuento(int idTipoDescuento)
	{
		this.idTipoDescuento = idTipoDescuento;
	}
	public int obtenerCreadoPor()
	{
		return creadoPor;
	}
	public void establecerCreadoPor(int creadoPor)
	{
		this.creadoPor = creadoPor;
	}
	public int obtenerModificadoPor()
	{
		return modificadoPor;
	}
	public void establecerModificadoPor(int modificadoPor)
	{
		this.modificadoPor = modificadoPor;
	}
	public String obtenerFechaCreacion()
	{
		return fechaCreacion;
	}
	public void establecerFechaCreacion(String fechaCreacion)
	{
		this.fechaCreacion = fechaCreacion;
	}
	public String obtenerFechaModificacion()
	{
		return fechaModificacion;
	}
	public void establecerFechaModificacion(String fechaModificacion)
	{
		this.fechaModificacion = fechaModificacion;
	}
	public int obtenerIdPropietario()
	{
		return idPropietario;
	}
	public void establecerIdPropietario(int idPropietario)
	{
		this.idPropietario = idPropietario;
	}
	public String obtenerNumeroReferancia()
	{
		return numeroReferancia;
	}
	public void establecerNumeroReferancia(String numeroReferancia)
	{
		this.numeroReferancia = numeroReferancia;
	}
	public String obtenerOrdenCompra()
	{
		return ordenCompra;
	}
	public void establecerOrdenCompra(String ordenCompra)
	{
		this.ordenCompra = ordenCompra;
	}
	public int obtenerIdDenegadoPorQue()
	{
		return idDenegadoPorQue;
	}
	public void establecerIdDenegadoPorQue(int idDenegadoPorQue)
	{
		this.idDenegadoPorQue = idDenegadoPorQue;
	}
	public int obtenerIdDenegadoContraQuien()
	{
		return idDenegadoContraQuien;
	}
	public void establecerIdDenegadoContraQuien(int idDenegadoContraQuien)
	{
		this.idDenegadoContraQuien = idDenegadoContraQuien;
	}
	public int obtenerIdDenegadoQueMarca()
	{
		return idDenegadoQueMarca;
	}
	public void establecerIdDenegadoQueMarca(int idDenegadoQueMarca)
	{
		this.idDenegadoQueMarca = idDenegadoQueMarca;
	}
	public boolean esRegistroBorrado()
	{
		return registroBorrado;
	}
	public void establecerRegistroBorrado(boolean registroBorrado)
	{
		this.registroBorrado = registroBorrado;
	}
	public int obtenerBorradoPor()
	{
		return borradoPor;
	}
	public void establecerBorradoPor(int borradoPor)
	{
		this.borradoPor = borradoPor;
	}
	public String obtenerFechaBorrado()
	{
		return fechaBorrado;
	}
	public void establecerFechaBorrado(String fechaBorrado)
	{
		this.fechaBorrado = fechaBorrado;
	}
	public int obtenerNumeroVersion()
	{
		return numeroVersion;
	}
	public void establecerNumeroVersion(int numeroVersion)
	{
		this.numeroVersion = numeroVersion;
	}
	public String obtenerFechaEnvio()
	{
		return fechaEnvio;
	}
	public void establecerFechaEnvio(String fechaEnvio)
	{
		this.fechaEnvio = fechaEnvio;
	}
	public String obtenerFechaCompromisoCliente()
	{
		return fechaCompromisoCliente;
	}
	public void establecerFechaCompromisoCliente(String fechaCompromisoCliente)
	{
		this.fechaCompromisoCliente = fechaCompromisoCliente;
	}
	public int obtenerIdSegmento()
	{
		return idSegmento;
	}
	public void establecerIdSegmento(int idSegmento)
	{
		this.idSegmento = idSegmento;
	}
	public int obtenerIdDepartamento()
	{
		return idDepartamento;
	}
	public void establecerIdDepartamento(int idDepartamento)
	{
		this.idDepartamento = idDepartamento;
	}
	public String obtenerFechaFin()
	{
		return fechaFin;
	}
	public void establecerFechaFin(String fechaFin)
	{
		this.fechaFin = fechaFin;
	}
	public float obtenerDuracionEnMillisegundos()
	{
		return duracionEnMillisegundos;
	}
	public void establecerDuracionEnMillisegundos(float duracionEnMillisegundos)
	{
		this.duracionEnMillisegundos = duracionEnMillisegundos;
	}
	public int obtenerIdPersona()
	{
		return idPersona;
	}
	public void establecerIdPersona(int idPersona)
	{
		this.idPersona = idPersona;
	}
	public String obtenerNumeroPedido()
	{
		return numeroPedido;
	}
	public void establecerNumeroPedido(String numeroPedido)
	{
		this.numeroPedido = numeroPedido;
	}
	public String obtenerCodigoChat()
	{
		return codigoChat;
	}
	public void establecerCodigoChat(String codigoChat)
	{
		this.codigoChat = codigoChat;
	}
	public String obtenerFechaDeAprovacion()
	{
		return fechaDeAprovacion;
	}
	public void establecerFechaDeAprovacion(String fechaDeAprovacion)
	{
		this.fechaDeAprovacion = fechaDeAprovacion;
	}
}
