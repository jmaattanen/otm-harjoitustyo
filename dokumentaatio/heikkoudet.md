# Ohjelman heikkoudet

- Sovelluksella tallennetun tekstitiedoston polku toimii avaimena tietokantahauissa. Jos tiedosto jostain syystä poistetaan, niin tietokantaan jääneisiin tietoihin on hankala päästä käsiksi. Samoin tiedoston siirtäminen tuottaa ongelmia.
- Tietokantahakuja ei ole toteutettu erityisen tehokkaasti, sillä jokaisen _Statement_-ilmentymän tila palautetaan erillisellä tietokantakyselyllä.
- FXML on vaikkakin tehokas niin jokseenkin kömpelö tapa kuvata ohjelman ulkoasua. Sovelluksen näkymät saattavat muotoutua arvaamattomasti eri suoritusympäristöissä.
- _Statement_-_Category_ -relaatiot on linkitetty vain yhteen suuntaan.