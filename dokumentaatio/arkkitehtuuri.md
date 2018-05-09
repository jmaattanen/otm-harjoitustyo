# Arkkitehtuurikuvaus

## Rakenne

Sovelluksen rakenne jakaantuu kolmeen tasoon. Ylimm�ll� tasolla pakkauksen _jm.transcribebuddy.gui_ sis�lt� vastaa sovelluksen graafisesta k�ytt�liittym�st�. K�ytt�liittym�� palvelee _jm.transcribebuddy.logics_ -pakkaukseen sis�llytetty sovelluslogiikka, miss� toteutetaan sovelluksen perustoiminnallisuudet. Kolmannen ja alimman tason pakkaus _jm.transcribebuddy.dao_ puolestaan huolehtii tietojen pysyv�istallennuksesta.

### Pakkauskaavio

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_package.png "Pakkauskaavio")

Yll� on sovelluksen t�m�nhetkinen pakkauskaavio. Kukin kolmesta suuresta paketista eriytyy yh� pienempiin pakkauksiin.

### Luokkakaaviot

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_gui.png "Gui")

K�ytt�liittym�� _jm.transcribebuddy.gui_ hallitseva luokka on _MainApp.java_. Se on my�skin javan main-metodin sis�lt�v� luokka.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_logics.png "Logics")

Sovelluslogiikan _jm.transcribebuddy.logics_ johtava luokka on _MainController.java_. Sen voi mielt�� abstraktiona gui:n ja sovelluslogiikan v�liss�.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_dao.png "Dao")

Paketin _jm.transcribebuddy.dao_ palveluja tarjoaa korkeimmalla tasolla luokka _ProjectDao.java_. _file_-paketti keskittyy (*.txt)-tiedostomuotoiseen levytallennukseen ja _db_-paketti puolestaan tietokantatallennukseen.

## P��toiminnallisuudet

K�ytt�j�n tekstikentt��n sy�tt�m� teksti tallennetaan _textBuilder_-olion linkitettyyn listaan mm. _MainController.set_-metodilla. Alla oleva sekvenssikaavio kuvaa metodin etenemist� esimerkkitapauksessa.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/set_statement.png "MainController.set")
