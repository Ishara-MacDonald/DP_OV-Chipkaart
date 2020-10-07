-- ------------------------------------------------------------------------
-- Data & Persistency
-- Opdracht S7: Indexen
--
-- (c) 2020 Hogeschool Utrecht
-- Tijmen Muller (tijmen.muller@hu.nl)
-- André Donk (andre.donk@hu.nl)
-- ------------------------------------------------------------------------
-- LET OP, zoals in de opdracht op Canvas ook gezegd kun je informatie over
-- het query plan vinden op: https://www.postgresql.org/docs/current/using-explain.html


-- S7.1.
--
-- Je maakt alle opdrachten in de 'sales' database die je hebt aangemaakt en gevuld met
-- de aangeleverde data (zie de opdracht op Canvas).
--
-- Voer het voorbeeld uit wat in de les behandeld is:
-- 1. Voer het volgende EXPLAIN statement uit:
   EXPLAIN SELECT * FROM order_lines WHERE stock_item_id = 9;
--    Bekijk of je het resultaat begrijpt. Kopieer het explain plan onderaan de opdracht
  ->  Parallel Seq Scan on order_lines  (cost=0.00..5051.27 rows=418 width=96)
  '5051 duration of sorts'
-- 2. Voeg een index op stock_item_id toe:
   CREATE INDEX ord_lines_si_id_idx ON order_lines (stock_item_id);
-- 3. Analyseer opnieuw met EXPLAIN hoe de query nu uitgevoerd wordt
--    Kopieer het explain plan onderaan de opdracht
  ->  Bitmap Index Scan on ord_lines_si_id_idx  (cost=0.00..19.94 rows=1002 width=0)
-- 4. Verklaar de verschillen. Schrijf deze hieronder op.
Index is sneller dan key.
Door te zoeken met de key loopt de query door de hele database.
Met een index erbij wordt er een techniek toegevoegd aan de database waarmee deze sneller iets kan terug vinden.


-- S7.2.
--
-- 1. Maak de volgende twee query’s:
-- 	  A. Toon uit de order tabel de order met order_id = 73590
SELECT * FROM orders WHERE order_id = 73590
-- 	  B. Toon uit de order tabel de order met customer_id = 1028
SELECT * FROM orders WHERE customer_id = 1028
-- 2. Analyseer met EXPLAIN hoe de query’s uitgevoerd worden en
--    kopieer het explain plan onderaan de opdracht
A. Index Scan using pk_sales_orders on orders  (cost=0.29..8.31 rows=1 width=155)
B. Seq Scan on orders  (cost=0.00..1819.94 rows=107 width=155)
-- 3. Verklaar de verschillen en schrijf deze op
Verschil 1: Index Scan vs. Seq Scan
order_id is een primary key en heeft een id en dus al een index. Dit betekent dat elk order_id uniek is en maar één keer kan voorkomen in dit tabel.
De database weet dit en zoekt op basis van PK.
customer_id is geen PK en kan meerdere keren voorkomen, dus moet de database voor deze query de hele tabel checken.
-- 4. Voeg een index toe, waarmee query B versneld kan worden
CREATE INDEX customer_id_idx ON orders (customer_id);
-- 5. Analyseer met EXPLAIN en kopieer het explain plan onder de opdracht
  ->  Bitmap Index Scan on customer_id_idx  (cost=0.00..5.10 rows=107 width=0)
-- 6. Verklaar de verschillen en schrijf hieronder op
Index is sneller dan key.
Door te zoeken met de key loopt de query door de hele database.
Met een index erbij wordt er een techniek toegevoegd aan de database waarmee deze sneller iets kan terug vinden.

-- S7.3.A
--
-- Het blijkt dat customers regelmatig klagen over trage bezorging van hun bestelling.
-- Het idee is dat verkopers misschien te lang wachten met het invoeren van de bestelling in het systeem.
-- Daar willen we meer inzicht in krijgen.
-- We willen alle orders (order_id, order_date, salesperson_person_id (als verkoper),
--    het verschil tussen expected_delivery_date en order_date (als levertijd),
--    en de bestelde hoeveelheid van een product zien (quantity uit order_lines).
-- Dit willen we alleen zien voor een bestelde hoeveelheid van een product > 250
--   (we zijn nl. als eerste geïnteresseerd in grote aantallen want daar lijkt het vaker mis te gaan)
-- En verder willen we ons focussen op verkopers wiens bestellingen er gemiddeld langer over doen.
-- De meeste bestellingen kunnen binnen een dag bezorgd worden, sommige binnen 2-3 dagen.
-- Het hele bestelproces is er op gericht dat de gemiddelde bestelling binnen 1.45 dagen kan worden bezorgd.
-- We willen in onze query dan ook alleen de verkopers zien wiens gemiddelde levertijd
--  (expected_delivery_date - order_date) over al zijn/haar bestellingen groter is dan 1.45 dagen.
-- Maak om dit te bereiken een subquery in je WHERE clause.
-- Sorteer het resultaat van de hele geheel op levertijd (desc) en verkoper.
-- 1. Maak hieronder deze query (als je het goed doet zouden er 377 rijen uit moeten komen, en het kan best even duren...)

-- levertijd:
-- SELECT expected_delivery_date - order_date
-- FROM orders
--
-- totaal_aantal_producten:
-- SELECT order_id, (
--     SELECT SUM(quantity)
--     FROM order_lines
--     GROUP BY order_id
--     HAVING orders.order_id = order_lines.order_id
--     )
-- FROM orders

SELECT order_id, order_date, salesperson_person_id AS verkoper, levertijd,
    (
    SELECT SUM(quantity)
    FROM order_lines
    GROUP BY order_id
    HAVING orders.order_id = order_lines.order_id
    ) AS totaal_aantal_producten
FROM orders, levertijd
WHERE EXISTS (
    SELECT SUM(quantity)
    FROM order_lines
    GROUP BY order_id
    HAVING orders.order_id = order_lines.order_id
    AND SUM(quantity) > 250
    )
AND ( expected_delivery_date - order_date) > 1.45

ORDER BY levertijd DESC and verkoper

CREATE INDEX ord_lines_si_id_idx ON order_lines (stock_item_id);
CREATE INDEX ord_ord_id_idx ON orders (order_id);

-- S7.3.B
--
-- 1. Vraag het EXPLAIN plan op van je query (kopieer hier, onder de opdracht)
-- 2. Kijk of je met 1 of meer indexen de query zou kunnen versnellen
-- 3. Maak de index(en) aan en run nogmaals het EXPLAIN plan (kopieer weer onder de opdracht)
-- 4. Wat voor verschillen zie je? Verklaar hieronder.



-- S7.3.C
--
-- Zou je de query ook heel anders kunnen schrijven om hem te versnellen?


