# TranscribeBuddy

Tämä on harjoitustyö kurssille Ohjelmistotekniikan menetelmät kevällä 2018. Harjoitustyö toteutetaan maven-projektina käyttäen JavaFX:ää ja FXML-kuvauskieltä. Sovelluksella on riippuvuus PostgreSQL-tietokantapalvelimeen.

## Dokumentaatio

[Määrittelydokumentti](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/maarittelydokumentti.md)

[Arkkitehtuurikuvaus](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/arkkitehtuuri.md)

[Käyttöohje](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/kayttoohje.md)

[Työaikakirjanpito](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tuntikirjanpito.md)

## Julkaisut

[Release 1](https://github.com/jmaattanen/otm-harjoitustyo/releases/tag/viikko5)

[Release 2](https://github.com/jmaattanen/otm-harjoitustyo/releases/tag/viikko6)

## Komentorivitoiminnot

Projektin voi suorittaa projektikansiossa _TranscribeBuddy_ maven-komennolla

```
mvn compile exec:java -Dexec.mainClass=jm.transcribebuddy.gui.MainApp
```

Testikattavuusraportti generoidaan komennolla

```
mvn test jacoco:report
```

Raportin voi avata selaimella polusta _/target/site/jacoco/index.html_

Sovelluslogiikan lähdekoodin ulkoasun voi analysoida komennolla

```
mvn jxr:jxr checkstyle:checkstyle
```

Checkstyle-raportti löytyy polusta _/target/site/checkstyle.html_

Checkstyle on konfiguroitu tiedostossa _checkstyle.xml_

JavaDocin voi generoida komennolla

```
mvn javadoc:javadoc
```

Generoidun JavaDocin voi avata selaimella polusta _/target/site/apidocs/index.html_

