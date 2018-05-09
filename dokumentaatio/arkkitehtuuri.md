# Arkkitehtuurikuvaus

## Rakenne

Sovelluksen rakenne jakaantuu kolmeen tasoon. Ylimmällä tasolla pakkauksen _jm.transcribebuddy.gui_ sisältö vastaa sovelluksen graafisesta käyttöliittymästä. Käyttöliittymää palvelee _jm.transcribebuddy.logics_ -pakkaukseen sisällytetty sovelluslogiikka, missä toteutetaan sovelluksen perustoiminnallisuudet. Kolmannen ja alimman tason pakkaus _jm.transcribebuddy.dao_ puolestaan huolehtii tietojen pysyväistallennuksesta.

### Pakkauskaavio

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_package.png "Pakkauskaavio")

Yllä on sovelluksen tämänhetkinen pakkauskaavio. Kukin kolmesta suuresta paketista eriytyy yhä pienempiin pakkauksiin.

## GUI - graafinen käyttöliittymä

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_gui.png "Gui")

Käyttöliittymää [jm.transcribebuddy.gui](https://github.com/jmaattanen/otm-harjoitustyo/tree/master/TranscribeBuddy/src/main/java/jm/transcribebuddy/gui) hallitseva luokka on _MainApp.java_. Se on myöskin javan main-metodin sisältävä luokka.

Käyttöliittymä koostuu kolmesta näkymästä, joista käyttäjälle näkyy aina yksi kerrallaan. Ohjelman käynnistyttyä käyttäjälle aukeaa _tekstinäkymä (ConstantText)_, jossa ohjelmalla kirjoitettu teksti näkyy kokonaisuudessaan. Toinen näkymä nimeltään _rivinäkymä (LineByLine)_ näyttää pienemmän osan tekstistä mutta antaa käyttöön enemmän työkaluja asiakirjan muokkaukseen. Vielä kolmantena on _hakunäkymä (Overview)_, joka taulukoi asiakirjan osia luokitteluominaisuuteen perustuen. Kunkin näkymän layout on kuvattu omassa [fxml-tiedostossaan](https://github.com/jmaattanen/otm-harjoitustyo/tree/master/TranscribeBuddy/src/main/resources/fxml) ja toiminnallisuuksia ohjaa _gui_-paketin fxml-kontrolleriluokat.

Lisäksi joitain ilmoituksia ja dialogeja näytetään erillisten ponnahdusikkunoiden avulla.

## Logics - sovelluslogiikka

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_logics.png "Logics")

Sovelluslogiikan [jm.transcribebuddy.logics](https://github.com/jmaattanen/otm-harjoitustyo/tree/master/TranscribeBuddy/src/main/java/jm/transcribebuddy/logics) johtava luokka on _MainController.java_. Sen voi mieltää abstraktiona gui:n ja sovelluslogiikan välissä.

_storage_-paketissa on logiikan käyttämiä lähinnä yksinkertaisia gettereitä ja settereitä sisältäviä tietorakenteenomaisia luokkia. Näistä tärkein lienee [Statement.java](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/TranscribeBuddy/src/main/java/jm/transcribebuddy/logics/storage/Statement.java)-luokka, jonka ilmentymiin säilötään asiakirjan tekstinpalaset, mikä heijastuu hyvin sovelluksen _rivinäkymästä_. _Statement_-olioiden kokonaisuutta puolestaan hallinnoi _TextBuilder_-olio. Luokassa _TextBuilder.java_ on ydinmetodit logiikan tekstinkäsittelyominaisuuksista ja sen perillisessä _DetailedTextBuilder.java_-luokassa ominaisuuksia on laajennettu kattamaan tekstin ja ääniraidat yhteensovittaminen sekä _Statement_-olioiden luokittelu.

Luokitteluominaisuuden logiikasta itsessään vastaa _Classifier.java_-luokka. Tällä hetkellä _Statement_-olioiden kategorisointiin on käytössä kahden korkuinen hakupuurakenne, jossa ensimmäisen tason solmut kuvaavat sovelluksen termein _yläluokkia (headcategory)_ ja toisella tasolla _alaluokkia (subcategory)_.
_storage_-paketin _Category.java_-luokan ilmentymät puolestaan vastaavat yhtä puun solmua. _Statement_-ilmentymä voi olla liitettynä täsmälleen yhteen puun lehteen eli _alaluokkaan_.


## DAO - pysyväistallennus

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_dao.png "Dao")

Paketin [jm.transcribebuddy.dao](https://github.com/jmaattanen/otm-harjoitustyo/tree/master/TranscribeBuddy/src/main/java/jm/transcribebuddy/dao) palveluja tarjoaa korkeimmalla tasolla luokka _ProjectDao.java_. _file_-paketti keskittyy (*.txt)-tiedostomuotoiseen levytallennukseen ja _db_-paketti puolestaan tietokantatallennukseen.

## Päätoiminnallisuudet

Käyttäjän tekstikenttään syöttämä teksti tallennetaan _textBuilder_-olion linkitettyyn listaan mm. _MainController.set_-metodilla. Alla oleva sekvenssikaavio kuvaa metodin etenemistä esimerkkitapauksessa.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/set_statement.png "MainController.set")
