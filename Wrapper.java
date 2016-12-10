public class Wrapper{
	public static void main(String[] args){
		Producer test = new Producer();
		int [][] holder = test.getStandard(4);
		int length = holder.length;
		int count = 0;
		System.out.println("Length: "+length);
		System.out.print("    ");
		for (int i = 0; i<length;i++){
			if (i<=9){
				System.out.print((i)+ "  ");	
			}
			else{
				System.out.print((i)+ " ");
			}
			
		}
		System.out.println("");
		for (int i=0;i<holder.length;i++){
			if (i<=9){
				System.out.print(" "+count+": ");
			}		
			else {
				System.out.print(count+": ");
			}
			for (int j= 0; j<holder[0].length;j++){
				//System.out.print("I = "+ i + " J = " + j + " ");
				System.out.print(holder[i][j] + "  ");		
			}
			System.out.println("");
			count++;
		}
		//System.out.println("");
                Game testgame = new Game();
                testgame.startGame();
                
	}
}