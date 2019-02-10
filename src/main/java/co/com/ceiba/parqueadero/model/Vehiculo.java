package co.com.ceiba.parqueadero.model;

import java.util.Optional;

import co.com.ceiba.parqueadero.persistence.VehiculoEntity;
import co.com.ceiba.parqueadero.persistence.VehiculoRepository;

public class Vehiculo {

	private VehiculoRepository vehiculoRepository;

	public Vehiculo(VehiculoRepository vehiculoRepository) {
		this.vehiculoRepository = vehiculoRepository;
	}

	public String nuevoVehiculo(VehiculoEntity vehiculoEntity) {
		Optional<VehiculoEntity> vehiculoValidacion = vehiculoRepository.findById(vehiculoEntity.getId());
		if (!vehiculoValidacion.isPresent()) {
			vehiculoRepository.save(vehiculoValidacion.get());
			return "OK";
		}
		return "EL VEHICULO YA EXISTE";

	}

}
