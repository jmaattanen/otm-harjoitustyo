# K‰yttˆohje

## Suoritus ja konfigurointi

Sovelluksen [Release 1](https://github.com/jmaattanen/otm-harjoitustyo/releases/tag/viikko5)-version pystyy suorittamaan koneella, jolle on asennettu Java versio 1.8.
Ohjelman voi suorittaa polusta, jonne jar-tiedosto on kopioitu, komennolla

```
java -jar TranscribeBuddy_release1.jar
```

Ensimm‰isell‰ suorituskerralla ohjelman pit‰isi luoda omaan kotipolkuunsa tiedosto _config.properties_. Kyseisen tiedoston avulla voidaan konfiguroida tietokantayhteys ulkoiseen PostgreSQL-tietokantapalvelimeen.
Jos _config.properties_-tiedostoa ei jostain syyst‰ luoda ensimm‰isell‰ ajokerralla, voit luoda itse samannimisen tiedoston sovelluksen juureen ja lis‰t‰ sinne vastaavat rivit:

```
postgresUser=_postgres_
postgresPass=_salasanasi_
databaseURL=jdbc:postgresql://localhost:5432/_tietokantasinimi_
```

## N‰pp‰inkomennot

| _ctrl_ + _space_           | Aloita/keskeyt‰ ‰‰niraidan toisto                             |
| _ctrl_ + _shift_ + _space_ | Aloita ‰‰niraidan toisto asetetusta merkist‰                  |
| _ctrl_ + _shift_ + _N_     | Hypp‰‰ 30s eteenp‰in toistossa                                |
| _ctrl_ + _N_               | Hypp‰‰ 5s eteenp‰in toistossa                                 |
| _ctrl_ + _shift_ + _B_     | Hypp‰‰ 30s taaksep‰in toistossa                               |
| _ctrl_ + _B_               | Hypp‰‰ 5s taaksep‰in toistossa                                |
| _ctrl_ + _PGUP_            | Siirry edelliseen virkkeeseen rivin‰kym‰ss‰                   |
| _ctrl_ + _PGDN_            | Siirry seuraavaan virkkeeseen rivin‰kym‰ss‰                   |
| _ctrl_ + _ENTER_           | P‰‰t‰ virke rivin‰kym‰ss‰                                     |
| _ctrl_ + ,                 | Siirry tekstin‰kym‰‰n (Poistettu k‰ytˆst‰ release1-versiossa) |
| _ctrl_ + .                 | Siirry rivin‰kym‰‰n (Poistettu k‰ytˆst‰ release1-versiossa)   |

