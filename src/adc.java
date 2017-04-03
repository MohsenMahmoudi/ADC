import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import clustering.Cluster;
import clustering.Pattern;
import clustering.Value;

public class adc {

	public static final String FILENAME = "No-show-Issue-Comma-50.csv";

	static final int K = 10;

	static ArrayList<Pattern> input = new ArrayList<>();
	static int[][] nearestMatrix;

	public static void main(String[] args) {

		BufferedReader br = null;
		FileReader fr = null;

		try {

			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(FILENAME));
			System.out.println("Header of cols:\n" + br.readLine());
			int index = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				String[] splitedStrings = sCurrentLine.split(",");
				Pattern tmp = new Pattern(index++, "Pattern#" + index, new Value(true).setValue(splitedStrings[0]),
						new Value(false).setValue(splitedStrings[1]), new Value(false).setValue(splitedStrings[6]),
						new Value(false).setValue(splitedStrings[7]), new Value(false).setValue(splitedStrings[8]),
						new Value(false).setValue(splitedStrings[9]), new Value(false).setValue(splitedStrings[10]),
						new Value(false).setValue(splitedStrings[11]), new Value(false).setValue(splitedStrings[12]),
						new Value(false).setValue(splitedStrings[13]), new Value(false).setValue(splitedStrings[14]));
				input.add(tmp);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		double[][] adjacencyMatrix = new double[input.size()][input.size()];
		System.out.println("Step#1:adjacency Matrix");
		for (int i = 0; i < input.size(); i++) {
			for (int j = 0; j < input.size(); j++) {
				adjacencyMatrix[i][j] = i == j ? Double.MAX_VALUE : dist(input.get(i), input.get(j));
			}
		}
		System.out.println("Step#2:nearest Matrix");
		nearestMatrix = nearestNeighborsAnalyze(adjacencyMatrix, input.size(), input.size());

		
		System.out.println("Step#3:common nearest neighbors’ matrix");
		int[][] CNNM = new int[input.size()][input.size()];
		for (int i = 0; i < input.size(); i++) {
			int MaxValue=Integer.MIN_VALUE;
			int MaxValueNO=-1;
			for (int j = i; j < input.size(); j++) {
				CNNM[i][j] = i == j ? 0 : CNN(i, j, K, input.size());
				if(MaxValue<CNNM[i][j]){
					MaxValue=CNNM[i][j];
					MaxValueNO=j;
				}
			}
			CNNM[i][0]=MaxValueNO;
		}
		
		ArrayList<Cluster> clusters = new ArrayList<>();
		int lastClusterIndex=0;
		for (int i = 0; i < input.size(); i++) {
			int foundedClusterIndex=-1;
			for (int clusterIndex = 0; clusterIndex < clusters.size(); clusterIndex++) {
				if(clusters.get(clusterIndex).existPattern(input.get(CNNM[i][0])))
				{
					foundedClusterIndex=clusterIndex;
					clusters.get(foundedClusterIndex).addPattern(input.get(i));
					break;
				}
				if(clusters.get(clusterIndex).existPattern(input.get(i)))
				{
					foundedClusterIndex=clusterIndex;
					clusters.get(foundedClusterIndex).addPattern(input.get(CNNM[i][0]));
					break;
				}
			}
			if(foundedClusterIndex<0){
				Cluster tmp = new Cluster("Cluster#"+lastClusterIndex++);
				tmp.addPattern(input.get(i));
				tmp.addPattern(input.get(CNNM[i][0]));
				clusters.add(tmp);
			}
		}
		
		for (Cluster cluster : clusters) {
			System.out.println(cluster);
		}
	}

	private static int CNN(int Xi, int Xj, int k, int height) {
		int count = 0;
		for (int i = 0; i < k; i++) {
			for (int j = i; j < k; j++) {
				if (nearestMatrix[Xi][i] == nearestMatrix[Xj][j])
					count++;
			}
		}
		return count;
	}

	private static int[][] nearestNeighborsAnalyze(double[][] adjacencyMatrix, int height, int width) {
		int[][] output = new int[height][width];

		for (int i = 0; i < output.length; i++) {
			for (int j = 0; j < output.length; j++) {
				double minValue = Double.MAX_VALUE;
				int minValueNO = -1;
				for (int n = 0; n < output.length; n++) {
					if (adjacencyMatrix[i][n] < minValue) {
						minValue = adjacencyMatrix[i][n];
						minValueNO = n;
					}
				}
				if (minValueNO != -1) {
					adjacencyMatrix[i][minValueNO] = Double.MAX_VALUE;
					output[i][j] = minValueNO;
				} else {
					adjacencyMatrix[i][j] = Double.MAX_VALUE;
					output[i][j] = j;
				}
			}
		}
		return output;
	}

	public static double dist(Pattern a, Pattern b) {

		double firstSum = 0;
		int secondSum = 0;

		for (int i = 0; i < a.getValues().length; i++) {
			if (!(a.getValue(i) == null || b.getValue(i) == null)) {
				secondSum++;
				if (!a.getValue(i).isNumeric())
					firstSum += bin_dist((String) a.getValue(i).getValue(), (String) b.getValue(i).getValue());
				else
					firstSum += Cont_dist((Double) a.getValue(i).getValue(), (Double) b.getValue(i).getValue(), i);
			}
		}
		return firstSum / secondSum;
	}

	private static double Cont_dist(Double value1, Double value2, int i) {
		double minValue = Double.MAX_VALUE;
		double maxValue = Double.MIN_VALUE;

		for (Pattern pattern : input) {
			double value = (double) pattern.getValue(i).getValue();
			maxValue = value > maxValue ? value : maxValue;
			minValue = value < minValue ? value : minValue;
		}
		return Math.abs(value1 - value2) / (maxValue - minValue);
	}

	private static double bin_dist(String value, String value2) {
		return value.equals(value2) ? 1 : 0;
	}
}
