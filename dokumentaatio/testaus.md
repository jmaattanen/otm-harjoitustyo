# Testausdokumentti

Sovellus on ollut koek�yt�ss� Windows- ja Linux-k�ytt�j�rjestelmiss� ja sit� on testattu JUnitin automatisoiduilla testeill�.
Testauksen p��paino on ollut integraatiotesteiss�, mutta yksikk�testej� on my�s tehty v�ltt�v�sti. K�ytt�liittym�� ei ole testattu JUnitilla.

## JUnit-testit

### Sovelluslogiikka

Suurin osa testeist� keskittyy kolmen toiminnallisimman luokan testaukseen: _MainController_, _(Detailed)TextBuilder_ ja _Classifier_.
_AudioPlayer_-luokkaa ei testata oikeiden ��niresurssien kanssa, mik� vuoksi luokan testikattavuus on alhainen. Kokonaisuudessaan Logics-pakkausta on testattu kohtuullisen kattavasti ja toiminnallisuuden virheit� ehk�isev�sti.
Suurimmat puutteet testauksen kattavuudessa kohdistuvat tilanteisiin, joihin p��tyminen on hyvin ep�todenn�k�ist�.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/test_logics.png "Logics tests")

### DAO

Pysyv�istallennuksen testaus mukailee t�ysin realistista skenaariota tallennuksesta. Testeiss� tiedot tallennetaan v�liaikaiseen tiedostoon ja tietokantaan soveltaen JUnitin TemporaryFolder-s��nt�j�.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/test_dao.png "DAO tests")

### Yleisesti

Rivikattavuus on yleisesti ottaen varsin hyv� ja kattaa nykyisell��n ohjelman logiikan perustoiminnallisuudet. Haarautumakattavuus sen sijaan on jonkin verran alhaisempi, sill� l�hdekoodi sis�lt�� haarautumia, joihin p��tyminen voi olla jopa mahdotonta ohjelmakoodin nykyisess� muodossa. Jatkokehityksen kannalta n�m� haarautumat on silti syyt� olla olemassa, jotta tulevaisuudessa ei p��dyt� niin herk�sti ei-toivottuihin virhetilanteisiin.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/test_tb.png "TB tests")