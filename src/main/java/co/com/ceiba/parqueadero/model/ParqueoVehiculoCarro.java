package co.com.ceiba.parqueadero.model;

import co.com.ceiba.parqueadero.persistence.VehiculoEntity;

public class ParqueoVehiculoCarro implements ParqueoVehiculo {

	private static final int VALOR_HORA_CARRO=1000;	
	private static final int VALOR_DIA_CARRO=8000;	
	private static final int CALCULAR_POR_HORAS=9;	
	private static final int HORAS_DEL_DIA=24;
	private static final int MAXIMO_VEHICULOS_PERMITIDOS=20;
	private int totalAPagar;	
	
	public ParqueoVehiculoCarro() {
		this.totalAPagar = 0;
	}
	
	public int calcularValorAPagar(int horasTotales, VehiculoEntity vehiculoEntity) {		
		if (horasTotales < CALCULAR_POR_HORAS) {			
			totalAPagar += VALOR_HORA_CARRO * horasTotales;
		} else if(horasTotales<HORAS_DEL_DIA) {
			totalAPagar += VALOR_DIA_CARRO;
		}else if(horasTotales>HORAS_DEL_DIA) {
			while(horasTotales>HORAS_DEL_DIA) {
				totalAPagar+=VALOR_DIA_CARRO;
				horasTotales-=HORAS_DEL_DIA;
			}
			this.calcularValorAPagar(horasTotales, vehiculoEntity);
		}
		return totalAPagar;
	}

	public int getMaximoVehiculosPermitidos() {
		return MAXIMO_VEHICULOS_PERMITIDOS;
	}	
	
		
	

}
