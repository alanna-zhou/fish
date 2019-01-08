package fish;

/**
 * Represents 1 playing card.
 * 
 * Joker = rank, Red/Black = Suit
 *
 */
public class Card {

	public String rank;
	public String suit;

	Card(String r, String s) {
		rank = r;
		suit = s;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Card)) {
			return false;
		}
		Card c = (Card) o;
		return c.rank.equals(rank) && c.suit.equals(suit);
	}

	public String toString() {
		if (rank.equals("Joker")) {
			return suit + " " + rank;
		}
		return rank + " of " + suit;
	}
}