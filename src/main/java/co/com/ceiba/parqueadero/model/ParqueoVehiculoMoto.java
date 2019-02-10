package co.com.ceiba.parqueadero.model;

import co.com.ceiba.parqueadero.persistence.VehiculoEntity;

public class ParqueoVehiculoMoto implements ParqueoVehiculo {

	private static final int VALOR_HORA_MOTO = 500;
	private static final int VALOR_DIA_MOTO = 4000;
	private static final int CALCULAR_POR_HORAS = 9;
	private static final int ALTO_CILINDRAJE = 500;
	private static final int EXTRA_POR_ALTO_CILINDRAJE = 2000;
	private static final int HORAS_DEL_DIA = 24;
	private static final int MAXIMO_VEHICULOS_PERMITIDOS = 10;
	private int totalAPagar;

	public ParqueoVehiculoMoto() {
		this.totalAPagar = 0;
	}

	public int calcularValorAPagar(int horasTotales, VehiculoEntity vehiculoEntity) {
		totalAPagar=this.calcularValorSinCilindraje(horasTotales);
		if (vehiculoEntity.getCilindraje() > ALTO_CILINDRAJE) {
			totalAPagar += EXTRA_POR_ALTO_CILINDRAJE;
		}		
		return totalAPagar;
	}
	
	public int calcularValorSinCilindraje(int horasTotales) {
		if (horasTotales < CALCULAR_POR_HORAS) {
			totalAPagar += VALOR_HORA_MOTO * horasTotales;
		} else if (horasTotales < HORAS_DEL_DIA) {
			totalAPagar += VALOR_DIA_MOTO;
		} else if (horasTotales > HORAS_DEL_DIA) {
			while (horasTotales > HORAS_DEL_DIA) {
				totalAPagar += VALOR_DIA_MOTO;
				horasTotales -= HORAS_DEL_DIA;
			}
			this.calcularValorSinCilindraje(horasTotales);
		}
		return totalAPagar;
	}

	public int getMaximoVehiculosPermitidos() {
		return MAXIMO_VEHICULOS_PERMITIDOS;
	}

}
