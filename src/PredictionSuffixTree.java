//Lauren Dube
import java.util.ArrayList;
public class PredictionSuffixTree<G> extends ProbablilityGenerator<G> {
	
	Node<G> root = new Node<G>();
	int L = 3;
	double pMin = 0.1;
	int sampleSet = 0;
	
public void train() {
	for(int i = 1; i <= L; i++){
		for(int k = 0; k <= tokenArray.size() - i; k++) {
			ArrayList<G> curSequence = new ArrayList<G>();
			for(int j = 0; j < i; j++) {
				curSequence.add(tokenArray.get(k+j));
				}
				Node<G> nood = new Node<G>(curSequence);
				root.addNode(nood);	
			}
		}
	sampleSet = tokenArray.size();
	root.pMinElim(sampleSet, pMin);
}

public void print() {
		root.print();
}
}
