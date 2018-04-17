# Arkkitehtuurikuvaus

## Rakenne

Sovelluksen rakenne jakaantuu kolmeen tasoon. Ylimmällä tasolla pakkauksen _jm.transcribebuddy.gui_ sisältö vastaa sovelluksen graafisesta käyttöliittymästä. Käyttöliittymää palvelee _jm.transcribebuddy.logics_ -pakkaukseen sisällytetty sovelluslogiikka, missä toteutetaan sovelluksen perustoiminnallisuudet. Kolmannen ja alimman tason pakkaus _jm.transcribebuddy.dao_ puolestaan huolehtii tietojen pysyväistallennuksesta.

## Pakkauskaavio

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/pakkauskaavio.png "Pakkauskaavio")

Yllä on sovelluksen alustava pakkauskaavio. Käyttöjärjestelmän kolmatta näkymää hallinnoiva luokka _OverviewController_ on vielä toteuttamatta. Dao-pakkauksen kehitys on myöskin vasta alkutekijöissään.
