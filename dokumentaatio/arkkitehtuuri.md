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

Lisäksi joitain ilmoituksia ja dialogeja näytetään erillisten ponnahdusikkunoiden avulla, kuten projektin avaus- ja tallennusdialogit.

## Logics - sovelluslogiikka

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_logics.png "Logics")

Sovelluslogiikan [jm.transcribebuddy.logics](https://github.com/jmaattanen/otm-harjoitustyo/tree/master/TranscribeBuddy/src/main/java/jm/transcribebuddy/logics) johtava luokka on _MainController.java_. Sen voi mieltää abstraktiona gui:n ja sovelluslogiikan välissä.

_Storage_-paketissa on logiikan käyttämiä lähinnä yksinkertaisia gettereitä ja settereitä sisältäviä tietorakenteenomaisia luokkia. Näistä tärkein lienee [Statement.java](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/TranscribeBuddy/src/main/java/jm/transcribebuddy/logics/storage/Statement.java)-luokka, jonka ilmentymiin säilötään asiakirjan tekstinpalaset, mikä heijastuu hyvin sovelluksen _rivinäkymästä_. _Statement_-olioiden kokonaisuutta puolestaan hallinnoi _TextBuilder_-olio. Luokassa _TextBuilder.java_ on ydinmetodit logiikan tekstinkäsittelyominaisuuksista ja sen perillisessä _DetailedTextBuilder.java_-luokassa ominaisuuksia on laajennettu kattamaan tekstin ja ääniraidat yhteensovittaminen sekä _Statement_-olioiden luokittelu.

Luokitteluominaisuuden logiikasta itsessään vastaa _Classifier.java_-luokka. Tällä hetkellä _Statement_-olioiden kategorisointiin on käytössä kahden korkuinen hakupuurakenne, jossa ensimmäisen tason solmut kuvaavat sovelluksen termein _yläluokkia (headcategory)_ ja toisella tasolla _alaluokkia (subcategory)_.
_Storage_-paketin _Category.java_-luokan ilmentymät puolestaan vastaavat yhtä puun solmua. _Statement_-ilmentymä voi olla liitettynä täsmälleen yhteen puun lehteen eli _alaluokkaan_.
Tällä hetkellä _Statement_-_Category_ -relaatio on linkitetty vain yhteen suuntaan eli _Statement_-ilmentymä tuntee oman alaluokkansa, mutta alaluokasta ei ole linkityksiä siihen kuuluviin _Statement_-ilmentymiin. Tämän linkityksen voisi ehdottomasti kehittää ajan salliessa kaksisuuntaiseksi.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_tree.png "Classifier")

## DAO - pysyväistallennus

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_dao.png "Dao")

Paketin [jm.transcribebuddy.dao](https://github.com/jmaattanen/otm-harjoitustyo/tree/master/TranscribeBuddy/src/main/java/jm/transcribebuddy/dao) palveluja tarjoaa korkeimmalla tasolla luokka _ProjectDao.java_. _File_-paketti keskittyy (*.txt)-tiedostomuotoiseen levytallennukseen ja _db_-paketti puolestaan tietokantatallennukseen.

Asiakirjan tallennus suoritetaan kolmessa vaiheessa. Ensin tallennetaan projektin yleiset tiedot, kuten projektin nimi, kuvaus ja äänitallenteen URI. Toisessa vaiheessa tallennetaan asiakirjan raakateksti. Kolmannessa vaiheessa tallennetaan vielä _Statement_-olioiden aikamerkit.
Tallennuksen ensimmäinen ja kolmas vaihe ovat tällä hetkellä riippuvaisia tietokantayhteydestä. Mikäli tietokantayhteyttä ei ole konfiguroitu oikein sovellusta käynnistäessä, niin osa tiedoista jää tallentamatta.
Projektin tilaa ladattaessa tietokannasta haun avaimena toimii tekstitiedoston polku.

Alemman tason daot _FileTextDao_, _DBTextInfoDao_ ja _DBProjectDao_ on luotu rajapintojen taakse, jotta käyttäjälle voidaan tarjota vaihtoehtoinen tallennusmuoto esimerkiksi yhdeksi pakatuksi tiedostoksi, jolloin yksittäinen projekti olisi helposti siirrettävissä laitteelta toiselle.
Toistaiseksi vaihtoehtoinen tallennusformaatti on vasta suunnitteluasteella.

## Päätoiminnallisuudet

_Teksti_- ja _rivinäkymissä_ käyttäjä voi aloittaa uuden litterointiprojektin __Aloita__-painikkeesta tai ladata aiemmin tallentamansa projektin.
Projektin ääniraidaksi valitaan levyltä (*.mp3), (*.m4a) tai (*.wav)-muotoinen tiedosto, jonka URI:n voi jälkeenpäin muuttaa __Info__-valikosta. Tämän jälkeen käyttäjä voi aloittaa luomaan asiakirjaa vapaasti kirjoittamalla näkymien aktiivisiin tekstikenttiin.
Sovelluslogiikka säilöö tekstikenttien muuttuneen sisällön vasta, kun jotain päivityksen aktivoivaa toimenpidettä käytetään. Päivittäviä toimenpiteitä ovat näkymänvaihto, projektin tallennus sekä virkkeen päättäminen, jakaminen, valitseminen ja poistaminen.
Käyttäjän tekstikenttään syöttämä teksti tallennetaan _textBuilder_-olion linkitettyyn listaan mm. _MainController.set_-metodilla. Alla oleva sekvenssikaavio kuvaa metodin etenemistä esimerkkitapauksessa.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/set_statement.png "MainController.set")

