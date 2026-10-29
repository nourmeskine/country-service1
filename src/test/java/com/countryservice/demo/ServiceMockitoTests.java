package com.countryservice.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.countryservice.demo.beans.Country;
import com.countryservice.demo.repositories.CountryRepository;
import com.countryservice.demo.services.CountryService;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceMockitoTests {

    @Mock
    CountryRepository countryrep;

    @InjectMocks
    CountryService countryService;

    // ---------- getAllCountries ----------
    @Test
    @Order(1)
    public void test_getAllCountries() {
        List<Country> mycountries = new ArrayList<>();
        mycountries.add(new Country(1, "India", "Delhi"));
        mycountries.add(new Country(2, "USA", "Washington"));

        when(countryrep.findAll()).thenReturn(mycountries); // Mocking

        assertEquals(2, countryService.getAllCountries().size());
    }

    // ---------- getCountryById ----------
    @Test
    @Order(2)
    public void test_getCountryByID() {
        int countryID = 1;
        Country country = new Country(1, "India", "Delhi");

        when(countryrep.findById(countryID)).thenReturn(Optional.of(country)); // Mocking

        assertEquals(countryID, countryService.getCountryById(countryID).getIdCountry());
    }

    @Test
    @Order(3)
    public void test_getCountryByID_notFound() {
        when(countryrep.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> countryService.getCountryById(99));
    }

    // ---------- getCountryByName ----------
    @Test
    @Order(4)
    public void test_getCountryByName() {
        List<Country> mycountries = new ArrayList<>();
        mycountries.add(new Country(1, "India", "Delhi"));
        mycountries.add(new Country(2, "USA", "Washington"));

        when(countryrep.findAll()).thenReturn(mycountries); // Mocking

        assertEquals("USA", countryService.getCountryByName("USA").getName());
    }

    @Test
    @Order(5)
    public void test_getCountryByName_ignoreCase() {
        List<Country> mycountries = new ArrayList<>();
        mycountries.add(new Country(1, "India", "Delhi"));

        when(countryrep.findAll()).thenReturn(mycountries);

        assertEquals("India", countryService.getCountryByName("iNdIa").getName());
    }

    @Test
    @Order(6)
    public void test_getCountryByName_notFound() {
        when(countryrep.findAll()).thenReturn(new ArrayList<>());

        assertThrows(NoSuchElementException.class,
                () -> countryService.getCountryByName("Unknown"));
    }

    // ---------- addCountry ----------
    @Test
    @Order(7)
    public void test_addCountry() {
        Country country = new Country(3, "France", "Paris");

        when(countryrep.findAll()).thenReturn(new ArrayList<>()); // aucun pays existant
        when(countryrep.save(country)).thenReturn(country);       // Mocking

        assertEquals(country, countryService.addCountry(country));
        verify(countryrep, times(1)).save(country);
    }

    @Test
    @Order(8)
    public void test_addCountry_alreadyExists() {
        List<Country> existing = new ArrayList<>();
        existing.add(new Country(1, "India", "Delhi"));

        when(countryrep.findAll()).thenReturn(existing);

        Country duplicate = new Country(5, "india", "New Delhi");

        assertThrows(IllegalArgumentException.class,
                () -> countryService.addCountry(duplicate));
        verify(countryrep, never()).save(duplicate); // save ne doit jamais être appelé
    }

    // ---------- updateCountry ----------
    @Test
    @Order(9)
    public void test_updateCountry() {
        Country country = new Country(3, "Germany", "Berlin");

        when(countryrep.save(country)).thenReturn(country); // Mocking

        assertEquals(country, countryService.updateCountry(country));
    }

    // ---------- deleteCountry ----------
    @Test
    @Order(10)
    public void test_deleteCountry() {
        Country country = new Country(3, "Germany", "Berlin");

        countryService.deleteCountry(country);

        verify(countryrep, times(1)).delete(country);
    }
}