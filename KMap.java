import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class InputOutput extends JFrame{
	String input;
	StringBuilder terms,currentTerm;
	Scanner sc;
	Set<Character> totalVariables;
	JFrame frame = new JFrame("Karnaugh Map");;
	public void scan(){
	    input = JOptionPane.showInputDialog("Give the Unminimized function for Minimization(Use LowerCase variable for representing complement bit):-");
        if(input.equals("")){
            JOptionPane.showMessageDialog(null,"You have not entered anything.");
            input = JOptionPane.showInputDialog("Give the Unminimized function for Minimization(Use LowerCase variable for representing complement bit):-");
        }
        currentTerm  = new StringBuilder();
		terms = new StringBuilder();
		totalVariables = new HashSet<Character>();
		for(int i=0;i<input.length();i++){
			if(Character.isLetter(input.charAt(i))){
				currentTerm.append(input.charAt(i));
				totalVariables.add(Character.toUpperCase(input.charAt(i)));
			}
			else if(input.charAt(i)=='+'){
				terms.append(currentTerm + ",");
				currentTerm = new StringBuilder();
			}
		}
		terms.append(currentTerm);
	}
    public void displayMap(boolean map[][] , char alphabets[]){
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		Graphics g = frame.getGraphics();
		int ycor = 200,xcor = 150,side = 50;
		g.clearRect(0,0,1200,1080);
		g.setFont(new Font("Courier", Font.PLAIN, 20));
		g.drawString("Karnaugh Map",600,75);
		g.drawLine(xcor-side/2, ycor-side/2,xcor-side/2-30, ycor-side/2-30);
		int r = map.length;
		int c = map[0].length;
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		if(r==2){
			g.drawString("0",xcor-side/2-20,ycor+5);
			g.drawString("1",xcor-side/2-20,ycor+5+side);
			g.drawString(new Character(alphabets[0]).toString(),xcor-side/2-32,ycor-22);
		}
		else if(r==4){
			g.drawString("00",xcor-side/2-20,ycor+5);
			g.drawString("01",xcor-side/2-20,ycor+5+side);
			g.drawString("11",xcor-side/2-20,ycor+5+2*side);
			g.drawString("10",xcor-side/2-20,ycor+5+3*side);
			g.drawString(new Character(alphabets[0]).toString(),xcor-side/2-47,ycor-22);
			g.drawString(new Character(alphabets[1]).toString(),xcor-side/2-32,ycor-22);
		}
		if(c==2){
			g.drawString("0",xcor-3,ycor-side/2-10);
			g.drawString("1",xcor-3+side,ycor-side/2-10);
			g.drawString(new Character(alphabets[1]).toString(),xcor-side/2-10,ycor-side/2-16);
		}
		else if(c==4){
			g.drawString("00",xcor-6,ycor-side/2-10);
			g.drawString("01",xcor-6+side,ycor-side/2-10);
			g.drawString("11",xcor-6+2*side,ycor-side/2-10);
			g.drawString("10",xcor-6+3*side,ycor-side/2-10);
			if(r==2){
				g.drawString(new Character(alphabets[1]).toString(),xcor-side/2-15,ycor-side/2-16);
				g.drawString(new Character(alphabets[2]).toString(),xcor-side/2,ycor-side/2-16);
			}
			else{
				g.drawString(new Character(alphabets[2]).toString(),xcor-side/2-15,ycor-side/2-16);
				g.drawString(new Character(alphabets[3]).toString(),xcor-side/2,ycor-side/2-16);
			}
		}
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[i].length;j++){
				g.drawRect (xcor-side/2, ycor-side/2, side,side);
				if(map[i][j])
					g.drawString("1",xcor-5,ycor+5);
				else
					g.drawString("0",xcor-5,ycor+5);
				xcor += side;
			}
			ycor += side;
			xcor = 150;
		}
	}
	public void displaySolution(StringBuilder minimizedFunction){
		String minimizedString = minimizedFunction.toString();
		StringBuilder s1 = new StringBuilder();
		for(int i=0;i<minimizedString.length();i++){
			if(i == 0 ){
                if(minimizedString.charAt(0) != ',')
                    s1.append(minimizedString.charAt(i));
				continue;
			}
			if(minimizedString.charAt(i)==','){
				s1.append(' ');
				s1.append('+');
				s1.append(' ');
			}
			else
				s1.append(minimizedString.charAt(i));
		}
		minimizedString = s1.toString();
		JOptionPane.showMessageDialog(null , "Minimized Function:-  " + minimizedString);
	}
}
class KarnaughMap{
	boolean map[][];
	boolean included[][];
	String tempMap[][];
	String term;
	StringBuilder minimizedFunction;
	char alphabets[];
	int numOfVariables;
	ArrayList<String> shorterTerms;
    StringTokenizer tokens;
	public boolean[][] buildMap(Set<Character> totalVariables,StringBuilder terms){
		shorterTerms = new ArrayList<String>();
		numOfVariables = totalVariables.size();
		int numbering[] = new int[(int)Math.pow(2, numOfVariables)];
		if(numOfVariables == 2){
			int seq[] = {0,1,2,3};
			for(int i=0;i<seq.length;i++)
				numbering[i] = seq[i];
		}
		else if(numOfVariables == 3){
			int seq[] = {0,1,3,2,4,5,7,6};
			for(int i=0;i<seq.length;i++)
				numbering[i] = seq[i];
		}
		else if(numOfVariables==4){
			int seq[] = {0,1,3,2,4,5,7,6,12,13,15,14,8,9,11,10};
			for(int i=0;i<seq.length;i++)
				numbering[i] = seq[i];
		}
		map = new boolean[2*(numOfVariables/2)][2*(numOfVariables - numOfVariables/2)];
		tempMap = new String[2*(numOfVariables/2)][2*(numOfVariables - numOfVariables/2)];
		Iterator itr = totalVariables.iterator();
		alphabets = new char[numOfVariables];
		int index = 0;
		while(itr.hasNext()){
			alphabets[index] =((Character)itr.next()).charValue();
			index++;
		}
		index = 0;
		for(int i=0;i<tempMap.length;i++){
			for(int j=0;j<tempMap[i].length;j++){
				tempMap[i][j]  = String.format("%"+numOfVariables+"s", Integer.toBinaryString(numbering[index])).replace(' ', '0');
				index++;
			}
		}
		tokens = new StringTokenizer(terms.toString(),",");
		while(tokens.hasMoreTokens()){
			term = tokens.nextToken();
			if(term.length()<alphabets.length){
				shorterTerms.add(term);
				continue;
			}
			StringBuilder currentTerm = new StringBuilder();
			for(int i=0;i<alphabets.length;i++){
				if(term.indexOf(alphabets[i])>=0){
					currentTerm.append("1");
				}
				else if(term.indexOf(Character.toLowerCase(alphabets[i]))>=0){
					currentTerm.append("0");
				}
			}
			String currentTermString = currentTerm.toString();
			for(int i=0;i<tempMap.length;i++){
				for(int j=0;j<tempMap[i].length;j++){
					if(tempMap[i][j].equals(currentTermString)){
						map[i][j] = true;
					}
				}
			}
		}
        itr = shorterTerms.iterator();
		while(itr.hasNext()){
			String s = itr.next().toString();
			StringBuilder temps = new StringBuilder();
			for(int i=0;i<alphabets.length;i++){
				if(s.indexOf(alphabets[i])>=0)
					temps.append('1');
				else if(s.indexOf(Character.toLowerCase(alphabets[i]))>=0)
					temps.append('0');
				else
					temps.append('N');

			}
			for(int i=0;i<tempMap.length;i++){
				for(int j=0;j<tempMap[i].length;j++){
					int flag = 1;
					for(int k=0;k<alphabets.length;k++){
						if(temps.charAt(k)!='N' && temps.charAt(k)!=tempMap[i][j].charAt(k)){
							flag=0;
							break;
						}
					}
					if(flag==1)
						map[i][j] = true;
				}
			}
		}
		return map;
	}
	public StringBuilder convertToString(int arr[], int k){
        StringBuilder temp = new StringBuilder();
        int combinedGroup;
        if (k != 0)
            combinedGroup = numOfVariables - numOfVariables / 2;
        else
            combinedGroup = numOfVariables / 2;

        int mapMatrix[][] = new int[arr.length][combinedGroup];
        for (int i = 0; i < arr.length; i++){
            for (int j = combinedGroup - 1; j >= 0; j--){
                if (arr[i] % 2 == 1)
                    mapMatrix[i][j] = 1;
                arr[i] = arr[i] / 2;
            }
        }
        boolean flag;
        for (int j = 0; j < combinedGroup; j++){
            int x = mapMatrix[0][j];
            flag = true;
            for (int i = 0; i < arr.length; i++){
                if (mapMatrix[i][j] != x){
                    flag = false;
                    break;
                }
            }
            if (flag == true && x == 0)
                 temp.append((char)(97 + k + j));
            else if (flag == true && x == 1)
                temp.append((char)(65 + k + j));
        }
        return temp;
    }
    public void arrangeOrder(int arr[], int index, int value){
        if (value == 3)
            arr[index] = 2;
        else if (value == 2)
            arr[index] = 3;
        else
            arr[index] = value;
    }
	public StringBuilder minimizeKMap(){
	    boolean flag = true;
		included = new boolean[map.length][map[0].length];
		minimizedFunction = new StringBuilder();
		int rows = map.length;
		int columns = map[0].length;
		int x = 1;
		while(x <= 4){
            int k = 0;
            while(k <= 1 + rows - rows/x){
                int groupableOnes = 0;
                flag = true;
                OUTER_LOOP:
                for(int i = k; i < k + rows/x; i++){
                    for(int j = 0; j < columns; j++){
                        if(included[i%rows][j])
                            groupableOnes++;
                        if(!map[i % rows][j]){
                            flag = false;
                            break OUTER_LOOP;
                        }
                    }
                }
                if(flag && x==1){
                    minimizedFunction.append("1");
                    return minimizedFunction;
                }
                if(flag == true && groupableOnes != (columns * (rows / x))){
                    int rowIndexValue[] = new int[rows / x];
                    for (int i = k; i < k + rows / x; i++){
                        int y = i % rows;
                        arrangeOrder(rowIndexValue, i - k, y);
                    }
                    minimizedFunction.append("," + convertToString(rowIndexValue, 0).toString());
                    for (int i = k; i < k + rows / x; i++)
                        for (int j = 0; j < columns; j++)
                            included[i % rows][j] = true;
                }
                k++;
            }
            k = 0;
            while( k <= 1 + columns - columns / x){
                flag = true;
                int groupableOnes = 0;
                OUTER_LOOP:
                for (int j = k; j < k + columns / x; j++){
                    for (int i = 0; i < rows; i++){
                        if (included[i][j % columns])
                            groupableOnes += 1;
                        if (!map[i][j % columns]){
                            flag = false;
                            break OUTER_LOOP;
                        }
                    }
                }
                if (flag == true && groupableOnes != ((columns / x) * rows)){
                    int columnIndexValue[] = new int[columns / x];
                    for (int j = k; j < k + columns / x; j++){
                        int y = j % columns;
                        arrangeOrder(columnIndexValue, j - k, y);
                    }
                    minimizedFunction.append("," + convertToString(columnIndexValue, numOfVariables/2).toString());

                    for (int j = k; j < k + columns / x; j++)
                        for (int i = 0; i < rows; i++)
                            included[i][j % columns] = true; //change to all including above
                }
                k++;
            }
            x = x*2;
		}
		for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                if (map[i][j] && map[(i + 1) % rows][j]  && map[i][(j + 1) % columns] && map[(i + 1) % rows][(j + 1) % columns] && (!included[i][j] || !included[(i + 1) % rows][j] || !included[i][(j + 1) % columns] || !included[(i + 1) % rows][(j + 1) % columns] )){
                    int rowIndexValue[] = new int[2];
                    int columnIndexValue[] = new int[2];
                    included[i][j] = true;
                    included[(i + 1) % rows][j] = true;
                    included[i][(j + 1) % columns] = true;
                    included[(i + 1) % rows][(j + 1) % columns] = true;
                    arrangeOrder(rowIndexValue, 0, i);
                    arrangeOrder(rowIndexValue, 1, (i + 1) % rows);
                    arrangeOrder(columnIndexValue, 0, j);
                    arrangeOrder(columnIndexValue, 1, (j + 1) % columns);
                    minimizedFunction.append("," + convertToString(rowIndexValue, 0).toString() + convertToString(columnIndexValue, numOfVariables / 2).toString());
                }
            }
        }
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                if (!included[i][j] && map[i][j]){
                    included[i][j] = true;
                    x = i - 1 < 0 ? rows - 1 : i - 1;
                    int y = j - 1 < 0 ? columns - 1 : j - 1;
                    if (map[(i + 1) % rows][j] ){
                        included[(i + 1) % rows][j] = true;
                        int rowIndexValue[] = new int[2];
                        int columnIndexValue[] = new int[1];
                        arrangeOrder(rowIndexValue, 0, i);
                        arrangeOrder(rowIndexValue, 1, (i + 1) % rows);
                        arrangeOrder(columnIndexValue, 0, j);
                        minimizedFunction.append( "," + convertToString(rowIndexValue, 0).toString() + convertToString(columnIndexValue, numOfVariables / 2).toString());
                    }
                    else if (map[i][(j + 1) % columns]){
                        included[i][(j + 1) % columns] = true;
                        int rowIndexValue[] = new int[1];
                        int columnIndexValue[] = new int[2];
                        arrangeOrder(rowIndexValue, 0, i);
                        arrangeOrder(columnIndexValue, 0, j);
                        arrangeOrder(columnIndexValue, 1, (j + 1) % columns);
                        minimizedFunction.append("," + convertToString(rowIndexValue, 0).toString() + convertToString(columnIndexValue, numOfVariables / 2).toString());
                    }
                    else if (map[x][j]){
                        included[x][j] = true;
                        int rowIndexValue[] = new int[2];
                        int columnIndexValue[] = new int[1];
                        arrangeOrder(rowIndexValue, 0, i);
                        arrangeOrder(rowIndexValue, 1, x);
                        arrangeOrder(columnIndexValue, 0, j);
                        minimizedFunction.append("," + convertToString(rowIndexValue, 0).toString() + convertToString(columnIndexValue, numOfVariables / 2).toString());
                    }
                    else if (map[i][y] ){
                        included[i][y] = true;
                        int rowIndexValue[] = new int[1];
                        int columnIndexValue[] = new int[2];
                        arrangeOrder(rowIndexValue, 0, i);
                        arrangeOrder(columnIndexValue, 0, j);
                        arrangeOrder(columnIndexValue, 1, y);
                        minimizedFunction.append("," + convertToString(rowIndexValue, 0).toString() + convertToString(columnIndexValue, numOfVariables / 2).toString());
                    }
                    else{
                        int rowIndexValue[] = new int[1];
                        int columnIndexValue[] = new int[1];
                        arrangeOrder(rowIndexValue, 0, i);
                        arrangeOrder(columnIndexValue, 0, j);
                        minimizedFunction.append("," + convertToString(rowIndexValue, 0).toString() + convertToString(columnIndexValue, numOfVariables / 2).toString());
                    }
                }
            }
        }
        return minimizedFunction;
	}
}
class KMap{

    KMap(){
        while(true){
            Scanner sc = new Scanner(System.in);
            InputOutput io = new InputOutput();
            io.scan();
            Set<Character> totalVariables = io.totalVariables;
            StringBuilder terms = io.terms;
            KarnaughMap obj = new KarnaughMap();
            boolean map[][] = obj.buildMap(totalVariables,terms);
            char alphabets[] = obj.alphabets;
            StringBuilder minimizedFunction = obj.minimizeKMap();
            io.displayMap(map , alphabets );
            try{
			Thread.sleep(2000);
			}catch(Exception e){}
            io.displaySolution(minimizedFunction);
            int ch = JOptionPane.showConfirmDialog(null,"Do you want to continue[Y/N]");
            if(ch == JOptionPane.NO_OPTION)
                break;
		}
	}
	public static void main(String[] args){
		new KMap();
	}
}
