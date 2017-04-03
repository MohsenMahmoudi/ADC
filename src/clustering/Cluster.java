package clustering;


import java.util.ArrayList;
import java.util.List;

public class Cluster {

	private String name;
	private List<Pattern> patterns;

	public Cluster(String name){
		this.name = name;
		this.patterns = new ArrayList<Pattern>();
	}
	
	public void addPattern(Pattern p){
		patterns.add(p);
	}
	
	public boolean existPattern(Pattern input){
		return patterns.contains(input);
	}
	
	public String toString(){
		String result = String.format("%s:{ ", name);
		for(int i = 0; i < patterns.size(); i++){
			String separator = (i == patterns.size() - 1)? " }":", ";
			result += String.format("%s%s", patterns.get(i),separator);
		}
		return result;
	}
	
	public List<Pattern> getPatterns(){
		return patterns;
	}
	
	public boolean equals(Object o){
		if(o instanceof Cluster)
			return equals((Cluster)o);
		else
			return false;
	}
	
	public boolean equals(Cluster c){
		return name.equals(c.getName());
	}

	public String getName() {
		return name;
	}

}
