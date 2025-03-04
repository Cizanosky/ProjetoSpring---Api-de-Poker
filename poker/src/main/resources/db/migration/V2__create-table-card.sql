CREATE TABLE TABLE_CARDS (
     id VARCHAR(12) PRIMARY KEY,
     suit VARCHAR(10) NOT NULL,
     rank VARCHAR(10) NOT NULL
);

INSERT INTO TABLE_CARDS (id, suit, rank) VALUES
('AS_HEARTS', 'Hearts', 'Ace'),
('2S_HEARTS', 'Hearts', '2'),
('3S_HEARTS', 'Hearts', '3'),
('4S_HEARTS', 'Hearts', '4'),
('5S_HEARTS', 'Hearts', '5'),
('6S_HEARTS', 'Hearts', '6'),
('7S_HEARTS', 'Hearts', '7'),
('8S_HEARTS', 'Hearts', '8'),
('9S_HEARTS', 'Hearts', '9'),
('10S_HEARTS', 'Hearts', '10'),
('JS_HEARTS', 'Hearts', 'Jack'),
('QS_HEARTS', 'Hearts', 'Queen'),
('KS_HEARTS', 'Hearts', 'King'),
('AS_DIAMONDS', 'Diamonds', 'Ace'),
('2S_DIAMONDS', 'Diamonds', '2'),
('3S_DIAMONDS', 'Diamonds', '3'),
('4S_DIAMONDS', 'Diamonds', '4'),
('5S_DIAMONDS', 'Diamonds', '5'),
('6S_DIAMONDS', 'Diamonds', '6'),
('7S_DIAMONDS', 'Diamonds', '7'),
('8S_DIAMONDS', 'Diamonds', '8'),
('9S_DIAMONDS', 'Diamonds', '9'),
('10S_DIAMONDS', 'Diamonds', '10'),
('JS_DIAMONDS', 'Diamonds', 'Jack'),
('QS_DIAMONDS', 'Diamonds', 'Queen'),
('KS_DIAMONDS', 'Diamonds', 'King'),
('AS_CLUBS', 'Clubs', 'Ace'),
('2S_CLUBS', 'Clubs', '2'),
('3S_CLUBS', 'Clubs', '3'),
('4S_CLUBS', 'Clubs', '4'),
('5S_CLUBS', 'Clubs', '5'),
('6S_CLUBS', 'Clubs', '6'),
('7S_CLUBS', 'Clubs', '7'),
('8S_CLUBS', 'Clubs', '8'),
('9S_CLUBS', 'Clubs', '9'),
('10S_CLUBS', 'Clubs', '10'),
('JS_CLUBS', 'Clubs', 'Jack'),
('QS_CLUBS', 'Clubs', 'Queen'),
('KS_CLUBS', 'Clubs', 'King'),
('AS_SPADES', 'Spades', 'Ace'),
('2S_SPADES', 'Spades', '2'),
('3S_SPADES', 'Spades', '3'),
('4S_SPADES', 'Spades', '4'),
('5S_SPADES', 'Spades', '5'),
('6S_SPADES', 'Spades', '6'),
('7S_SPADES', 'Spades', '7'),
('8S_SPADES', 'Spades', '8'),
('9S_SPADES', 'Spades', '9'),
('10S_SPADES', 'Spades', '10'),
('JS_SPADES', 'Spades', 'Jack'),
('QS_SPADES', 'Spades', 'Queen'),
('KS_SPADES', 'Spades', 'King');
