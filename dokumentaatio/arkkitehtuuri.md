# Arkkitehtuurikuvaus

## Rakenne

Sovelluksen rakenne jakaantuu kolmeen tasoon. Ylimm�ll� tasolla pakkauksen _jm.transcribebuddy.gui_ sis�lt� vastaa sovelluksen graafisesta k�ytt�liittym�st�. K�ytt�liittym�� palvelee _jm.transcribebuddy.logics_ -pakkaukseen sis�llytetty sovelluslogiikka, miss� toteutetaan sovelluksen perustoiminnallisuudet. Kolmannen ja alimman tason pakkaus _jm.transcribebuddy.dao_ puolestaan huolehtii tietojen pysyv�istallennuksesta.

## Pakkauskaavio

![alt text](https://github.com/jmaattanen/otm-harjoitustyo/blob/master/dokumentaatio/pakkauskaavio.png "Pakkauskaavio")

Yll� on sovelluksen alustava pakkauskaavio. K�ytt�j�rjestelm�n kolmatta n�kym�� hallinnoiva luokka _OverviewController_ on viel� toteuttamatta. Dao-pakkauksen kehitys on my�skin vasta alkutekij�iss��n.
