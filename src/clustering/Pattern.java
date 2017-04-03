package clustering;


public class Pattern {
	
	static private int indexGenerator = 0;
	
	private String name;
	private Value[] values;
	private int index;

	public Pattern(String name,Value... values){
		this.index = indexGenerator++;
		this.name = name;
		this.values = values;
	}
	
	public Pattern(int index,String name, Value... values){
		this.index = index;
		this.name = name;
		this.values = values;
	}
	
	public String toString(){
		if(values.length == 0)
			return String.format("%s", name);
		String result = String.format("%s:(",name); 
		for(int i = 0; i < values.length; i++){
			String separator = (i == values.length - 1)? " )":", ";
			result += values[i]+separator;
		}
		return result; 
	}
	
	public Value[] getValues(){
		return values;
	}
	
	public int getIndex(){
		return index;
	}
	
	public int getPatternDimension(){
		return values.length;
	}
	
	public Value getValue(int index){
		return values[index];
	}

}
