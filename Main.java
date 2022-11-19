import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Main {
  static String[] symbols = { "♣", "❤", "♠", "♦" };
  static String[] cards = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "K", "Q", "J" };
  static ArrayList<String> deck;
  static ArrayList<String> playerCards = new ArrayList<>();
  static ArrayList<String> dealerCards = new ArrayList<>();
  static Random rand = new Random();
  static Scanner input = new Scanner(System.in);

  static int wins, losses;

  static void resetDeck() {
    ArrayList<String> newList = new ArrayList<>();
    for (String card : cards) {
      for (int i = 0; i < 4; i++) {
        newList.add(card + symbols[i]);
      }
    }
    deck = newList;
  }

  static String randomCard() {
    if (deck.size() < 1)
      resetDeck();
    int index = rand.nextInt(deck.size());
    String card = deck.get(index);
    deck.remove(index);
    return card;
  }

  static int cardsNumberValue(Iterable<String> cards, boolean inside) {
    int total = 0;
    boolean ace = false;
    for (String card : cards) {
      String cardValue = card.substring(0, card.length() - 1);
      if (cardValue.equals("J") || cardValue.equals("K") || cardValue.equals("Q")) {
        total += 10;
      } else if (cardValue.equals("A")) {
        // Check whether the ace should be a 1 or 11
        if (!inside) {
          if (ace || cardsNumberValue(cards, true) > 21)
            total++;
          else
            total += 11;
        } else {
          if (ace)
            total++;
          else
            total += 11;
        }
        ace = true;
      } else {
        total += Integer.parseInt(cardValue);
      }
    }
    return total;
  }

  static void stand() {
    int dealer = cardsNumberValue(dealerCards, false);
    int player = cardsNumberValue(playerCards, false);
    System.out.printf("Dealer's cards: %s (%s)%n", String.join(", ", dealerCards), dealer);
    while (dealer < 17) {
      dealerCards.add(randomCard());
      dealer = cardsNumberValue(dealerCards, false);
      try {
        Thread.sleep(1200);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      System.out.printf("Dealer's cards: %s (%s)%n", String.join(", ", dealerCards), dealer);
    }
    System.out.printf("Your cards: %s (%s)%n%n", String.join(", ", playerCards), player);
    if (dealer > 21) {
      System.out.println("The dealer busted. You win!");
      wins++;
    } else if (dealer == player) {
      System.out.println("It's a push!");
    } else if (dealer < player) {
      if (player == 21)
        System.out.println("You got a Blackjack. You won!");
      else
        System.out.println("You won!");
      wins++;
    } else {
      if (dealer == 21)
        System.out.println("The dealer got a Blackjack. You lost!");
      else
        System.out.println("You lost!");
      losses++;
    }
    System.out.printf("Win(s): %s, Loss(es): %s%n", wins, losses);
  }

  static void hit() {
    playerCards.add(randomCard());
    int player = cardsNumberValue(playerCards, false);
    System.out.printf("Your cards: %s (%s)%nDealer's cards: %s (%s)%n%n", String.join(", ", playerCards),
        player, String.join(", ", dealerCards.subList(0, 1)),
        cardsNumberValue(dealerCards.subList(0, 1), false));
    if (player > 21) {
      System.out.println("You busted!");
      losses++;
      System.out.printf("Win(s): %s, Loss(es): %s%n", wins, losses);
      return;
    }
    System.out.print("Hit or stand: ");
    String hitOrStand = input.nextLine().toLowerCase();
    if (!hitOrStand.equals("") && hitOrStand.charAt(0) == 'h') {
      hit();
      System.out.println();
    } else {
      stand();
    }
  }

  static void startGame() {
    System.out.println();
    dealerCards.clear();
    playerCards.clear();
    dealerCards.add(randomCard());
    dealerCards.add(randomCard());
    playerCards.add(randomCard());
    playerCards.add(randomCard());
    int player = cardsNumberValue(playerCards, false);
    String cards = String.format("Your cards: %s (%s)%nDealer's cards: %s (%s)%n", String.join(", ", playerCards),
        player, String.join(", ", dealerCards), cardsNumberValue(dealerCards, false));
    if (cardsNumberValue(dealerCards, false) == 21 && player != 21) {
      System.out.println(cards);
      System.out.println("The dealer got a Blackjack. You lost!");
      losses++;
      System.out.printf("Win(s): %s, Loss(es): %s%n", wins, losses);
      return;
    } else if (cardsNumberValue(dealerCards, false) == 21 && player == 21) {
      System.out.println(cards);
      System.out.println("It's a push!");
      System.out.printf("Win(s): %s, Loss(es): %s%n", wins, losses);
      return;
    }
    System.out.printf("Your cards: %s (%s)%nDealer's cards: %s (%s)%n%n", String.join(", ", playerCards),
        player, String.join(", ", dealerCards.subList(0, 1)),
        cardsNumberValue(dealerCards.subList(0, 1), false));
    System.out.print("Hit or stand: ");
    String hitOrStand = input.nextLine().toLowerCase();
    if (!hitOrStand.equals("") && hitOrStand.charAt(0) == 'h')
      hit();
    else
      stand();
  }

  public static void main(String[] args) {
    System.out.println("---------------------");
    System.out.println("Welcome to Blackjack!");
    System.out.println("---------------------\n");
    System.out.println("Press CTRL+C to exit the game");
    resetDeck();
    int games = 0;
    while (true) {
      if (games++ > 0) {
        System.out.print("Play again? (Y/N): ");
        String yOrN = input.nextLine().toLowerCase();
        if (!yOrN.equals("") && yOrN.charAt(0) == 'n')
          break;
        else
          System.out.println("\n--------------------");
      }
      startGame();
    }
  }
}
