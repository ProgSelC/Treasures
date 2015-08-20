package selcpkg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
	private static boolean debug = false;

	public static void main(String[] args) {
		String filename = "chestsAndTreasure.txt";
		for (int i = 0; i < 9; i++) {
			processTask(filename, i);
			System.out.println();
		}

	}

	public static void processTask(String filename, int tasknum) {
		ArrayList<Integer> keys = new ArrayList<>();
		ArrayList<Box> boxes = new ArrayList<>();
		boolean readBegin = (tasknum == 0) ? true : false;
		int sepnum = 0;
		int keysQty = 0;
		int boxesQty = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			int i = 1;
			String str = "";

			while ((str = br.readLine()) != null) {
				if (readBegin && !str.matches("^\\s*$")) {
					switch (i) {
					case 1:
						keysQty = Integer.parseInt(str.split("\\s+")[0]);
						boxesQty = Integer.parseInt(str.split("\\s+")[1]);
						break;
					case 2:
						for (int j = 0; j < keysQty; j++) {
							keys.add(Integer.parseInt(str.split("\\s+")[j]));
						}
						break;
					default:
						int k = 1;
						int lock = 0;
						ArrayList<Integer> storedKeys = new ArrayList<>();
						for (String v : str.split("\\s+")) {
							switch (k) {
							case 1:
								lock = Integer.parseInt(v);
								break;
							case 2:
								break;
							default:
								storedKeys.add(Integer.parseInt(v));
							}
							k++;
						}
						boxes.add(new Box(i - 2, lock, storedKeys));
					}

					if (++i >= boxesQty + 3)
						break;

				} else if (str.matches("^\\s*$")) {
					if (tasknum == ++sepnum)
						readBegin = true;
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading file!");
		}

		if (keys.isEmpty() || boxes.isEmpty()) {
			System.out
					.println("Error intializing task from file! Check task number!");
		} else {
			System.out.println("Task N" + (tasknum + 1) + " initial data:");
			System.out.println("Keys: " + keys);
			System.out.println("Boxes:");

			for (Box bx : boxes) {
				System.out.println(bx);
			}

			System.out.println("The answer is:");
			System.out.println(findOrder(keys, boxes));
		}
	}

	public static String findOrder(ArrayList<Integer> keys, ArrayList<Box> boxes) {
		String answer = "";

		while (true) {
			int k = (keys.isEmpty()) ? 0 : keys.get(0);

			ArrayList<Box> openable = findOpenable(boxes, k);
			ArrayList<Integer> uselessKeys = new ArrayList<>();

			// debug block
			if (debug) {
				System.out.println("key " + k + " openable boxes:");
				for (Box bx : openable) {
					System.out.println(bx);
				}
			}
			// ------------

			if (openable.size() == 0) {
				if (keys.size() == 0) {
					answer = "There is no resolution!";
					System.out.println("Boxes which left locked: ");
					for (Box bx : boxes) {
						System.out.println(bx);
					}
					break;
				} else {
					uselessKeys.add(k);
					keys.removeAll(uselessKeys);
					k = 0;
				}
			} else if (openable.size() != 0) {
				for (Box bx : openable) {
					if (bx.getStoredKeys().contains(k)) {
						if (bx.openAndGet(keys)) {
							boxes.remove(bx);
							k = 0;
							answer += "->" + bx.getNumber();
							// debug block
							if (debug) {
								System.out.println("Box opened: "
										+ bx.getNumber());
								System.out.println(keys);
							}
							// ------------
							break;
						}
					}
				}
				if (k != 0) {
					Box bx = findBest(openable, boxes);
					if (bx.openAndGet(keys)) {
						boxes.remove(bx);
						k = 0;
						answer += "->" + bx.getNumber();
						// debug block
						if (debug) {
							System.out.println("Box opened: " + bx.getNumber());
							System.out.println(keys);
						}
						// ------------
					}
				}
			}
			if (boxes.size() == 0) {
				break;
			}
		}
		return answer;
	}

	public static ArrayList<Box> findOpenable(ArrayList<Box> boxes, int key) {
		ArrayList<Box> openable = new ArrayList<Box>();
		for (Box bx : boxes) {
			if (bx.getLock() == key) {
				openable.add(bx);
			}
		}
		Collections.sort(openable);

		return openable;
	}

	public static Box findBest(ArrayList<Box> openable, ArrayList<Box> boxes) {
		int maxRate = 0;
		Box bestBox = openable.get(0);
		for (Box op : openable) {
			int rate = 0;
			for (int key : op.getStoredKeys()) {
				ArrayList<Box> list = findOpenable(boxes, key);
				if (!list.isEmpty()) {
					rate += list.get(0).getStoredQty();
				}
			}
			if (maxRate < rate) {
				maxRate = rate;
				bestBox = op;
			}
		}
		return bestBox;
	}
}
