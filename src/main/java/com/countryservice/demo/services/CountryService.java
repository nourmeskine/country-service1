package com.countryservice.demo.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.countryservice.demo.beans.Country;
import com.countryservice.demo.repositories.CountryRepository;

@Service
public class CountryService {

	private final CountryRepository countryRep;

	public CountryService(CountryRepository countryRep) {
		this.countryRep = countryRep;
	}

	public List<Country> getAllCountries() {
		return countryRep.findAll();
	}

	public Country getCountryById(int id) {
		return countryRep.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Country not found with id " + id));
	}

	public Country getCountryByName(String name) {
		return countryRep.findAll().stream()
				.filter(c -> c.getName() != null && c.getName().equalsIgnoreCase(name))
				.findFirst()
				.orElseThrow(() -> new NoSuchElementException("Country not found with name " + name));
	}

	public Country addCountry(Country country) {
		boolean exists = countryRep.findAll().stream()
				.anyMatch(c -> c.getName() != null && c.getName().equalsIgnoreCase(country.getName()));
		if (exists) {
			throw new IllegalArgumentException("Country already exists: " + country.getName());
		}
		return countryRep.save(country);
	}

	public Country updateCountry(Country country) {
		return countryRep.save(country);
	}

	public void deleteCountry(Country country) {
		countryRep.delete(country);
	}
}