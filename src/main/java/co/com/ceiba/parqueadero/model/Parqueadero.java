package co.com.ceiba.parqueadero.model;

import java.util.Date;

import co.com.ceiba.parqueadero.persistence.VehiculoEntity;

public class Parqueadero {
	
	private final int MAXIMO_MOTOS=10;
	private final int MAXIMO_CARROS=20;
	private final int HORA_CARRO=1000;
	private final int HORA_MOTO=500;
	private final int DIA_CARRO=8000;
	private final int DIA_MOTO=4000;
	
	private int totalCarrosParqueados=0;
	private int totalMotosParqueadas=0;
	
	private enum tipoDeVehiculo{
		CARRO,
		MOTO
	}
	
	public Parqueadero() {
		
	}
	
	
	public void salidaVehiculo(VehiculoEntity vehiculo,Date fechaHoraDeSalida) {
		
		
	}

}
