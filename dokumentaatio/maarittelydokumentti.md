# Määrittelydokumentti

Tämä on harjoitustyön *alustava* määrittelydokumentti.

## Käyttötarkoitus

Sovellus toimii litteroinnin apuvälineenä. Sovelluksella voidaan avata äänitallenne, jonka käyttäjä aikoo puhtaaksikirjoittaa. Tavoitteena on tarjota sujuva käyttöliittymä yhtäaikaiseen äänitallenteen toiston hallinnointiin
ja tekstin tuottamiseen. Lisäksi sovellukseen on suunnitteilla jonkinlainen virkkeiden (tallenteen puhujan lausumien) luokittelutyökalu.

## Käyttäjät

Alustavasti sovelluksella on vain yksi käyttäjärooli.

## Toiminnallisuus

- käyttäjä voi avata äänitallenteen levymuistista ja samalla luoda uuden litterointiprojektin
- olemassa olevan projektin voi avata uudelleen
- nauhoitteen toiston voi aloittaa ja pysäyttää pikanäppäimellä tai hiirellä näkymän painikkeista
- käyttäjä voi kirjoittaa ja muokata tekstiä kaiken aikaa projektin ollessa avoinna
- käyttäjä voi merkitä meneillä olevan virkkeen päättymiskohdan, jolloin sovellus linkittää virkkeen äänitallenteen osaväliin
- äänitallenteen toistoa voi hallinnoida aikahyppäyksillä eteen ja taakse pikanäppäimillä tai näkymän painikkeilla
- käyttäjä voi luokitella virkkeen vapaasti kirjoittamalla näkymän luokittelukenttiin
- sovelluksen näkymäksi voi valita vaihtoehtoisesti joko virkekohtaisen näkymän tai jatkuvan tekstin, jolloin luokittelukentät poistuvat näkymästä
- käyttäjä voi siirtyä hakupuunäkymään, missä näkyviin suodatetaan haettavaan luokkaan kuuluvat virkkeet

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/nakymat.png "Näkymät")

### Mahdollisia laajennuksia toiminnallisuuksiin

- äänitallenteen toistoa voi hidastaa esimerkiksi 1/2- tai 1/4-kertaiseksi, jotta yhtäaikainen kirjoittaminen helpottuu
- luokittelukenttiin kirjoitettaessa avautuu pikavalintalista projektissa aiemmin käytetyistä luokista
- sovelluksella tuotettu teksti voidaan viedä esimerkiksi tekstitiedostoon tai taulukoksi html-tiedostoon