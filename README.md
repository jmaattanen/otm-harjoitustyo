# TranscribeBuddy

Tämä on harjoitustyö kurssille Ohjelmistotekniikan menetelmät kevällä 2018. Harjoitustyö toteutetaan maven-projektina käyttäen JavaFX:ää ja FXML-kuvauskieltä.

## Määrittelydokumentti

[Linkki määrittelydokumenttiin](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentointi/maarittelydokumentti.md)

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

## Työaikakirjanpito

[Linkki työaikakirjanpitoon](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentointi/tuntikirjanpito.md)