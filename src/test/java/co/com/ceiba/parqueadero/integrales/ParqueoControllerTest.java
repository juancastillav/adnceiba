package co.com.ceiba.parqueadero.integrales;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.GregorianCalendar;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import co.com.ceiba.parqueadero.controller.ParqueoController;
import co.com.ceiba.parqueadero.model.Parqueo;
import co.com.ceiba.parqueadero.persistence.ParqueoEntity;
import co.com.ceiba.parqueadero.persistence.ParqueoRepository;
import co.com.ceiba.parqueadero.persistence.VehiculoEntity;
import co.com.ceiba.parqueadero.persistence.VehiculoRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ParqueoControllerTest {

	@Autowired
	private ParqueoRepository parqueoRepository;
	@Autowired
	private VehiculoRepository vehiculoRepository;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void todosLosParqueos() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		int contador = 19;
		while (contador > 0) {
			entityManager.persist(new ParqueoEntity());
			contador--;
		}
		assertEquals(20, parqueoController.todosLosParqueos().size());
	}

	@Test
	public void parqueoPorId() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		long idParqueo = (long) entityManager.persistAndGetId(new ParqueoEntity());
		assertNotNull(parqueoController.parqueoPorId("" + idParqueo));
	}

	@Test
	public void parqueoPorIdNotFound() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		assertEquals("404 NOT_FOUND", parqueoController.parqueoPorId("890").getStatusCode().toString());
	}

	@Test
	public void nuevoParqueo() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);

		long idGeneradoVehiculoCorrecto = (long) entityManager
				.persistAndGetId(new VehiculoEntity(1000, "XBC", "CARRO"));

		ParqueoEntity parqueoCorrecto = new ParqueoEntity(idGeneradoVehiculoCorrecto,
				new GregorianCalendar(2019, 1, 5, 6, 24, 00));

		assertEquals("PARQUEO CREADO CON EXITO", parqueoController.nuevoParqueo(parqueoCorrecto).getBody());

	}

	@Test
	public void nuevoParqueoVehiculoNoPuedeIngresarPorDia() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		long idGeneradoVehiculoNoPuedeIngresarDia = (long) entityManager
				.persistAndGetId(new VehiculoEntity(1000, "ABC", "CARRO"));
		ParqueoEntity parqueoNoPuedeIngresarDia = new ParqueoEntity(idGeneradoVehiculoNoPuedeIngresarDia,
				new GregorianCalendar(2019, 1, 5, 6, 24, 00));
		assertEquals("EL VEHICULO NO PUEDE INGRESAR EL DIA DE HOY",
				parqueoController.nuevoParqueo(parqueoNoPuedeIngresarDia).getBody());
	}

	@Test
	public void nuevoParqueoVehiculoLimiteSuperado() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);

		int contador = 20;
		long idGeneradoVehiculoCorrecto = (long) entityManager
				.persistAndGetId(new VehiculoEntity(1000, "XBC", "CARRO"));

		ParqueoEntity parqueoCorrecto = new ParqueoEntity(idGeneradoVehiculoCorrecto,
				new GregorianCalendar(2019, 1, 5, 6, 24, 00));
		while (contador > 0) {
			entityManager.persist(new VehiculoEntity(1000, "XXX", "CARRO"));
			contador--;
		}
		assertEquals("EL LIMITE DE: CARRO HA SIDO SUPERADO", parqueoController.nuevoParqueo(parqueoCorrecto).getBody());
	}

	@Test
	public void nuevoParqueoVehiculoNoExiste() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		ParqueoEntity parqueoCorrecto = new ParqueoEntity(-1, new GregorianCalendar(2019, 1, 5, 6, 24, 00));
		assertEquals("EL VEHICULO NO EXISTE", parqueoController.nuevoParqueo(parqueoCorrecto).getBody());
	}

	@Test
	public void salidaDeParqueoExitosa() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);
		long idGeneradoVehiculo = (long) entityManager.persistAndGetId(new VehiculoEntity(1000, "XBC", "CARRO"));
		long idGeneradoParqueo = (long) entityManager
				.persistAndGetId(new ParqueoEntity(idGeneradoVehiculo, new GregorianCalendar(2019, 1, 5, 6, 24, 00)));
		parqueo.setCalendar(new GregorianCalendar(2019, 1, 5, 9, 24, 00));
		parqueoController.salidaDeParqueo(idGeneradoParqueo);
		assertEquals("SALIDA CREADA CON EXITO", parqueoController.salidaDeParqueo(idGeneradoParqueo).getBody());

	}

	@Test
	public void salidaDeParqueoNoExiste() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		assertEquals("EL PARQUEO INGRESADO NO EXISTE", parqueoController.salidaDeParqueo((long) -1).getBody());
	}

	@Test
	public void salidaDeParqueoVehiculoNoExiste() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		long idGeneradoParqueo = (long) entityManager
				.persistAndGetId(new ParqueoEntity(-1, new GregorianCalendar(2019, 1, 5, 6, 24, 00)));
		assertEquals("EL VEHICULO DEL PARQUEO NO SE PUDO ENCONTRAR",
				parqueoController.salidaDeParqueo(idGeneradoParqueo).getBody());
	}

}
