package co.com.ceiba.parqueadero.model;

import java.util.Calendar;
import java.util.Optional;
import co.com.ceiba.parqueadero.persistence.ParqueoEntity;
import co.com.ceiba.parqueadero.persistence.ParqueoRepository;
import co.com.ceiba.parqueadero.persistence.VehiculoEntity;
import co.com.ceiba.parqueadero.persistence.VehiculoRepository;

public class Parqueo {
	private ParqueoRepository parqueoRepository;
	private VehiculoRepository vehiculoRepository;
	private Calendar calendar;

	public Parqueo(ParqueoRepository parqueoRepository, VehiculoRepository vehiculoRepository) {
		this.parqueoRepository = parqueoRepository;
		this.vehiculoRepository = vehiculoRepository;
		this.calendar = Calendar.getInstance();
	}

	public String nuevoParqueo(ParqueoEntity parqueo) {
		try {
			Optional<VehiculoEntity> vehiculoEntity = vehiculoRepository.findById(parqueo.getVehiculoId());
			if (vehiculoEntity.isPresent()) {
				if (verificarPlacaYFechaVehiculo(vehiculoEntity.get()))
					return "EL VEHICULO NO PUEDE INGRESAR EL DIA DE HOY";
				if (verificarLimiteDeVehiculoNoEsExcedido(vehiculoEntity.get()))
					return "EL LIMITE DE: " + vehiculoEntity.get().getTipoVehiculo() + " HA SIDO SUPERADO";
				parqueoRepository.save(parqueo);
				return "OK";
			}			
			return "EL VEHICULO NO EXISTE";
		} catch (Exception e) {
			return "EL VEHICULO NO EXISTE";
		}

	}

	private boolean verificarPlacaYFechaVehiculo(VehiculoEntity vehiculoEntity) {
		Calendar fechaActual = (Calendar) calendar.clone();
		return (vehiculoEntity.getPlaca().charAt(0) == 'A'
				&& (fechaActual.get(Calendar.DAY_OF_WEEK) != 0 || fechaActual.get(Calendar.DAY_OF_WEEK) != 6));
	}

	private boolean verificarLimiteDeVehiculoNoEsExcedido(VehiculoEntity vehiculoEntity) {
		ParqueoVehiculoFactory costoParqueoFactory = new ParqueoVehiculoFactory();
		return (vehiculoRepository.findAllByTipoVehiculo(vehiculoEntity.getTipoVehiculo()).size() >= costoParqueoFactory
				.costoParqueo(vehiculoEntity.getTipoVehiculo()).getMaximoVehiculosPermitidos());
	}

	public String salidaDeParqueo(Long parqueoId) {
		Optional<ParqueoEntity> parqueoEntity = parqueoRepository.findById(parqueoId);
		if (!parqueoEntity.isPresent())
			return "EL PARQUEO INGRESADO NO EXISTE";
		else {
			ParqueoEntity parqueoEntityNuevo = parqueoEntity.get();
			Calendar fechaHoraDeSalida = (Calendar) calendar.clone();
			int valorAPagar = this.calcularValorAPagar(parqueoEntityNuevo);
			if(valorAPagar==-1)
				return "EL VEHICULO DEL PARQUEO NO SE PUDO ENCONTRAR";
			parqueoEntityNuevo.setFechaHoraDeSalida(fechaHoraDeSalida);
			parqueoEntityNuevo.setValorAPagar(valorAPagar);
			parqueoRepository.save(parqueoEntityNuevo);
			return "OK";
		}
	}

	public int calcularValorAPagar(ParqueoEntity parqueoEntity) {
		Optional<VehiculoEntity> vehiculoEntity = vehiculoRepository.findById(parqueoEntity.getVehiculoId());
		if (vehiculoEntity.isPresent()) {
			Calendar fechaActual = (Calendar) calendar.clone();
			double horasTotales = ((fechaActual.getTimeInMillis()
					- parqueoEntity.getFechaHoraDeEntrada().getTimeInMillis()) / 3600000.0);
			ParqueoVehiculoFactory costoParqueoFactory = new ParqueoVehiculoFactory();
			return costoParqueoFactory.costoParqueo(vehiculoEntity.get().getTipoVehiculo())
					.calcularValorAPagar((int) Math.ceil(horasTotales), vehiculoEntity.get());
		}
		return -1;
	}

	// TEST ONLY
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

}
