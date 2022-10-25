
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Yavuz_Yilmaz_2019510086 {

	static int GOLD_AMOUNT;
	static int MAX_LEVEL_ALLOWED;
	static int NUMBER_OF_AVAILABLE_PIECES_PER_LEVEL;

	static int dynamicProgrammingApproach(int gold_amount, int gold[], int attack_point[], int lenght, String d_hero[],
			String hero[]) {
		int i, w = 0;
		int K[][] = new int[lenght + 1][gold_amount + 1];

		Boolean[] level = new Boolean[10];
		for (int j = 0; j < level.length; j++) {
			level[j] = false;
		}
		int levell = 0;
		// Build table K[][] in bottom up manner
		for (i = 0; i <= lenght; i++) {

			for (w = 0; w <= gold_amount; w++) {
				if (i != 0)
					levell = find_dynamic_level(i - 1, d_hero, hero);
				if (i == 0 || w == 0)
					K[i][w] = 0;
				else if (gold[i - 1] <= w) {
					K[i][w] = max(attack_point[i - 1] + K[i - 1][w - gold[i - 1]], K[i - 1][w]);

				} else
					K[i][w] = K[i - 1][w];

			}
		}

		return K[lenght][gold_amount];
	}

	public static int[] greedyApproach(String[] hero, String[] piece_type, int[] gold, int[] attack_point,
			int gold_amount, int max_level_allowed, int number_of_available_pieces_per_level, int line_counter) {
		int[] selected_pieces = new int[100];					//array containing indexes of selected parts
		for (int i = 0; i < selected_pieces.length; i++) {
			selected_pieces[i] = -1;
		}
		int level_counter = 1;
		Boolean[] level = new Boolean[10];					//array used for level control
		for (int i = 0; i < level.length; i++) {
			level[i] = false;
		}

		double[] ratio = new double[900];                 //ratio=attack_point/gold
		for (int i = 0; i < line_counter; i++) {
			ratio[i] = (double) attack_point[i] / gold[i];
		}

		int counter = 0;
		for (int i = 0; i < line_counter; i++) {
			int best = find_best_ratio(ratio);
			level_counter = find_level(best);
			if (gold[best] <= gold_amount && best <= (max_level_allowed * 10) - 1
					&& (best % 10) < number_of_available_pieces_per_level && level[level_counter] == false) {
				selected_pieces[counter] = best;
				counter++;
				gold_amount = gold_amount - gold[best];
				level[level_counter] = true;
			}
			ratio[best] = 0;
		}
		return selected_pieces;
	}

	public static int[] randomApproach(String[] hero, String[] piece_type, int[] gold, int[] attack_point,
			int gold_amount, int max_level_allowed, int number_of_available_pieces_per_level, int line_counter) {
		Random rand = new Random();
		int[] selected_pieces = new int[100];
		for (int i = 0; i < selected_pieces.length; i++) {
			selected_pieces[i] = -1;
		}
		int counter = 0;
		int final_counter = 0;
		Boolean[] level = new Boolean[10];
		for (int i = 0; i < level.length; i++) {
			level[i] = false;
		}
		int level_counter = 1;
		Boolean flag = false;
		while (true) {
			int number = rand.nextInt(line_counter);
			level_counter = find_level(number);				//find level for number

			if (gold[number] <= gold_amount && number <= (max_level_allowed * 10) - 1
					&& (number % 10) < number_of_available_pieces_per_level && level[level_counter] == false) {
				selected_pieces[counter] = number;
				counter++;
				gold_amount = gold_amount - gold[number];
				level[level_counter] = true;               
			}

			for (int i = 0; i < line_counter; i++) {
				int level_counter1 = find_level(i);
				if (gold[i] <= gold_amount && level[level_counter1] == false && i <= (max_level_allowed * 10) - 1
						&& (i % 10) < number_of_available_pieces_per_level) {
					final_counter++;
					flag = true;
					if (final_counter > 100) {
						selected_pieces[counter] = i;
						counter++;
						gold_amount = gold_amount - gold[i];
						level[level_counter1] = true;
					}
					break;
				}

			}

			if (flag)
				flag = false;
			else
				break;

		}

		return selected_pieces;

	}

	public static int find_level(int number) { //finding level method
		int level_counter = 1;
		while (number >= 10) {
			number = number - 10;
			level_counter++;
		}

		return level_counter;
	}

	public static int find_best_ratio(double[] ratio) { //finding best ratio for greedy
		int best = -1;
		for (int i = 0; i < ratio.length; i++) {
			if (ratio[i] > best) {
				best = i;
			}
		}

		return best;
	}

	static int max(int a, int b) { //find max for dynamic programming
		return (a > b) ? a : b;
	}

	public static int find_dynamic_level(int number, String d_hero[], String hero[]) {// find level for dynamic programming
		int temp = -1;
		for (int i = 0; i < hero.length; i++) {
			if (d_hero[number].equals(hero[i])) {
				temp = i;
				break;
			}

		}

		int level_counter = 1;
		while (temp >= 10) {
			temp = temp - 10;
			level_counter++;
		}

		return level_counter;

	}

	@SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter gold amount: ");
		GOLD_AMOUNT = sc.nextInt();
		while (!(GOLD_AMOUNT <= 1200 && GOLD_AMOUNT >= 5)) {
			System.out.println("Please try again!!");
			System.out.println("5 <= gold amount <= 1200");
			GOLD_AMOUNT = sc.nextInt();
		}

		System.out.println("Please enter max allowed level: ");
		MAX_LEVEL_ALLOWED = sc.nextInt();
		while (!(MAX_LEVEL_ALLOWED <= 9 && MAX_LEVEL_ALLOWED >= 1)) {
			System.out.println("Please try again!!");
			System.out.println("1 <= maximum allowed level <= 9");
			MAX_LEVEL_ALLOWED = sc.nextInt();
		}

		System.out.println("Please enter number of pieces for each level : ");
		NUMBER_OF_AVAILABLE_PIECES_PER_LEVEL = sc.nextInt();
		while (!(NUMBER_OF_AVAILABLE_PIECES_PER_LEVEL <= 25 && NUMBER_OF_AVAILABLE_PIECES_PER_LEVEL >= 1)) {
			System.out.println("Please try again!!");
			System.out.println("1 <= number of pieces for each level (i.e., piece type) <= 25");
			NUMBER_OF_AVAILABLE_PIECES_PER_LEVEL = sc.nextInt();
		}

		String[] hero = new String[900];
		String[] piece_type = new String[900];
		int[] gold = new int[900];
		int[] attack_point = new int[900];

		int line_counter = 0;
		String line = "";
		String splitBy = ",";
		try {
			// parsing a CSV file into BufferedReader class constructor
			BufferedReader br = new BufferedReader(new FileReader(
					"input_1.csv"));

			boolean first_line = true;
			while ((line = br.readLine()) != null) // returns a Boolean value
			{
				if (first_line == false) {
					String[] inputs = line.split(splitBy); // use comma as separator
					hero[line_counter] = inputs[0];
					piece_type[line_counter] = inputs[1];
					int gold0 = Integer.parseInt(inputs[2]);
					gold[line_counter] = gold0;
					int attack_point0 = Integer.parseInt(inputs[3]);
					attack_point[line_counter] = attack_point0;
					line_counter++;
				} else {
					first_line = false;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int Users_point=0;
		int Computers_point=0;

		System.out.println("-----------------TRIAL #1-----------------");
		System.out.println("Computer's Greedy Approach result");
		
		long startTime = System.nanoTime();
		int[] pieces2 = greedyApproach(hero, piece_type, gold, attack_point, GOLD_AMOUNT, MAX_LEVEL_ALLOWED,
				NUMBER_OF_AVAILABLE_PIECES_PER_LEVEL, line_counter);
		double estimatedTime = (double) (System.nanoTime() - startTime) / 1000000;
		int t = 0;
		while (pieces2[t] != -1) {
			System.out.println(hero[pieces2[t]] +"("+ piece_type[pieces2[t]]+","+gold[pieces2[t]]+","+attack_point[pieces2[t]]+")");
			Computers_point=Computers_point+attack_point[pieces2[t]];
			t++;
		}
		System.out.println();
		System.out.println("Total Attack point for Greedy Approach: "+Computers_point);
		System.out.println("Total Attack point for Computer: "+Computers_point);
		System.out.println("Greedy Time: "+estimatedTime);
		System.out.println();
		
		
		
		
		System.out.println("User's Dynamic Programming result");
		int[] d_gold = new int[gold.length];
		int[] d_attack_point = new int[attack_point.length];
		String[] d_hero = new String[hero.length];

		int counter = 0;
		for (int j = 0; j < attack_point.length; j++) {
			if (j <= (MAX_LEVEL_ALLOWED * 10) - 1 && (j % 10) < NUMBER_OF_AVAILABLE_PIECES_PER_LEVEL) {
				d_gold[counter] = gold[j];
				d_attack_point[counter] = attack_point[j];
				d_hero[counter] = hero[j];

				counter++;
			}

		}

		startTime = System.nanoTime();
		int point=dynamicProgrammingApproach(GOLD_AMOUNT, d_gold, d_attack_point, counter, d_hero, hero);
		estimatedTime = (double) (System.nanoTime() - startTime) / 1000000;
		System.out.println("Total Attack point for Dynamic Programming: "+point);
		Users_point=Users_point+point;
		System.out.println("Total Attack point for User: "+Users_point);
		
		

		System.out.println("Dynamic Programming Time: "+estimatedTime);
		System.out.println();
		
		
		
		System.out.println("-----------------TRIAL #2-----------------");
		System.out.println("Computer's Random Approach result");
		startTime = System.nanoTime();
		int[] pieces = randomApproach(hero, piece_type, gold, attack_point, GOLD_AMOUNT, MAX_LEVEL_ALLOWED,
				NUMBER_OF_AVAILABLE_PIECES_PER_LEVEL, line_counter);
		estimatedTime = (double) (System.nanoTime() - startTime) / 1000000;
		point=0;
		int j = 0;
		while (pieces[j] != -1) {
			System.out.println(hero[pieces[j]] +"("+ piece_type[pieces[j]]+","+gold[pieces[j]]+","+attack_point[pieces[j]]+")");
			point=point+attack_point[pieces[j]];
			Computers_point=Computers_point+attack_point[pieces[j]];
			j++;
		}
		System.out.println();
		System.out.println("Total Attack point for Random Approach: "+point);
		System.out.println("Total Attack point for Computer: "+Computers_point);
		System.out.println("Random Approach Time: "+estimatedTime);
		System.out.println();
		
		
		
		startTime = System.nanoTime();
		point=dynamicProgrammingApproach(GOLD_AMOUNT, d_gold, d_attack_point, counter, d_hero, hero);
		System.out.println("Total Attack point for Dynamic Programming: "+point);
		estimatedTime = (double) (System.nanoTime() - startTime) / 1000000;
		
		Users_point=Users_point+point;
		System.out.println("Total Attack point for User: "+Users_point);
		System.out.println("Dynamic Programming Time: "+estimatedTime);
		System.out.println();

	}

}
