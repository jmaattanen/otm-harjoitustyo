package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class KassapaateTest {
    
    Kassapaate kassapaate;
    Maksukortti kortti;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        kassapaate = new Kassapaate();
        kortti = new Maksukortti(2000);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void luotuKassapaateOlemassa() {
        assertTrue(kassapaate!=null);
    }
    
    @Test
    public void luodussaKassapaatteessaOikeaMaaraRahaa() {
        assertEquals(100000,kassapaate.kassassaRahaa());
    }
    
    @Test
    public void luodussaKassapaatteessaEiOleMyytyjaLounaita() {
        int lounaitaMyyty = 0;
        lounaitaMyyty += kassapaate.edullisiaLounaitaMyyty();
        lounaitaMyyty += kassapaate.maukkaitaLounaitaMyyty();
        assertEquals(0,lounaitaMyyty);
    }
    
    @Test
    public void edullistenLounaidenMaaraKasvaa() {
        kassapaate.syoEdullisesti(500);
        kassapaate.syoEdullisesti(1000);
        kassapaate.syoEdullisesti(5000);
        assertEquals(3,kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void edullisestiSyominenPalauttaaRahatOikein() {
        int palautus = kassapaate.syoEdullisesti(500);
        assertEquals(260,palautus);
    }
    
    @Test
    public void maukkaidenLounaidenMaaraKasvaa() {
        kassapaate.syoMaukkaasti(500);
        kassapaate.syoMaukkaasti(1000);
        kassapaate.syoMaukkaasti(5000);
        assertEquals(3,kassapaate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void maukkaastiSyominenPalauttaaRahatOikein() {
        int palautus = kassapaate.syoMaukkaasti(500);
        assertEquals(100,palautus);
    }
    
    @Test
    public void riittamatonMaksuPalauttaaRahat() {
        int palautus = kassapaate.syoMaukkaasti(200);
        assertEquals(200,palautus);
    }
    
    @Test
    public void riittamatonMaksuEiLisaaMyytyjenMaaraa() {
        kassapaate.syoEdullisesti(200);
        assertEquals(0,kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void riittamatonMaksuEiLisaaKassaanRahaa() {
        kassapaate.syoEdullisesti(200);
        assertEquals(100000,kassapaate.kassassaRahaa());
    }
    
    @Test
    public void kassapaatteellaVoiSyodaEdullisestiKortilla() {
        for(int i = 0; i < 3; i++)
            kassapaate.syoEdullisesti(kortti);
        assertEquals(3,kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void kassapaateVeloittaaEdullisenMaksunKortilta() {
        kassapaate.syoEdullisesti(kortti);
        assertEquals(1760,kortti.saldo());
    }
    
    @Test
    public void kassapaateVeloittaaMaukkaanMaksunKortilta() {
        kassapaate.syoMaukkaasti(kortti);
        assertEquals(1600,kortti.saldo());
    }
    
    @Test
    public void korttimaksuKasvattaaMyytyjenEdullistenMaaraa() {
        for(int i = 0; i < 3; i++)
            kassapaate.syoEdullisesti(kortti);
        assertEquals(3,kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void korttimaksuKasvattaaMyytyjenMaukkaidenMaaraa() {
        for(int i = 0; i < 3; i++)
            kassapaate.syoMaukkaasti(kortti);
        assertEquals(3,kassapaate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void kortinSaldonLoputtuaMyytyjenEdullistenMaaraEiKasva() {
        for(int i = 0; i < 10; i++)
            kassapaate.syoEdullisesti(kortti);
        assertEquals(8,kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void kortiltaEiVeloitetaRiittamatontaEdullistaMaksua() {
        for(int i = 0; i < 10; i++)
            kassapaate.syoEdullisesti(kortti);
        assertEquals(80,kortti.saldo());
    }
    
    @Test
    public void kortiltaEiVeloitetaRiittamatontaMaukastaMaksua() {
        for(int i = 0; i < 10; i++)
            kassapaate.syoMaukkaasti(kortti);
        assertEquals(0,kortti.saldo());
    }
    
    @Test
    public void kortinSaldonLoputtuaMyytyjenMaukkaidenMaaraEiKasva() {
        for(int i = 0; i < 10; i++)
            kassapaate.syoMaukkaasti(kortti);
        assertEquals(5,kassapaate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void kassanRahamaaraEiMuutuKorttillaMaksaessa() {
        kassapaate.syoMaukkaasti(kortti);
        assertEquals(100000,kassapaate.kassassaRahaa());
    }
    
    @Test
    public void kortinLatausKasvattaaKortinSaldoa() {
        kassapaate.lataaRahaaKortille(kortti, 1000);
        assertEquals(3000,kortti.saldo());
    }
    
    @Test
    public void kortinLatausKasvattaaKassanRahamaaraa() {
        kassapaate.lataaRahaaKortille(kortti, 1000);
        assertEquals(101000,kassapaate.kassassaRahaa());
    }
    
    @Test
    public void kortinLatausEiToimiNegatiivisellaSummalla() {
        kassapaate.lataaRahaaKortille(kortti, -1000);
        assertEquals(2000,kortti.saldo());
    }
}
