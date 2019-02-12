package co.com.ceiba.parqueadero.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParqueoRepository extends JpaRepository<ParqueoEntity, Long>{

	Optional<ParqueoEntity> findByVehiculoId(Long vehiculoId);
	

}
