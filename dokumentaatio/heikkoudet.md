# Ohjelman heikkoudet

- Sovelluksella tallennetun tekstitiedoston polku toimii avaimena tietokantahauissa. Jos tiedosto jostain syyst� poistetaan, niin tietokantaan j��neisiin tietoihin on hankala p��st� k�siksi. Samoin tiedoston siirt�minen tuottaa ongelmia.
- Tietokantahakuja ei ole toteutettu erityisen tehokkaasti, sill� jokaisen _Statement_-ilmentym�n tila palautetaan erillisell� tietokantakyselyll�.
- FXML on vaikkakin tehokas niin jokseenkin k�mpel� tapa kuvata ohjelman ulkoasua. Sovelluksen n�kym�t saattavat muotoutua arvaamattomasti eri suoritusymp�rist�iss�.
- _Statement_-_Category_ -relaatiot on linkitetty vain yhteen suuntaan.