package fish;

import java.util.ArrayList;

public class Player {

	String name;
	ArrayList<Card> hand; // invariant: 0<=size<=9
	String team;
	int score; // invariant: is the same across all teammates and 0 <= score <= 6

	Player(String n, ArrayList<Card> h, String t) {
		name = n;
		hand = h;
		team = t;
		score = 0;
	}

	public String toString() {
		String handToString = "";
		for (Card c : hand) {
			handToString += c.toString() + ", ";
		}

		return "name: " + name + ", team: " + team + ", score: " + score + "\n\t hand: " + handToString;

	}

}