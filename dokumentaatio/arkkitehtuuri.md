# Arkkitehtuurikuvaus

## Rakenne

Sovelluksen rakenne jakaantuu kolmeen tasoon. Ylimmällä tasolla pakkauksen _jm.transcribebuddy.gui_ sisältö vastaa sovelluksen graafisesta käyttöliittymästä. Käyttöliittymää palvelee _jm.transcribebuddy.logics_ -pakkaukseen sisällytetty sovelluslogiikka, missä toteutetaan sovelluksen perustoiminnallisuudet. Kolmannen ja alimman tason pakkaus _jm.transcribebuddy.dao_ puolestaan huolehtii tietojen pysyväistallennuksesta.

### Pakkauskaavio

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_package.png "Pakkauskaavio")

Yllä on sovelluksen tämänhetkinen pakkauskaavio. Kukin kolmesta suuresta paketista eriytyy yhä pienempiin pakkauksiin.

### Luokkakaaviot

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_gui.png "Gui")

Käyttöliittymää _jm.transcribebuddy.gui_ hallitseva luokka on _MainApp.java_. Se on myöskin javan main-metodin sisältävä luokka.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_logics.png "Logics")

Sovelluslogiikan _jm.transcribebuddy.logics_ johtava luokka on _MainController.java_. Sen voi mieltää abstraktiona gui:n ja sovelluslogiikan välissä.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/tb_dao.png "Dao")

Paketin _jm.transcribebuddy.dao_ palveluja tarjoaa korkeimmalla tasolla luokka _ProjectDao.java_. _file_-paketti keskittyy (*.txt)-tiedostomuotoiseen levytallennukseen ja _db_-paketti puolestaan tietokantatallennukseen.

## Päätoiminnallisuudet

Käyttäjän tekstikenttään syöttämä teksti tallennetaan _textBuilder_-olion linkitettyyn listaan mm. _MainController.set_-metodilla. Alla oleva sekvenssikaavio kuvaa metodin etenemistä esimerkkitapauksessa.

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/set_statement.png "MainController.set")
