package co.com.ceiba.parqueadero.model;

import co.com.ceiba.parqueadero.persistence.VehiculoEntity;

public interface ParqueoVehiculo {

	int calcularValorAPagar(int horasTotales, VehiculoEntity vehiculoEntity);
	public int getMaximoVehiculosPermitidos();
}
