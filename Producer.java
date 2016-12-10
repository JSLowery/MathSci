public class Producer{

	public int[][] getStandard(int rowSize){
		int count = 0;
		
		int columnSize = (int)Math.floor((rowSize*1.6)+1);
		//System.out.println(columnSize);
		int[][] array = new int[rowSize*columnSize][rowSize*columnSize];
		int rowi = 0;
		for (int i = 0; i<array.length; i++){
			int p = i+1;
			int lastrow = 1;
			int row = 1;
			//rowi = 1;
			count = 0;
			if (p%rowSize== 1)
						rowi++;
			for (int j = 0; j<array[0].length;j++){
				int q = j+1;
				
				int evenFlag = 0;
				
				//System.out.println("Count: "+count + " p-q: " + (Math.abs(p-q)) + " p: "+ p + " q: "+ q);
				
				// default values that will always be 0 in the array
				if (p==q || Math.abs(p-q) >rowSize+1 || p >q ){
					//System.out.println("Hit some default 0s");
					array [i][j] = 0;
				}
				// 
				else if ((p-q ==-1 ) && ( p%rowSize == 0)){
					//System.out.println("I = "+p+" J = "+ q + " I mod rowSize = " + p%rowSize+ " J mod rowSize = " + q%rowSize);
						array[i][j]= 0;
				}
				else if (Math.abs(p-q)== rowSize-1 || Math.abs(p-q)== rowSize+1  ){
					if (Math.abs(p-q) == rowSize+1 && p%rowSize== 0){
						array[i][j]= 0;
						//System.out.println((p-q) + " " + p%rowSize);
					}
					else{
						//System.out.println("row: " + row + " lastrow: " + lastrow + " rowI: " + rowi);
						if (rowi%2 ==0 && Math.abs(p-q)== rowSize-1 && row != lastrow){
							//System.out.println("row is even and p-q = rowsize -1");
							array[i][j]= 1;
						}
						else if (rowi%2 ==1 && Math.abs(p-q)== rowSize+1 && row != lastrow){
							//System.out.println("row is odd and p-q = rowsize +1");
							array[i][j]= 1;
						}
						else{
							//System.out.println("default 0 for p-q == rowSize-1 row: " + row + " lastrow: " + lastrow);
							array[i][j]= 0;
						}
					
					}
				}
				else if (q%rowSize < rowSize && Math.abs(p-q)<rowSize && lastrow != row){
					if ( Math.abs(p-q) != 1 ){
						array [i][j] = 0;
						//System.out.println("Made this one 0");
					}
					else {
						//System.out.println("proto 1");
						array [i][j] = 1;
					}
				}
				// handles the cases like 1-3 or 1-4 but allows for 4-8 on a 16x 16 rowsize 4 graph
				else if ( lastrow == row && p-q < -1 && Math.abs(p-q) != rowSize){
					//System.out.println("lastrow == row and p-q <-1 p: " + p + " q: " + q);
					array[i][j] = 0;
				}
				else{
					//System.out.println("Default 1 assignment");
					array[i][j] = 1;
				}
				if (q%rowSize== 0 && q != 0 && j>=i){
					lastrow = row;
					//System.out.println("Changed row");
					row++;
				}
				// this is the catchall that blocks things like 1-3 moves or 4-6 moves on a 16x16 with 4 width on rows
				// if (q%rowSize < rowSize && Math.abs(p-q)<rowSize){
				// 	if ( Math.abs(p-q) != 1 ){
				// 		array [i][j] = 0;
				// 		System.out.println("Made this one 0");
				// 	}
					
				// }
				
				//if (count <rowSize){
					count++;
					
				//}
			}

		}
		return array;
	}
	// int recJumps(int rowSize){
	// 	if (rowSize == 1)
	// 		return rowSize - 1;
	// }

}