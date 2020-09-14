-- S3.1.
-- Produceer een overzicht van alle cursusuitvoeringen; geef de
-- cursuscode, de begindatum, de cursuslengte en de naam van de docent.
DROP VIEW IF EXISTS s3_1;
CREATE OR REPLACE VIEW s3_1 AS

SELECT code, begindatum, lengte, med.naam
FROM cursussen cursus, uitvoeringen uitv
INNER JOIN medewerkers med ON med.mnr = uitv.docent

WHERE uitv.cursus = cursus.code

-- S3.2.
-- Geef in twee kolommen naast elkaar de naam van elke cursist (`cursist`)
-- die een S02-cursus heeft gevolgd, met de naam van de docent (`docent`).
DROP VIEW IF EXISTS s3_2;
CREATE OR REPLACE VIEW s3_2 AS

SELECT cursist.naam AS cursist, docent.naam AS docent
FROM medewerkers cursist
INNER JOIN inschrijvingen ins
ON cursist.mnr = ins.cursist
INNER JOIN uitvoeringen uit
ON ins.begindatum = uit.begindatum AND
ins.cursus = uit.cursus
INNER JOIN medewerkers docent
ON docent.mnr = uit.docent

WHERE ins.cursus = 'S02'

-- S3.3.
-- Geef elke afdeling (`afdeling`) met de naam van het hoofd van die
-- afdeling (`hoofd`).

DROP VIEW IF EXISTS s3_3;
CREATE OR REPLACE VIEW s3_3 AS

SELECT afd.naam AS afdeling, med.naam AS hoofd
FROM afdelingen afd
LEFT JOIN medewerkers med
ON afd.hoofd = med.mnr

-- S3.4.
-- Geef de namen van alle medewerkers, de naam van hun afdeling (`afdeling`)
-- en de bijbehorende locatie.
DROP VIEW IF EXISTS s3_4; CREATE OR REPLACE VIEW s3_4 AS

SELECT med.naam AS medewerker, afd.naam AS afdeling, afd.locatie
FROM medewerkers med
INNER JOIN afdelingen afd
ON afd.anr = med.afd

-- S3.5.
-- Geef de namen van alle cursisten die staan ingeschreven voor de cursus S02 van
-- 12 april 2019
DROP VIEW IF EXISTS s3_5; CREATE OR REPLACE VIEW s3_5 AS

SELECT med.naam--, inschr.begindatum
FROM medewerkers med
INNER JOIN inschrijvingen inschr
ON med.mnr = inschr.cursist

WHERE inschr.begindatum = '2019-04-12' AND inschr.cursus = 'S02'

-- S3.6.
-- Geef de namen van alle medewerkers en hun toelage.
DROP VIEW IF EXISTS s3_6; CREATE OR REPLACE VIEW s3_6 AS

SELECT med.naam, schalen.toelage--, schalen.snr, med.maandsal
FROM medewerkers med
LEFT JOIN schalen
ON med.maandsal <= 700 OR
(med.maandsal >= schalen.ondergrens AND med.maandsal <= schalen.bovengrens)

ORDER BY maandsal