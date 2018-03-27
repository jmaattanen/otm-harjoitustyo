package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void konstruktoriAsettaaSaldonOikein() {
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void lataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(10);
        assertEquals(20, kortti.saldo());
    }
    
    @Test
    public void saldoaVoiVahentaa() {
        kortti.otaRahaa(5);
        assertEquals(5, kortti.saldo());
    }
    
    @Test
    public void rahaaEiVoiOttaaYliSaldonVerran() {
        kortti.otaRahaa(20);
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void onnistunutRahanOttaminenPalauttaaTrue() {
        boolean otto = kortti.otaRahaa(10);
        assertEquals(true, otto);
    }
}
