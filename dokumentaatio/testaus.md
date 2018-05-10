# Testausdokumentti

Sovellus on ollut koekäytössä Windows- ja Linux-käyttöjärjestelmissä ja sitä on testattu JUnitin automatisoiduilla testeillä.
Testauksen pääpaino on ollut integraatiotesteissä, mutta yksikkötestejä on myös tehty välttävästi. Käyttöliittymää ei ole testattu JUnitilla.

## JUnit-testit

### Sovelluslogiikka

Suurin osa testeistä keskittyy kolmen toiminnallisimman luokan testaukseen: _MainController_, _(Detailed)TextBuilder_ ja _Classifier_.
_AudioPlayer_-luokkaa ei testata oikeiden ääniresurssien kanssa, mikä vuoksi luokan testikattavuus on alhainen. Kokonaisuudessaan Logics-pakkausta on testattu kohtuullisen kattavasti ja toiminnallisuuden virheitä ehkäisevästi.
Suurimmat puutteet testauksen kattavuudessa kohdistuvat tilanteisiin, joihin päätyminen on hyvin epätodennäköistä.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/test_logics.png "Logics tests")

### DAO

Pysyväistallennuksen testaus mukailee täysin realistista skenaariota tallennuksesta. Testeissä tiedot tallennetaan väliaikaiseen tiedostoon ja tietokantaan soveltaen JUnitin TemporaryFolder-sääntöjä.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/test_dao.png "DAO tests")

### Yleisesti

Rivikattavuus on yleisesti ottaen varsin hyvä ja kattaa nykyisellään ohjelman logiikan perustoiminnallisuudet. Haarautumakattavuus sen sijaan on jonkin verran alhaisempi, sillä lähdekoodi sisältää haarautumia, joihin päätyminen voi olla jopa mahdotonta ohjelmakoodin nykyisessä muodossa. Jatkokehityksen kannalta nämä haarautumat on silti syytä olla olemassa, jotta tulevaisuudessa ei päädytä niin herkästi ei-toivottuihin virhetilanteisiin.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/test_tb.png "TB tests")