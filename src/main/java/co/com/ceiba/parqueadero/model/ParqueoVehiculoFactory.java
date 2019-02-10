package co.com.ceiba.parqueadero.model;

public class ParqueoVehiculoFactory {

	public ParqueoVehiculo costoParqueo(String tipoVehiculo) {
		if(tipoVehiculo.equals("CARRO"))
			return new ParqueoVehiculoCarro();
		else if(tipoVehiculo.equals("MOTO")) 
			return new ParqueoVehiculoMoto();
		else
			return null;		
	}
	
	
}
