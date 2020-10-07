-- ------------------------------------------------------------------------
-- Data & Persistency
-- Opdracht S6: Views
--
-- (c) 2020 Hogeschool Utrecht
-- Tijmen Muller (tijmen.muller@hu.nl)
-- André Donk (andre.donk@hu.nl)
-- ------------------------------------------------------------------------


-- S6.1.
--
-- 1. Maak een view met de naam "deelnemers" waarmee je de volgende gegevens uit de tabellen inschrijvingen en uitvoering combineert:
--    inschrijvingen.cursist, inschrijvingen.cursus, inschrijvingen.begindatum, uitvoeringen.docent, uitvoeringen.locatie
CREATE OR REPLACE VIEW 	deelnemers AS
SELECT ins.cursist, ins.cursus, ins.begindatum, uitv.docent, uitv.locatie
FROM inschrijvingen ins, uitvoeringen uitv
WHERE ins.cursus = uitv.cursus
AND ins.begindatum = uitv.begindatum;

-- 2. Gebruik de view in een query waarbij je de "deelnemers" view combineert met de "personeels" view (behandeld in de les):
--     CREATE OR REPLACE VIEW personeel AS
-- 	     SELECT mnr, voorl, naam as medewerker, afd, functie
--       FROM medewerkers;
SELECT *
FROM deelnemers
LEFT JOIN personeel
ON mnr = cursist;
-- 3. Is de view "deelnemers" updatable ? Waarom ?
UPDATE deelnemers SET docent = 7566 WHERE cursist = 7499;
'DETAIL: Views that do not select from a single table or view are not automatically updatable';

-- S6.2.
--
-- 1. Maak een view met de naam "dagcursussen". Deze view dient de gegevens op te halen:
--      code, omschrijving en type uit de tabel curssussen met als voorwaarde dat de lengte = 1. Toon aan dat de view werkt.
CREATE OR REPLACE VIEW dagcursussen AS
SELECT code, omschrijving, type
FROM cursussen
WHERE lengte = 1;

-- 2. Maak een tweede view met de naam "daguitvoeringen".
--    Deze view dient de uitvoeringsgegevens op te halen voor de "dagcurssussen" (gebruik ook de view "dagcursussen"). Toon aan dat de view werkt
CREATE OR REPLACE VIEW daguitvoeringen AS
SELECT cursus, begindatum, docent, locatie
FROM uitvoeringen uitv
JOIN dagcursussen dagc
ON uitv.cursus = dagc.code
-- 3. Verwijder de views en laat zien wat de verschillen zijn bij DROP view <viewnaam> CASCADE en bij DROP view <viewnaam> RESTRICT
DROP VIEW dagcursussen CASCADE
'CASCADE: Verwijdert ook de views waaraan de view is gelinkt.'
DROP VIEW dagcursussen RESTRICT
'RESTRICT: Verwijdert alleen de view als deze niet in een andere view wordt gebruikt.'