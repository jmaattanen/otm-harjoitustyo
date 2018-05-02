# Käyttöohje

## Suoritus ja konfigurointi

Sovelluksen [Release 1](https://github.com/jmaattanen/otm-harjoitustyo/releases/tag/viikko5)-version pystyy suorittamaan koneella, jolle on asennettu Java versio 1.8.
Ohjelman voi suorittaa polusta, jonne jar-tiedosto on kopioitu, komennolla

```
java -jar TranscribeBuddy_release1.jar
```

Ensimmäisellä suorituskerralla ohjelman pitäisi luoda omaan kotipolkuunsa tiedosto _config.properties_. Kyseisen tiedoston avulla voidaan konfiguroida tietokantayhteys ulkoiseen PostgreSQL-tietokantapalvelimeen.
Jos _config.properties_-tiedostoa ei jostain syystä luoda ensimmäisellä ajokerralla, voit luoda itse samannimisen tiedoston sovelluksen juureen ja lisätä sinne vastaavat rivit:

```
postgresUser=postgres
postgresPass=salasanasi
databaseURL=jdbc:postgresql://localhost:5432/tietokantasinimi
```

Tietokantayhteys ei ole välttämätön sovelluksen käytön kannalta, mutta se mahdollistaa mm. virkkeiden merkkien ja ääniraidan sijainnin muistamisen suorituskertojen välillä. Litterointiprojektin leipäteksti itsessään tallennetaan *.txt-muotoiseen tiedostoon.
[Postgresin sivuilta](https://jdbc.postgresql.org/documentation/head/connect.html) löytyy lisää ohjeita tietokantaan yhdistämisestä.

Sovellus tukee nyt myös SQLite-tietokantaa. Seuraava konfiguraatio luo _munbeissi.db_-nimisen tietokannan sovelluksen juureen:

```
databaseURL=jdbc:sqlite:munbeissi.db
```


## Näppäinkomennot

| Näppäinyhdistelmä          | Toiminto                                                      |
| :-------------------------:| :-------------------------------------------------------------|
| _ctrl_ + _space_           | Aloita/keskeytä ääniraidan toisto                             |
| _ctrl_ + _shift_ + _space_ | Aloita ääniraidan toisto asetetusta merkistä rivinäkymässä    |
| _ctrl_ + _shift_ + _N_     | Hyppää 30s eteenpäin toistossa                                |
| _ctrl_ + _N_               | Hyppää 5s eteenpäin toistossa                                 |
| _ctrl_ + _shift_ + _B_     | Hyppää 30s taaksepäin toistossa                               |
| _ctrl_ + _B_               | Hyppää 5s taaksepäin toistossa                                |
| _ctrl_ + _PGUP_            | Siirry edelliseen virkkeeseen rivinäkymässä                   |
| _ctrl_ + _PGDN_            | Siirry seuraavaan virkkeeseen rivinäkymässä                   |
| _ctrl_ + _ENTER_           | Päätä virke rivinäkymässä                                     |
| _ctrl_ + ,                 | Siirry tekstinäkymään (Poistettu käytöstä release1-versiossa) |
| _ctrl_ + .                 | Siirry rivinäkymään (Poistettu käytöstä release1-versiossa)   |
| _ctrl_ + -                 | Siirry hakunäkymään                                           |

## Vinkkejä sovelluksen käyttöön

- Jo päätettyjen virkkeiden muokkaus/poistaminen tulee tehdä aina rivinäkymässä.
- Myös tyhjän rivin voi päättää rivinäkymässä, jolloin se näkyy tekstissä kappaleenvaihtona.
- Siirryttäessä tekstinäkymästä rivinäkymään voit navigoida haluamaasi kohtaan tekstissä siirtämällä tekstikursorin haluamaasi kohtaan ennen näkymänvaihtoa.
- Rivinäkymän jaa virke -toiminto katkaisee virkkeen tekstikursorin osoittamasta kohdasta.