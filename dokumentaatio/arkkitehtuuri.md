# Arkkitehtuurikuvaus

## Rakenne

Sovelluksen rakenne jakaantuu kolmeen tasoon. Ylimm�ll� tasolla pakkauksen _jm.transcribebuddy.gui_ sis�lt� vastaa sovelluksen graafisesta k�ytt�liittym�st�. K�ytt�liittym�� palvelee _jm.transcribebuddy.logics_ -pakkaukseen sis�llytetty sovelluslogiikka, miss� toteutetaan sovelluksen perustoiminnallisuudet. Kolmannen ja alimman tason pakkaus _jm.transcribebuddy.dao_ puolestaan huolehtii tietojen pysyv�istallennuksesta.

### Pakkauskaavio

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_package.png "Pakkauskaavio")

Yll� on sovelluksen t�m�nhetkinen pakkauskaavio. Kukin kolmesta suuresta paketista eriytyy yh� pienempiin pakkauksiin.

## GUI - graafinen k�ytt�liittym�

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_gui.png "Gui")

K�ytt�liittym�� [jm.transcribebuddy.gui](https://github.com/jmaattanen/otm-harjoitustyo/tree/master/TranscribeBuddy/src/main/java/jm/transcribebuddy/gui) hallitseva luokka on _MainApp.java_. Se on my�skin javan main-metodin sis�lt�v� luokka.

K�ytt�liittym� koostuu kolmesta n�kym�st�, joista k�ytt�j�lle n�kyy aina yksi kerrallaan. Ohjelman k�ynnistytty� k�ytt�j�lle aukeaa _tekstin�kym� (ConstantText)_, jossa ohjelmalla kirjoitettu teksti n�kyy kokonaisuudessaan. Toinen n�kym� nimelt��n _rivin�kym� (LineByLine)_ n�ytt�� pienemm�n osan tekstist� mutta antaa k�ytt��n enemm�n ty�kaluja asiakirjan muokkaukseen. Viel� kolmantena on _hakun�kym� (Overview)_, joka taulukoi asiakirjan osia luokitteluominaisuuteen perustuen. Kunkin n�kym�n layout on kuvattu omassa [fxml-tiedostossaan](https://github.com/jmaattanen/otm-harjoitustyo/tree/master/TranscribeBuddy/src/main/resources/fxml) ja toiminnallisuuksia ohjaa _gui_-paketin fxml-kontrolleriluokat.

Lis�ksi joitain ilmoituksia ja dialogeja n�ytet��n erillisten ponnahdusikkunoiden avulla.

## Logics - sovelluslogiikka

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_logics.png "Logics")

Sovelluslogiikan [jm.transcribebuddy.logics](https://github.com/jmaattanen/otm-harjoitustyo/tree/master/TranscribeBuddy/src/main/java/jm/transcribebuddy/logics) johtava luokka on _MainController.java_. Sen voi mielt�� abstraktiona gui:n ja sovelluslogiikan v�liss�.

_storage_-paketissa on logiikan k�ytt�mi� l�hinn� yksinkertaisia gettereit� ja settereit� sis�lt�vi� tietorakenteenomaisia luokkia. N�ist� t�rkein lienee [Statement.java](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/TranscribeBuddy/src/main/java/jm/transcribebuddy/logics/storage/Statement.java)-luokka, jonka ilmentymiin s�il�t��n asiakirjan tekstinpalaset, mik� heijastuu hyvin sovelluksen _rivin�kym�st�_. _Statement_-olioiden kokonaisuutta puolestaan hallinnoi _TextBuilder_-olio. Luokassa _TextBuilder.java_ on ydinmetodit logiikan tekstink�sittelyominaisuuksista ja sen perillisess� _DetailedTextBuilder.java_-luokassa ominaisuuksia on laajennettu kattamaan tekstin ja ��niraidat yhteensovittaminen sek� _Statement_-olioiden luokittelu.

Luokitteluominaisuuden logiikasta itsess��n vastaa _Classifier.java_-luokka. T�ll� hetkell� _Statement_-olioiden kategorisointiin on k�yt�ss� kahden korkuinen hakupuurakenne, jossa ensimm�isen tason solmut kuvaavat sovelluksen termein _yl�luokkia (headcategory)_ ja toisella tasolla _alaluokkia (subcategory)_.
_storage_-paketin _Category.java_-luokan ilmentym�t puolestaan vastaavat yht� puun solmua. _Statement_-ilmentym� voi olla liitettyn� t�sm�lleen yhteen puun lehteen eli _alaluokkaan_.


## DAO - pysyv�istallennus

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_dao.png "Dao")

Paketin [jm.transcribebuddy.dao](https://github.com/jmaattanen/otm-harjoitustyo/tree/master/TranscribeBuddy/src/main/java/jm/transcribebuddy/dao) palveluja tarjoaa korkeimmalla tasolla luokka _ProjectDao.java_. _file_-paketti keskittyy (*.txt)-tiedostomuotoiseen levytallennukseen ja _db_-paketti puolestaan tietokantatallennukseen.

## P��toiminnallisuudet

K�ytt�j�n tekstikentt��n sy�tt�m� teksti tallennetaan _textBuilder_-olion linkitettyyn listaan mm. _MainController.set_-metodilla. Alla oleva sekvenssikaavio kuvaa metodin etenemist� esimerkkitapauksessa.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/set_statement.png "MainController.set")
