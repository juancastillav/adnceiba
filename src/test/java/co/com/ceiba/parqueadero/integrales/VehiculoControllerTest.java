package co.com.ceiba.parqueadero.integrales;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import co.com.ceiba.parqueadero.controller.VehiculoController;
import co.com.ceiba.parqueadero.persistence.ParqueoRepository;
import co.com.ceiba.parqueadero.persistence.VehiculoEntity;
import co.com.ceiba.parqueadero.persistence.VehiculoRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VehiculoControllerTest {
		
	@Autowired
	private VehiculoRepository vehiculoRepository;
	@Autowired
	private ParqueoRepository parqueoRepository;
	@Autowired
	private TestEntityManager entityManager;
	@BeforeEach
	public void setup(){
		parqueoRepository.deleteAll();
		vehiculoRepository.deleteAll();
		}
	
	@Test
	public void todosLosVehiculos() {
		//given
		VehiculoController vehiculoController=new VehiculoController(vehiculoRepository);
		//when
		int contador = 20;		
		while (contador > 0) {
			entityManager.persist(new VehiculoEntity());
			contador--;
		}		
		//then
		assertEquals(20, vehiculoController.todosLosVehiculos().size());		
	}
	
	@Test
	public void vehiculoPorId() {
		//given
		VehiculoController vehiculoController=new VehiculoController(vehiculoRepository);
		//when
		long idVehiculo = (long) entityManager.persistAndGetId(new VehiculoEntity());		
		//then
		assertNotNull(vehiculoController.vehiculoPorId(idVehiculo));
	}
	
	@Test
	public void vehiculoPorIdNotFound() {
		//given
		VehiculoController vehiculoController=new VehiculoController(vehiculoRepository);		
		//then
		assertEquals(HttpStatus.NOT_FOUND, vehiculoController.vehiculoPorId(0).getStatusCode());
	}
	
	@Test
	public void nuevoVehiculoCreado() {
		//given
		VehiculoController vehiculoController=new VehiculoController(vehiculoRepository);	
		VehiculoEntity vehiculoEntity=new VehiculoEntity(1000, "XBC", "CARRO");		
		//then
		assertEquals(HttpStatus.OK,vehiculoController.nuevoVehiculo(vehiculoEntity).getStatusCode());
	}	

}
