package co.com.ceiba.parqueadero.unitarias;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import co.com.ceiba.parqueadero.model.ParqueoVehiculoCarro;
import co.com.ceiba.parqueadero.persistence.VehiculoEntity;

public class ParqueoVehiculoCarroTest {
	
	@Test
	void calcularValorAPagar() {
		// given
		ParqueoVehiculoCarro parqueoVehiculoCarroHoras=new ParqueoVehiculoCarro();
		ParqueoVehiculoCarro parqueoVehiculoCarroDia=new ParqueoVehiculoCarro();
		ParqueoVehiculoCarro parqueoVehiculoCarroMasDelDia=new ParqueoVehiculoCarro();
		// when
		VehiculoEntity vehiculoEntity=new VehiculoEntity(1000, "ABC", "CARRO");
		// then
		assertEquals(3000, parqueoVehiculoCarroHoras.calcularValorAPagar(3, vehiculoEntity));
		assertEquals(8000, parqueoVehiculoCarroDia.calcularValorAPagar(10, vehiculoEntity));
		assertEquals(11000, parqueoVehiculoCarroMasDelDia.calcularValorAPagar(27, vehiculoEntity));
	}

}
