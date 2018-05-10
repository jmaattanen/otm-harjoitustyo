# Määrittelydokumentti

## Käyttötarkoitus

Sovellus toimii litteroinnin apuvälineenä. Sovelluksella voidaan avata äänitallenne, jonka käyttäjä aikoo puhtaaksikirjoittaa. Tavoitteena on tarjota sujuva käyttöliittymä yhtäaikaiseen äänitallenteen toiston hallinnointiin
ja tekstin tuottamiseen. Sovelluksella on voidaan myös jäsentää tuotettuja tekstin osia kaksiportaisen luokittelun avulla.

## Käyttäjät

Sovelluksella on vain yksi käyttäjärooli, eikä erillistä kirjautumista vaadita.

## Toiminnallisuus

- käyttäjä voi avata äänitallenteen levymuistista ja samalla luoda uuden litterointiprojektin
- projektin voi tallentaa ja avata uudelleen, vaikka sovellus välillä suljettaisiin
- nauhoitteen toiston voi aloittaa ja pysäyttää pikanäppäimellä tai hiirellä näkymän painikkeista
- käyttäjä voi kirjoittaa ja muokata tekstiä kaiken aikaa projektin ollessa avoinna
- käyttäjä voi merkitä muokattavana olevan virkkeen alkamiskohdan, jolloin sovellus linkittää virkkeen äänitallenteen kohtaan
- äänitallenteen toistoa voi hallinnoida aikahyppäyksillä eteen ja taakse pikanäppäimillä tai näkymän painikkeilla
- käyttäjä voi luokitella virkkeen vapaasti kirjoittamalla näkymän luokittelukenttään
- sovelluksen näkymäksi voi valita vaihtoehtoisesti joko virkekohtaisen näkymän tai jatkuvan tekstin, jolloin luokittelukentät poistuvat näkymästä
- käyttäjä voi siirtyä hakunäkymään, missä näkyviin suodatetaan haettavaan luokkaan kuuluvat virkkeet

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/nakymat.png "Näkymät")

### Mahdollisia laajennuksia toiminnallisuuksiin

Periodi hujahti niin äkkiä, että jatkokehitettävää riittää yhä pitkäksi aikaa:

- luokittelurakenteen pysyväistallennus tulisi toteuttaa ASAP
- pysyväistallennukselle voisi tarjota vaihtoehtoisen tallennusmuodon esim yhdeksi pakatuksi tiedostoksi, mikä helpottaisi projektin siirrettävyyttä
- hakunäkymään voisi tuoda näkyville kerralla useampia sovelluksella tuotettuja projekteja (esim kokoelma haastatteluja johonkin tutkimukseen liittyen)
- luokittelukenttiin kirjoitettaessa avautuu pikavalintalista projektissa aiemmin käytetyistä luokista
- sovelluksella tuotettu teksti voidaan viedä luokkarakennetta mukaillen esimerkiksi taulukoksi html-tiedostoon
- äänitallenteen toistoa voi hidastaa esimerkiksi 1/2-kertaiseksi, jotta yhtäaikainen kirjoittaminen helpottuu
- näkymiin voi lisätä kuuntelijan, joka näyttää missä kohtaa ääniraidan toisto on
- ääniraidan toistoa voisi hallita sliderin avulla
- aikahyppäyksien pituudet voisi olla käyttäjän määritettävissä
- tietokannan käyttäjätunnuksen ja salasanan tulisi pystyä määrittämään ohjelman käyttöliittymällä (ellei laiska käyttäjä halua lojuttaa tietojaan kaikkien nähtävillä)