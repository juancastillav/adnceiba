package co.com.ceiba.parqueadero.persistence;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PARQUEO")
public class ParqueoEntity {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private long id;	
	@Column(name="vehiculo_Id")
	private long vehiculoId;
	@Column(name = "FECHA_HORA_ENTRADA")
	private Calendar fechaHoraDeEntrada;
	@Column(name = "FECHA_HORA_SALIDA")
	private Calendar fechaHoraDeSalida;
	@Column(name = "VALOR_PAGAR")
	private int valorAPagar;
	public ParqueoEntity() {}
	public ParqueoEntity (long vehiculoId,Calendar fechaHoraDeEntrada) {
		this.vehiculoId=vehiculoId;
		this.fechaHoraDeEntrada=fechaHoraDeEntrada;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getVehiculoId() {
		return vehiculoId;
	}

	public void setVehiculo(Long vehiculoId) {
		this.vehiculoId = vehiculoId;
	}

	public Calendar getFechaHoraDeEntrada() {
		return fechaHoraDeEntrada;
	}

	public void setFechaHoraDeEntrada(Calendar fechaHoraDeEntrada) {
		this.fechaHoraDeEntrada = fechaHoraDeEntrada;
	}

	public Calendar getFechaHoraDeSalida() {
		return fechaHoraDeSalida;
	}

	public void setFechaHoraDeSalida(Calendar fechaHoraDeSalida2) {
		this.fechaHoraDeSalida = fechaHoraDeSalida2;
	}

	public int getValorAPagar() {
		return valorAPagar;
	}

	public void setValorAPagar(int valorAPagar) {
		this.valorAPagar = valorAPagar;
	}
	
	

}
