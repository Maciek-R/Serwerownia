package tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Test {

	HashMap<String, Integer>hashmap;
	
	@org.junit.Test
	public void test() {
		//fail("Not yet implemented");
		
		hashmap = new HashMap<String, Integer>();
		
		hashmap.put("Maciek", 0);
		hashmap.put("Szymon", 5);
		
		System.out.println(hashmap.get("ASD"));
		System.out.println(hashmap.get("Maciek"));
		System.out.println(hashmap.get("Szymon"));
		
		Set set = hashmap.entrySet();
		Iterator it = set.iterator();
		
		while(it.hasNext()){
		//	System.out.println(it.next());
			Map.Entry m = (Map.Entry)it.next();
			System.out.println(m.getKey()+" "+m.getValue());
		}
	}

}
