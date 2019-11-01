import java.util.ArrayList;

public class Node<G> {
	//Properties
	ArrayList<G> tokenSequence;
	ArrayList<Node<G>> children = new ArrayList<Node<G>>();
	int count = 1;
	
	public Node(ArrayList<G> token) { //Node Constructor
		this.tokenSequence = token;
	}
	public Node() { //Null Constructor
		tokenSequence = new ArrayList<G>(); 
	}
	//Methods
	public boolean addNode(Node<G> node) {
		boolean found = false;
		int i = 0;
		if(tokenSequence.equals(node.tokenSequence)) {
			found = true;
			count++;
		}else if(amISuffix(node)||(tokenSequence.size()==0)) {
				while(!found && i < children.size()) {
					found = children.get(i).addNode(node);
					i++;
				}
			if(!found) {
				children.add(node);
				found = true;
			}
		}
		return found;
	}
	public boolean amISuffix(Node<G> node) {		
		int fromIndex = node.tokenSequence.size() - this.tokenSequence.size();
		if(fromIndex < 0)
			return false;
		int toIndex = node.tokenSequence.size();
		
		return tokenSequence.equals(node.tokenSequence.subList(fromIndex, toIndex));
	}
	public boolean pMinElim(int total, double p) {
		boolean shouldRemove = false;
		float num = (count/(float)(total-(tokenSequence.size()-1)));
		
		if(num <= p && tokenSequence.size() != 0) {
			shouldRemove = true;
			System.out.println(tokenSequence + " :" + num);
		}else {
				for(int i = children.size()-1; i >= 0; i--) {
					if(children.get(i).pMinElim(total, p)) {
						children.remove(children.get(i));
						System.out.println("Removed x_x");
					}
				}
			shouldRemove = false;
		}
		return shouldRemove; 
	}
	public void print() {
		System.out.println(tokenSequence); 
		for(int i = 0; i < children.size(); i++) 
			children.get(i).print(1);
	}
	public void print(int spaces) {
		for(int i = 1; i <= spaces; i++) {
			System.out.print(" ");
		}
		System.out.print("-->");
		System.out.println(tokenSequence);
		for(int j = 0; j < children.size(); j++) 
			children.get(j).print(spaces + 1);
	}
}
