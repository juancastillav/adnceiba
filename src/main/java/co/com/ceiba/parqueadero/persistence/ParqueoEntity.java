package co.com.ceiba.parqueadero.persistence;


import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="PARQUEO")
public class ParqueoEntity {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private long id;	
	@ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="vehiculo_Id")	
	private VehiculoEntity vehiculoEntity;
	@Column(name = "FECHA_HORA_ENTRADA")
	private LocalDateTime  fechaHoraDeEntrada;
	@Column(name = "FECHA_HORA_SALIDA")
	private LocalDateTime fechaHoraDeSalida;
	@Column(name = "VALOR_PAGAR")
	private int valorAPagar;
	
	public ParqueoEntity() {}
	public ParqueoEntity (VehiculoEntity vehiculoEntity,LocalDateTime fechaHoraDeEntrada) {
		this.vehiculoEntity=vehiculoEntity;
		this.fechaHoraDeEntrada=fechaHoraDeEntrada;		
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public VehiculoEntity getVehiculoEntity() {
		return vehiculoEntity;
	}

	public void setVehiculoEntity(VehiculoEntity vehiculoId) {
		this.vehiculoEntity = vehiculoId;
	}

	public LocalDateTime getFechaHoraDeEntrada() {
		return fechaHoraDeEntrada;
	}

	public void setFechaHoraDeEntrada(LocalDateTime fechaHoraDeEntrada) {
		this.fechaHoraDeEntrada = fechaHoraDeEntrada;
	}

	public LocalDateTime getFechaHoraDeSalida() {
		return fechaHoraDeSalida;
	}

	public void setFechaHoraDeSalida(LocalDateTime fechaHoraDeSalida2) {
		this.fechaHoraDeSalida = fechaHoraDeSalida2;
	}

	public int getValorAPagar() {
		return valorAPagar;
	}

	public void setValorAPagar(int valorAPagar) {
		this.valorAPagar = valorAPagar;
	}
}
