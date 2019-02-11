package co.com.ceiba.parqueadero.model;

public class ParqueoVehiculoFactory {

	public ParqueoVehiculo costoParqueo(String tipoVehiculo) {
		if("CARRO".equals(tipoVehiculo))
			return new ParqueoVehiculoCarro();
		else if("MOTO".equals(tipoVehiculo)) 
			return new ParqueoVehiculoMoto();
		else
			return null;		
	}
	
	
}
