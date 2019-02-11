package co.com.ceiba.parqueadero.model;
import co.com.ceiba.parqueadero.persistence.VehiculoEntity;
import co.com.ceiba.parqueadero.persistence.VehiculoRepository;

public class Vehiculo {

	private VehiculoRepository vehiculoRepository;

	public Vehiculo(VehiculoRepository vehiculoRepository) {
		this.vehiculoRepository = vehiculoRepository;
	}

	public String nuevoVehiculo(VehiculoEntity vehiculoEntity) {
		VehiculoEntity vehiculoValidacion = vehiculoRepository.findById(vehiculoEntity.getId()).orElse(null);
		if (vehiculoValidacion!=null) {
			vehiculoRepository.save(vehiculoValidacion);
			return "OK";
		}else {
			return "EL VEHICULO YA EXISTE";
		}
		

	}

}
