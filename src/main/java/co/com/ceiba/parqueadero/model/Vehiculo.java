package co.com.ceiba.parqueadero.model;
import co.com.ceiba.parqueadero.persistence.VehiculoEntity;
import co.com.ceiba.parqueadero.persistence.VehiculoRepository;

public class Vehiculo {

	private VehiculoRepository vehiculoRepository;

	public Vehiculo(VehiculoRepository vehiculoRepository) {
		this.vehiculoRepository = vehiculoRepository;
	}

	public VehiculoEntity nuevoVehiculo(VehiculoEntity vehiculoEntity) {
			return vehiculoRepository.save(vehiculoEntity);	
	}
	
	public VehiculoEntity buscarVehiculoPorPlaca(String placaVehiculo) {	
		return vehiculoRepository.findByPlaca(placaVehiculo);
	}
	

}
