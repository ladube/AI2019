import java.util.ArrayList;
public class MarkovN<G> extends ProbablilityGenerator<G> {
	int orderN = 2; 
	int tokenIndex = 0;
	int seqIndex = 0;
	int check = 0;
	ArrayList<ArrayList<G>> uniqueSequences = new ArrayList<ArrayList<G>>();
	
	//transition table
	ArrayList<ArrayList<Integer>> transitionTableCount = new ArrayList<ArrayList<Integer>>(); // = new ArrayList();
	ArrayList<ArrayList<Float>> transitionTable = new ArrayList<ArrayList<Float>>();
	
	//extend and override train
	public void train() {
		for(int i = 0; i < tokenArray.size(); i++) {
			if(alphabet.contains(tokenArray.get(i)) == false) {
				tokenIndex = alphabet.size();
				alphabet.add(tokenArray.get(i));
			}
		}
		for(int i = orderN - 1; i< tokenArray.size(); i++)
			sequence(i);
	}
	private void sequence(int i) {
		ArrayList<G> curSequence = new ArrayList<G>();
		for(int j = orderN - 1; j >= 0; j--)
			curSequence.add(tokenArray.get(i - j));
		if(uniqueSequences.contains(curSequence) == false) {
			uniqueSequences.add(curSequence);
			seqIndex = uniqueSequences.size()-1;
			expandTable();
			int currCharIndex = alphabet.indexOf(tokenArray.get(i));
			transitionTableCount.get(seqIndex).set(currCharIndex, 1);
		}else {
			seqIndex = uniqueSequences.indexOf(curSequence);
			int charIndex = alphabet.indexOf(tokenArray.get(i));
			int tmp = transitionTableCount.get(seqIndex).get(charIndex);
			tmp++;
			transitionTableCount.get(seqIndex).set(charIndex, tmp);
		}
	}
	private void expandTable() { 
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		ArrayList<Float> tmpFloat = new ArrayList<Float>();
		for(int j = 0; j < alphabet.size(); j++) {
			tmp.add(0);
			tmpFloat.add((float) 0);
		}
			transitionTableCount.add(tmp);		
			transitionTable.add(tmpFloat);
	}
//	private void expandTableY() {
//		for(int i = 0; i < transitionTable.size(); i++) {
//			transitionTableCount.get(i).add(0);
//			transitionTable.get(i).add((float) 0);
//		}
// }
	public void normalize() {
		for(int i = 0; i < transitionTableCount.size(); i++) {
			int sum = 0;
			for(int j = 0; j < transitionTableCount.get(i).size(); j++)
				sum += transitionTableCount.get(i).get(j);
			for(int j = 0; j < transitionTableCount.get(i).size(); j++) {
				float val = transitionTableCount.get(i).get(j);
				if(sum == 0)
					val = 0;
				else
					val /= sum;
				transitionTable.get(i).set(j,val);
			}
		}
	}
	public void print(){ 
		for(int i = 0; i < transitionTableCount.size(); i++) {
			System.out.print("[");
			for(int j = 0; j < transitionTableCount.get(i).size(); j++) {
				System.out.print(transitionTableCount.get(i).get(j) + ", ");
			}
			System.out.println("]");
		}
		System.out.println("normalized");
		for(int i = 0; i < transitionTable.size(); i++) {
			System.out.print("[");
			for(int j = 0; j < transitionTable.get(i).size(); j++) {
				System.out.print(transitionTable.get(i).get(j) + ", ");
			}
			System.out.println("]");
		}
	}
	void generate(G initToken) {
		//clear the generated array if full already
		genarray.clear();
		for(int i = orderN - 1; i < tokenArray.size(); i++) {
			tokenIndex = alphabet.indexOf(initToken);
			G genToken = generateToken(tokenIndex);
			genarray.add(genToken);
			initToken = genToken;
		}
	}
	public G generateToken(int i) {//determine the generated token based off of transition table
		float randIndex = (float)Math.random();
		boolean found = false;
		int index = 0;
		float total = 0;
	
		while(!found && index < alphabet.size()) {
			total += transitionTable.get(i).get(index);
			found = randIndex <= total;
			index++;
		}
		if(found) return (G) alphabet.get(index-1);
		else return (G) alphabet.get(alphabet.size()-1);
	}
}
