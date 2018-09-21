package com.grove.microservices.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {
	
	@Autowired
	private CurrencyExchangeServiceProxy proxy;  //Feign proxy
	
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		
		//Lookup the conversion factor (conversionMultiple) from the currency-exchange-service using a RestTemplate:
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class, uriVariables);
		CurrencyConversionBean response = responseEntity.getBody(); 
		BigDecimal conversionMultiple = response.getConversionMultiple();
		BigDecimal totalCalculatedAmount = quantity.multiply(conversionMultiple);
				
		return new CurrencyConversionBean(response.getId(), response.getFrom(), response.getTo(), 
				conversionMultiple, quantity, totalCalculatedAmount , response.getPort());
	}
	
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		
		//Lookup the conversion factor (conversionMultiple) from the currency-exchange-service using the feign proxy:
		CurrencyConversionBean response = proxy.retrieveExchangeValue(from, to);
		BigDecimal conversionMultiple = response.getConversionMultiple();
		BigDecimal totalCalculatedAmount = quantity.multiply(conversionMultiple);
				
		return new CurrencyConversionBean(response.getId(), response.getFrom(), response.getTo(), 
				conversionMultiple, quantity, totalCalculatedAmount , response.getPort());
	}	
}
