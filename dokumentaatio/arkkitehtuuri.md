# Arkkitehtuurikuvaus

## Rakenne

Sovelluksen rakenne jakaantuu kolmeen tasoon. Ylimm�ll� tasolla pakkauksen _jm.transcribebuddy.gui_ sis�lt� vastaa sovelluksen graafisesta k�ytt�liittym�st�. K�ytt�liittym�� palvelee _jm.transcribebuddy.logics_ -pakkaukseen sis�llytetty sovelluslogiikka, miss� toteutetaan sovelluksen perustoiminnallisuudet. Kolmannen ja alimman tason pakkaus _jm.transcribebuddy.dao_ puolestaan huolehtii tietojen pysyv�istallennuksesta.

