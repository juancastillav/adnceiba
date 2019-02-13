package co.com.ceiba.parqueadero.unitarias;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import co.com.ceiba.parqueadero.model.Parqueo;
import co.com.ceiba.parqueadero.model.Vehiculo;
import co.com.ceiba.parqueadero.persistence.ParqueoEntity;
import co.com.ceiba.parqueadero.persistence.ParqueoRepository;
import co.com.ceiba.parqueadero.persistence.VehiculoEntity;
import co.com.ceiba.parqueadero.persistence.VehiculoRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ParqueoTest {

	@Autowired
	private ParqueoRepository parqueoRepository;
	@Autowired
	private VehiculoRepository vehiculoRepository;
	@Autowired
	private TestEntityManager entityManager;
	@MockBean
	private Vehiculo vehiculoMock;

	@BeforeEach
	public void init() {
		vehiculoRepository.deleteAll();
		parqueoRepository.deleteAll();
	}

	@Test
	public void nuevoParqueoExitoso() {
		// given
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);
		// when
		ParqueoEntity parqueoEntity = new ParqueoEntity(entityManager.persist(new VehiculoEntity(1000, "XBC", "CARRO")),
				LocalDateTime.of(2019, Month.JANUARY, 5, 06, 24, 00));
		// then
		assertEquals("OK", parqueo.nuevoParqueo(parqueoEntity));
	}

	@Test
	public void agregarVehiculoCorrecto() {

		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);
		VehiculoEntity agregar = new VehiculoEntity(1000, "ABC", "CARRO");
		ParqueoEntity parqueoEntity = new ParqueoEntity(agregar, LocalDateTime.of(2019, Month.JANUARY, 5, 06, 24, 00));
		Mockito.when(vehiculoMock.nuevoVehiculo(agregar)).thenReturn(agregar);
		assertEquals(parqueo.agregarVehiculo(parqueoEntity).getPlaca(), agregar.getPlaca());
	}

	@Test
	public void nuevoParqueoVehiculoNoPuedeIngresarPorPlacaYDia() {
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);
		ParqueoEntity parqueoEntity = new ParqueoEntity(entityManager.persist(new VehiculoEntity(1000, "ABC", "CARRO")),
				LocalDateTime.of(2019, Month.JANUARY, 5, 06, 24, 00));
		assertEquals("EL VEHICULO NO PUEDE INGRESAR EL DIA DE HOY", parqueo.nuevoParqueo(parqueoEntity));
	}

	@Test
	public void nuevoParqueoVehiculoSuperaCantidadMaximaVehiculos() {
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);
		ParqueoEntity parqueoEntity = new ParqueoEntity(entityManager.persist(new VehiculoEntity(1000, "XXX", "CARRO")),
				LocalDateTime.of(2019, Month.JANUARY, 5, 06, 24, 00));
		int contador = 20;
		while (contador > 0) {
			entityManager.persist(new VehiculoEntity(1000, "GGG", "CARRO"));
			contador--;
		}

		assertEquals("EL LIMITE DE: CARRO HA SIDO SUPERADO", parqueo.nuevoParqueo(parqueoEntity));
	}

	@Test
	public void salidaDeParqueo() {
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);
		VehiculoEntity vehiculoEntity = entityManager.persist(new VehiculoEntity(1000, "ABC", "CARRO"));
		ParqueoEntity parqueoEntity = entityManager
				.persist(new ParqueoEntity(vehiculoEntity, LocalDateTime.of(2019, Month.JANUARY, 5, 06, 24, 00)));
		assertEquals("OK", parqueo.salidaDeParqueo(parqueoEntity));

	}

	@Test
	public void calcularValorAPagar() {
		// arrange
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);

		ParqueoEntity parqueoEntityVehiculoPorHoras = new ParqueoEntity(
				entityManager.persist(new VehiculoEntity(1000, "ABC", "CARRO")),
				LocalDateTime.of(2019, Month.JANUARY, 5, 06, 00, 00));
		ParqueoEntity parqueoEntityVehiculoPorDia = new ParqueoEntity(
				entityManager.persist(new VehiculoEntity(1000, "EFG", "CARRO")),
				LocalDateTime.of(2019, Month.JANUARY, 5, 06, 00, 00));
		ParqueoEntity parqueoEntityVehiculoMasDelDia = new ParqueoEntity(
				entityManager.persist(new VehiculoEntity(1000, "EFG", "CARRO")),
				LocalDateTime.of(2019, Month.JANUARY, 5, 06, 00, 00));
		ParqueoEntity parqueoEntityMotoBajoCilindraje = new ParqueoEntity(
				entityManager.persist(new VehiculoEntity(250, "EFG", "MOTO")),
				LocalDateTime.of(2019, Month.JANUARY, 5, 06, 00, 00));
		ParqueoEntity parqueoEntityMotoAltoCilindraje = new ParqueoEntity(
				entityManager.persist(new VehiculoEntity(1000, "EFG", "MOTO")),
				LocalDateTime.of(2019, Month.JANUARY, 5, 06, 00, 00));

		// assert
		parqueo.setClock(Clock.fixed(Instant.parse("2019-01-05T14:00:00.00Z"), ZoneId.systemDefault()));
		assertEquals(3000, parqueo.calcularValorAPagar(parqueoEntityVehiculoPorHoras));

		parqueo.setClock(Clock.fixed(Instant.parse("2019-01-05T21:00:00.00Z"), ZoneId.systemDefault()));
		assertEquals(8000, parqueo.calcularValorAPagar(parqueoEntityVehiculoPorDia));

		parqueo.setClock(Clock.fixed(Instant.parse("2019-01-06T13:00:00.00Z"), ZoneId.systemDefault()));
		assertEquals(10000, parqueo.calcularValorAPagar(parqueoEntityVehiculoMasDelDia));

		parqueo.setClock(Clock.fixed(Instant.parse("2019-01-05T14:00:00.00Z"), ZoneId.systemDefault()));
		assertEquals(1500, parqueo.calcularValorAPagar(parqueoEntityMotoBajoCilindraje));

		parqueo.setClock(Clock.fixed(Instant.parse("2019-01-05T14:00:00.00Z"), ZoneId.systemDefault()));
		assertEquals(3500, parqueo.calcularValorAPagar(parqueoEntityMotoAltoCilindraje));

	}

	@Test
	public void calcularValorAPagarVehiculoPorHoras() {
		// arrange
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);
		ParqueoEntity parqueoEntityVehiculoPorHoras = new ParqueoEntity(
		entityManager.persist(new VehiculoEntity(1000, "ABC", "CARRO")),
		LocalDateTime.of(2019, Month.JANUARY, 5, 06, 00, 00));

		// assert
		parqueo.setClock(Clock.fixed(Instant.parse("2019-01-05T14:00:00.00Z"), ZoneId.systemDefault()));
		assertEquals(3000, parqueo.calcularValorAPagar(parqueoEntityVehiculoPorHoras));

	}

	@Test
	public void calcularValorAPagarVehiculoPorDia() {
		// arrange
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);

		ParqueoEntity parqueoEntityVehiculoPorDia = new ParqueoEntity(
		entityManager.persist(new VehiculoEntity(1000, "EFG", "CARRO")),
		LocalDateTime.of(2019, Month.JANUARY, 5, 06, 00, 00));

		// assert

		parqueo.setClock(Clock.fixed(Instant.parse("2019-01-05T21:00:00.00Z"), ZoneId.systemDefault()));
		assertEquals(8000, parqueo.calcularValorAPagar(parqueoEntityVehiculoPorDia));
	}

	@Test
	public void calcularValorAPagarVehiculoPorMasDelDia() {
		// arrange
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);
				
		ParqueoEntity parqueoEntityVehiculoMasDelDia=new ParqueoEntity(entityManager.persist(new VehiculoEntity(1000, "EFG", "CARRO")),LocalDateTime.of(2019, Month.JANUARY, 5, 06, 00, 00));	
				
		// assert				
		parqueo.setClock(Clock.fixed(Instant.parse("2019-01-06T13:00:00.00Z"),
						ZoneId.systemDefault()));
		assertEquals(10000, parqueo.calcularValorAPagar(parqueoEntityVehiculoMasDelDia));
	}

	@Test
	public void calcularValorAPagarVehiculoPorMotoBajoCilindraje() {
		// arrange
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);

		ParqueoEntity parqueoEntityMotoBajoCilindraje = new ParqueoEntity(
				entityManager.persist(new VehiculoEntity(250, "EFG", "MOTO")),
				LocalDateTime.of(2019, Month.JANUARY, 5, 06, 00, 00));
		// assert
		parqueo.setClock(Clock.fixed(Instant.parse("2019-01-05T14:00:00.00Z"), ZoneId.systemDefault()));
		assertEquals(1500, parqueo.calcularValorAPagar(parqueoEntityMotoBajoCilindraje));
	}

	@Test
	public void calcularValorAPagarVehiculoPorMotoAltoCilindraje() {
		// arrange
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);

		ParqueoEntity parqueoEntityMotoAltoCilindraje = new ParqueoEntity(
				entityManager.persist(new VehiculoEntity(1000, "EFG", "MOTO")),
				LocalDateTime.of(2019, Month.JANUARY, 5, 06, 00, 00));

		// assert
		parqueo.setClock(Clock.fixed(Instant.parse("2019-01-05T14:00:00.00Z"), ZoneId.systemDefault()));
		assertEquals(3500, parqueo.calcularValorAPagar(parqueoEntityMotoAltoCilindraje));
	}

}
