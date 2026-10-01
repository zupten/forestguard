package io.forestguard.api.service;

import java.time.OffsetDateTime;
import java.util.List;

import io.forestguard.api.dto.CrearMuestraRequest;
import io.forestguard.api.dto.MuestraResponse;
import io.forestguard.api.entity.MuestraMeteorologica;
import io.forestguard.api.repository.MuestraMeteorologicaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MuestraMeteorologicaService {

    private final MuestraMeteorologicaRepository repository;

    public MuestraMeteorologicaService(
            MuestraMeteorologicaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public MuestraResponse guardar(CrearMuestraRequest request) {
        MuestraMeteorologica muestra = new MuestraMeteorologica();

        muestra.setEstacion(request.estacion());
        muestra.setFecha(request.fecha());
        muestra.setTemperatura(request.temperatura());
        muestra.setOrigen(request.origen());

        MuestraMeteorologica guardada = repository.save(muestra);

        return convertirARespuesta(guardada);
    }

    @Transactional(readOnly = true)
    public List<MuestraResponse> consultarTodas() {
        return repository.findAll()
                .stream()
                .map(this::convertirARespuesta)
                .toList();
    }
    

	@Transactional
	public MuestraResponse guardarDesdeModelo(
	        OffsetDateTime instante, Double temperatura) {
	
	    MuestraMeteorologica muestra = new MuestraMeteorologica();
	    muestra.setEstacion("PUNTO-VALENCIA-39.47--0.38");
	    muestra.setFecha(instante.toLocalDate());
	    muestra.setInstante(instante);
	    muestra.setTemperatura(temperatura);
	    muestra.setOrigen("OPEN_METEO_MODELO");
	
	    return convertirARespuesta(repository.save(muestra));
	}

    private MuestraResponse convertirARespuesta(
            MuestraMeteorologica muestra) {
        return new MuestraResponse(
                muestra.getId(),
                muestra.getEstacion(),
                muestra.getFecha(),
                muestra.getTemperatura(),
                muestra.getOrigen(),
                muestra.getInstante()
        );
    }
}