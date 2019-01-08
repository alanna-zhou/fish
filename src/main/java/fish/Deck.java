package fish;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

	public ArrayList<Card> cards;
	String[] suits = { "Clubs", "Spades", "Hearts", "Diamonds" };
	String[] upperRanks = { "9", "10", "Jack", "Queen", "King", "Ace" };

	Deck() {
		cards = new ArrayList<Card>();

		for (String rank : suits) {
			// 2 - 10 clubs, spades, hearts, diamonds = 36 total
			for (int i = 2; i <= 10; i++) {
				cards.add(new Card(Integer.toString(i), rank));
			}
			// j, q, k, a clubs, spades, hearts, diamonds = 16 total
			cards.add(new Card("Jack", rank));
			cards.add(new Card("Queen", rank));
			cards.add(new Card("King", rank));
			cards.add(new Card("Ace", rank));
		}
		// red joker, black joker = 2 total
		cards.add(new Card("Joker", "Red"));
		cards.add(new Card("Joker", "Black"));
	}

	public void shuffle() {
		Collections.shuffle(cards);
	}

	public boolean isValidSet(ArrayList<Card> cards) {
		for (String suit : suits) {
			// 2-7 clubs, spades, hearts, diamonds = 4 sets
			if (cards.contains(new Card("2", suit))) {
				for (int i = 3; i <= 7; i++) {
					if (!cards.contains(new Card(Integer.toString(i), suit))) {
						return false;
					}
				}
			}
			// 9-a clubs, spades, hearts, diamonds = 4 sets
			if (cards.contains(new Card("9", suit))) {
				for (String rank : upperRanks) {
					if (!cards.contains(new Card(rank, suit))) {
						return false;
					}
				}
			}

		}
		// 8 clubs, spades, hearts, diamonds + 2 jokers = 1 set
		if (cards.contains(new Card("8", "Clubs"))) {
			for (String suit : suits) {
				if (!cards.contains(new Card("8", suit))) {
					return false;
				}
			}
			if (!cards.contains(new Card("Joker", "Red")) || !cards.contains(new Card("Joker", "Black"))) {
				return false;
			}

		}
		return true;
	}

}