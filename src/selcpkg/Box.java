package selcpkg;

import java.util.ArrayList;

public class Box implements Comparable<Box>{
	private int number;
	private Integer lock;
	private ArrayList<Integer> storedKeys;
	
	public Box(int number, int lock, ArrayList<Integer> storedKeys) {
		super();
		this.number = number;
		this.lock = lock;
		this.storedKeys = storedKeys;
	}

	public int getNumber() {
		return number;
	}

	public int getLock() {
		return lock;
	}

	public ArrayList<Integer> getStoredKeys(){
		return storedKeys;
	}
	
	public int getStoredQty() {
		return storedKeys.size();
	}

	public boolean openAndGet(ArrayList<Integer> keys){
		if(keys.contains(lock)){
			keys.remove(lock);
			lock = -1;
			keys.addAll(storedKeys);
			storedKeys.clear();
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString(){
		return String.format("%2d) lock: %s stores: %s", number, lock, storedKeys);
	}

	@Override
	public int compareTo(Box other) {
		return other.storedKeys.size() - this.storedKeys.size();
	}
}
