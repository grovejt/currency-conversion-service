package com.grove.microservices.currencyconversionservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Hard coded location:
//@FeignClient(name="currency-exchange-service", url="localhost:8000")

// Instead of connecting to naming server connect to zuul:
//@FeignClient(name="currency-exchange-service")
@FeignClient(name="netflix-zuul-api-gateway-server")

@RibbonClient(name="currency-exchange-service")
public interface CurrencyExchangeServiceProxy {
	
	//@GetMapping("/currency-exchange/from/{from}/to/{to}")
	// Add in zule service name to uri since we want to go through zuul instead of the naming server:
	@GetMapping("/currency-exchange-service/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);

}
