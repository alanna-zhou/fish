package fish;

import java.util.ArrayList;
import java.util.HashMap;

public class Fish {

	Deck deck;
	String[] teamNames = { "Nemo", "Dory" };
	HashMap<String, Player> nameToPlayerMap;

	/**
	 * Invariant: players must be an array of size 6.
	 * 
	 * @param players
	 */
	Fish(String[] playerNames) {
		deck = new Deck();
		deck.shuffle();
		nameToPlayerMap = new HashMap<String, Player>();
		for (int i = 0; i < deck.cards.size(); i += 9) {
			ArrayList<Card> hand = (ArrayList<Card>) deck.cards.subList(i, i + 8);
			Player player = new Player(playerNames[i % 6], hand, teamNames[nameToPlayerMap.size() % 2]);
			nameToPlayerMap.put(player.name, player);
		}

		System.out.println("# of players should be 6: " + nameToPlayerMap.size());

	}

	public boolean declare(String playerName, ArrayList<Card> playerCards, String teammate1Name,
			ArrayList<Card> teammate1Cards, String teammate2Name, ArrayList<Card> teammate2Cards) {
		Player player = nameToPlayerMap.get(playerName);
		Player teammate1 = nameToPlayerMap.get(teammate1Name);
		Player teammate2 = nameToPlayerMap.get(teammate2Name);
		if (player == null || teammate1 == null || teammate2 == null) {
			// client error
			return false;
		}
		if (playerCards.size() > 9 || teammate1Cards.size() > 9 || teammate2Cards.size() > 9) {
			// client error
			return false;
		}
		ArrayList<Card> potentialSet = new ArrayList<Card>();
		potentialSet.addAll(playerCards);
		potentialSet.addAll(teammate1Cards);
		potentialSet.addAll(teammate2Cards);
		if (!deck.isValidSet(potentialSet)) {
			// player declared an invalid set, point goes to other team
			updateLoseDeclare(player, playerCards, teammate1, teammate1Cards, teammate2, teammate2Cards);
			return false;
		}
		if (!player.hand.containsAll(playerCards) || !teammate1.hand.containsAll(teammate1Cards)
				|| !teammate2.hand.containsAll(teammate2Cards)) {
			updateLoseDeclare(player, playerCards, teammate1, teammate1Cards, teammate2, teammate2Cards);
			return false;
		}
		// must be a successful declaration at this point
		// hands and scores must be updated
		updateWinDeclare(player, playerCards, teammate1, teammate1Cards, teammate2, teammate2Cards);
		return true;
	}

	public void updateLoseDeclare(Player player, ArrayList<Card> playerCards, Player teammate1,
			ArrayList<Card> teammate1Cards, Player teammate2, ArrayList<Card> teammate2Cards) {
		player.hand.removeAll(playerCards);
		teammate1Cards.removeAll(teammate1Cards);
		teammate2Cards.removeAll(teammate2Cards);
		for (Player p : nameToPlayerMap.values()) {
			if (!p.team.equals(player.team)) {
				p.score++;
			}
		}
	}

	public void updateWinDeclare(Player player, ArrayList<Card> playerCards, Player teammate1,
			ArrayList<Card> teammate1Cards, Player teammate2, ArrayList<Card> teammate2Cards) {
		player.hand.removeAll(playerCards);
		teammate1Cards.removeAll(teammate1Cards);
		teammate2Cards.removeAll(teammate2Cards);
		for (Player p : nameToPlayerMap.values()) {
			if (p.team.equals(player.team)) {
				p.score++;
			}
		}
	}
}