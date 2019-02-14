package co.com.ceiba.parqueadero.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<VehiculoEntity, Long>{
	List<VehiculoEntity> findAllByTipoVehiculo(String tipoVehiculo);
	VehiculoEntity findByPlaca(String placa);
}
