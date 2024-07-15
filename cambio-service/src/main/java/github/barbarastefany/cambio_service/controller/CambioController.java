package github.barbarastefany.cambio_service.controller;

import github.barbarastefany.cambio_service.model.Cambio;
import github.barbarastefany.cambio_service.repository.CambioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/cambio-service")
public class CambioController {

    @Autowired
    private Environment environment;

    @Autowired
    private CambioRepository cambioRepository;

    @GetMapping(value= "/cambio-service/{amount}/{from}/{to}")
    public Cambio getCambio(
            @PathVariable("amount")BigDecimal amount,
            @PathVariable("from") String from,
            @PathVariable("to") String to
            ) {

            Cambio cambio = cambioRepository.findByFromAndTo(from, to)
                    .orElseThrow(() -> new RuntimeException("currency unsupported"));

            String PORT = environment.getProperty("server.port");
            BigDecimal conversionFactor = cambio.getConversionFactor();
            BigDecimal convertedValue = conversionFactor.multiply(amount);
            cambio.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING));
            cambio.setEnvironment(PORT);

        return cambio;
    }
}
