package co.com.ceiba.parqueadero.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VEHICULOS")
public class VehiculoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long id;
	@Column(name = "CILINDRAJE")
	private int cilindraje;
	@Column(name = "PLACA")
	private String placa;
	@Column(name = "TIPO_VEHICULO")
	private String tipoVehiculo;

	public VehiculoEntity() {
	}

	public VehiculoEntity(int cilindraje, String placa, String tipoVehiculo) {
		this.cilindraje = cilindraje;
		this.placa = placa;
		this.tipoVehiculo = tipoVehiculo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCilindraje() {
		return cilindraje;
	}

	public void setCilindraje(int cilindraje) {
		this.cilindraje = cilindraje;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

}
