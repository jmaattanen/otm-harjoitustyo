# TranscribeBuddy

Litteroitsijan apukäsi :) Tällä ohjelmalla saat kirjoitettua äänitallenteet kätevästi tekstimuotoon sekä analysoitua tuotoksen sisältöä. Sovellus on toteutettu maven-projektina käyttäen JavaFX:ää ja FXML-kuvauskieltä. Sovelluksella on riippuvuus PostgreSQL-tietokantapalvelimeen, mutta rinnalle voi myös konfiguroida SQLiten.

## Dokumentaatio

[Määrittelydokumentti](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/maarittelydokumentti.md)

[Arkkitehtuurikuvaus](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/arkkitehtuuri.md)

[Käyttöohje](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/kayttoohje.md)

[Testausdokumentti](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/testaus.md)

[Työaikakirjanpito](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tuntikirjanpito.md)

## Julkaisut

[Release 1](https://github.com/jmaattanen/otm-harjoitustyo/releases/tag/viikko5)

[Release 2](https://github.com/jmaattanen/otm-harjoitustyo/releases/tag/viikko6)

## Komentorivitoiminnot

Seuraavat maven-komennot ovat ajettavissa projektikansiosta _TranscribeBuddy_ käsin:

### JAR-paketointi

Jar-tiedoston voi generoida komennolla 

```
mvn package
```

Generoitu _TranscribeBuddy-1.0-SNAPSHOT.jar_ löytyy hakemistosta _target_

### Lähdekoodin kääntäminen

Projektin voi suorittaa komennolla

```
mvn compile exec:java -Dexec.mainClass=jm.transcribebuddy.gui.MainApp
```

### Testaus

Testikattavuusraportti generoidaan komennolla

```
mvn test jacoco:report
```

Raportin voi avata selaimella polusta _/target/site/jacoco/index.html_

### Checkstyle

Sovelluslogiikan lähdekoodin ulkoasun voi analysoida komennolla

```
mvn jxr:jxr checkstyle:checkstyle
```

Checkstyle-raportti löytyy polusta _/target/site/checkstyle.html_

Checkstyle on konfiguroitu tiedostossa _checkstyle.xml_

### JavaDoc

JavaDocin voi generoida komennolla

```
mvn javadoc:javadoc
```

Generoidun JavaDocin voi avata selaimella polusta _/target/site/apidocs/index.html_

