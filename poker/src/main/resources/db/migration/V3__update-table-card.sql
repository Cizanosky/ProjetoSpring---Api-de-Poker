ALTER TABLE table_cards ADD COLUMN rank_value INT;

UPDATE table_cards SET rank_value = CASE
    WHEN rank = '2' THEN 2
    WHEN rank = '3' THEN 3
    WHEN rank = '4' THEN 4
    WHEN rank = '5' THEN 5
    WHEN rank = '6' THEN 6
    WHEN rank = '7' THEN 7
    WHEN rank = '8' THEN 8
    WHEN rank = '9' THEN 9
    WHEN rank = '10' THEN 10
    WHEN rank = 'Jack' THEN 11
    WHEN rank = 'Queen' THEN 12
    WHEN rank = 'King' THEN 13
    WHEN rank = 'Ace' THEN 14
END;
