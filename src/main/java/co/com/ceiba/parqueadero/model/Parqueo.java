package co.com.ceiba.parqueadero.model;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import co.com.ceiba.parqueadero.persistence.ParqueoEntity;
import co.com.ceiba.parqueadero.persistence.ParqueoRepository;
import co.com.ceiba.parqueadero.persistence.VehiculoEntity;
import co.com.ceiba.parqueadero.persistence.VehiculoRepository;

public class Parqueo {
	private ParqueoRepository parqueoRepository;
	private VehiculoRepository vehiculoRepository;
	private Clock clock; 

	public Parqueo(ParqueoRepository parqueoRepository, VehiculoRepository vehiculoRepository) {
		this.parqueoRepository = parqueoRepository;
		this.vehiculoRepository = vehiculoRepository;	
		clock=Clock.system(Clock.systemDefaultZone().getZone() );
	}

	public String nuevoParqueo(ParqueoEntity parqueo) {			
			VehiculoEntity vehiculoEntity = vehiculoRepository.findByPlaca(parqueo.getVehiculoEntity().getPlaca());			
			if (vehiculoEntity==null) {
				VehiculoEntity aCrear=this.agregarVehiculo(parqueo);
				if(aCrear==null)
					return "ERROR AL INTENTAR GUARDAR EL VEHICULO";
				else
					vehiculoEntity=aCrear; 								
			}
			if (verificarPlacaYFechaVehiculo(vehiculoEntity)) 				
				return "EL VEHICULO NO PUEDE INGRESAR EL DIA DE HOY";
			if (verificarLimiteDeVehiculoNoEsExcedido(vehiculoEntity))
				return "EL LIMITE DE: " + vehiculoEntity.getTipoVehiculo() + " HA SIDO SUPERADO";				
			parqueo.setVehiculoEntity(vehiculoEntity);
			parqueo.setFechaHoraDeEntrada(LocalDateTime.now(clock));
			parqueoRepository.save(parqueo);				
			return "OK";				
	}
	
	private VehiculoEntity agregarVehiculo(ParqueoEntity parqueo) {
		Vehiculo vehiculo=new Vehiculo(vehiculoRepository);				
		VehiculoEntity agregar=new VehiculoEntity();
		agregar.setCilindraje(parqueo.getVehiculoEntity().getCilindraje());
		agregar.setPlaca(parqueo.getVehiculoEntity().getPlaca());
		agregar.setTipoVehiculo(parqueo.getVehiculoEntity().getTipoVehiculo());
		return vehiculo.nuevoVehiculo(agregar);		
	}

	private boolean verificarPlacaYFechaVehiculo(VehiculoEntity vehiculoEntity) {
		LocalDateTime fechaActual = LocalDateTime.now();
		return (vehiculoEntity.getPlaca().charAt(0) == 'A'
				&& (!"MONDAY".equals(fechaActual.getDayOfWeek().toString())|| !"SUNDAY".equals(fechaActual.getDayOfWeek().toString())));
	}

	private boolean verificarLimiteDeVehiculoNoEsExcedido(VehiculoEntity vehiculoEntity) {
		ParqueoVehiculoFactory costoParqueoFactory = new ParqueoVehiculoFactory();		
		return (vehiculoRepository.findAllByTipoVehiculo(vehiculoEntity.getTipoVehiculo()).size() >= costoParqueoFactory
				.costoParqueo(vehiculoEntity.getTipoVehiculo()).getMaximoVehiculosPermitidos());
	}

	public String salidaDeParqueo(ParqueoEntity parqueoEntity) {		
		if (parqueoEntity==null)
			return "EL PARQUEO INGRESADO NO EXISTE";
		else {						
			int valorAPagar = this.calcularValorAPagar(parqueoEntity);
			if(valorAPagar==-1)
				return "EL VEHICULO DEL PARQUEO NO SE PUDO ENCONTRAR";
			LocalDateTime fechaHoraDeSalida = LocalDateTime.now();
			parqueoEntity.setFechaHoraDeSalida(fechaHoraDeSalida);
			parqueoEntity.setValorAPagar(valorAPagar);
			parqueoRepository.save(parqueoEntity);
			return "OK";
		}
	}

	public int calcularValorAPagar(ParqueoEntity parqueoEntity) {
		Optional<VehiculoEntity> vehiculoEntity = vehiculoRepository.findById(parqueoEntity.getVehiculoEntity().getId());
		if (vehiculoEntity.isPresent()) {
			LocalDateTime fechaActual = LocalDateTime.now(clock);			
			double horasTotales = (ChronoUnit.SECONDS.between(parqueoEntity.getFechaHoraDeEntrada(), fechaActual)/3600.0);
			ParqueoVehiculoFactory costoParqueoFactory = new ParqueoVehiculoFactory();
			return costoParqueoFactory.costoParqueo(vehiculoEntity.get().getTipoVehiculo())
					.calcularValorAPagar((int) Math.ceil(horasTotales), vehiculoEntity.get());
		}
		return -1;
	}
	
	public ParqueoEntity buscarParqueoPorPlaca(String placaVehiculo){
		Vehiculo vehiculo=new Vehiculo(vehiculoRepository);
		VehiculoEntity buscado=vehiculo.buscarVehiculoPorPlaca(placaVehiculo);
		return parqueoRepository.findByVehiculoEntity(buscado);
		
	}		
	
	//TEST ONLY
	public void setClock(Clock clock) {
		this.clock=clock;
	}
	

}
