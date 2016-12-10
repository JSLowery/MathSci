/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author RaySSD
 */
public class Game {    
    public void startGame() {
        int numPlayers = 2;
        //currently these graphs are being produced portrait rather than
        //landscape.
        int numColumns = 4;
        int numRows = (int)Math.floor((numColumns*1.6)+1);
        Producer graphProducer = new Producer();
        boolean[][] edges = intToBool(graphProducer.getStandard(numColumns));
        char[] occupied = buildBoard(numRows, numColumns);
        int[] weights = produceInterestingWeights(numRows, numColumns);
        char[] players = createPlayers(numPlayers);
        
        
        
        int playerTurn = 0;
        printBoard(edges, occupied, weights, players, numRows, numColumns);
        while(playsExist(edges, occupied, players)) {
            takeTurnGreedy(edges, occupied, weights, players[playerTurn]);            
            playerTurn = (playerTurn + 1) % numPlayers;
        }
        printBoard(edges, occupied, weights, players, numRows, numColumns);
        printScores(occupied, weights, players);
        
    }
    //Builds a board array that will keep track of which players have claimed
    //which spaces.  Initializes to all zeros.
    char[] buildBoard(int row, int col) {
        char[] board = new char[row*col];
        for(int i=0; i<row*col; i++) {
            board[i] = '0';
        }
        return board;
    }
    
    //randomly fills the board with some player occupation
    //just for testing some colors
    char[] buildTestBoard(int row, int col, int numPlayers) {
        char[] board = new char[row*col];
        for(int i=0; i<row*col; i++) {
            board[i] = (char)('A' + (int)(Math.random()*numPlayers));
        }
        return board;
    }
    
    int[] produceWeights(int row, int col) {
        int[] weights = new int[row*col];
        for(int i=0; i<row*col; i++) {
            weights[i] = 1;
        }
        return weights;
    }
    
    int[] produceInterestingWeights(int row, int col) {
        int[] notBoringWeights = new int[row*col];
        for(int i=0; i<row*col; i++) {
            notBoringWeights[i] = (int)Math.floor(Math.random()*10 + 1);
        }
        return notBoringWeights;
    }
    
    char[] createPlayers(int num) {
        char[] players = new char[num];
        for(int i=0; i<num; i++) {
            players[i] = (char) (i + 'A');
        }
        return players;
    }
    
    boolean boardFilled(char[] board) {
        boolean filled = true;
        for(int i=0; i<board.length; i++) {
            if(board[i] == '0') {
                filled = false;
            }
        }
        return filled;
    }
    
    void printBoard(boolean[][] edges, char[] occupations, int[] weights, char[] players, int rows, int cols) {
        double xMin = -.5, xMax, yMin = -.5, yMax, xWidth, yHeight;
        int canvasHeight = 900, canvasWidth;
        xMax = cols;
        yMax = rows - 0.5;
        
        xWidth = xMax - xMin;
        yHeight = yMax - yMin;
        //ensure ratio is equal, so that circles are actually circular
        canvasWidth = (int) ((canvasHeight/yHeight) * xWidth);
        
        
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.setXscale(xMin, xMax);
        //not yMax first, means y increases downwards
        StdDraw.setYscale(yMax, yMin);
        
        
        
        //this section draws all edges
        for(int i=0; i<rows*cols; i++) {
            for(int j=0; j<rows*cols; j++) {
                if (edges[i][j]) {
                    //if statement checks if adjustment is necessary
                    //since every other row is indented
                    if(I2R(i, cols)%2 == 0){
                        //sinces we're dealing with two vertices, must also check the other one
                        if(I2R(j, cols)%2 == 0) {
                            StdDraw.line(I2C(i, cols) + 0.5, I2R(i, cols), I2C(j, cols) + 0.5, I2R(j, cols));
                        } else {
                            StdDraw.line(I2C(i, cols) + 0.5, I2R(i, cols), I2C(j, cols), I2R(j, cols));
                        }
                    } else {
                        //must check other vertex here as well
                        if(I2R(j, cols)%2 == 0) {
                            StdDraw.line(I2C(i, cols), I2R(i, cols), I2C(j, cols) + 0.5, I2R(j, cols));
                        } else {
                            StdDraw.line(I2C(i, cols), I2R(i, cols), I2C(j, cols), I2R(j, cols));
                        }
                    }
                }
            }
        }
        
        
        //this section draws all vertices and adjusts size according to weight
        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                if(i%2 == 0) {
                    StdDraw.setPenColor(colorForPosition(RC2I(i,j,cols), occupations));
                    StdDraw.filledCircle(j+0.5, i, weights[RC2I(i,j,cols)] * 0.02 + 0.08);
                    StdDraw.setPenColor();
                    StdDraw.circle(j+0.5, i, weights[RC2I(i,j,cols)] * 0.02 + 0.08);
                } else {
                    StdDraw.setPenColor(colorForPosition(RC2I(i,j,cols), occupations));
                    StdDraw.filledCircle(j, i, weights[RC2I(i,j,cols)] * 0.02 + 0.08);
                    StdDraw.setPenColor();
                    StdDraw.circle(j, i, weights[RC2I(i,j,cols)] * 0.02 + 0.08);
                }
            }
        }
    }
    
    //converts integer array to boolean array
    boolean[][] intToBool(int[][] array) {
        int rows = array.length;
        int cols = array[0].length;
        boolean[][] boolarray = new boolean[rows][cols];
        
        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                if(array[i][j] == 0) {
                    boolarray[i][j] = false;
                } else {
                    boolarray[i][j] = true;
                }
            }
        }
        return boolarray;
    }
    
    //row and column to index (zero based)
    int RC2I(int row, int col, int numCols) {
        return row*numCols +col;
    }
    //index to row
    int I2R(int index, int numCols) {
        return index/numCols;
    }
    //index to col
    int I2C(int index, int numCols) {
        return index - (index/numCols)*numCols;
    }
    
    
    //currently, colors are hard coded, could accept an array of colors.
    Color colorForPosition(int index, char[] occupations) {
        switch(occupations[index]) {
            case 'A':
                return Color.RED;
            case 'B':
                return Color.GREEN;
            case 'C':
                return Color.YELLOW;
            case 'D':
                return Color.BLUE;
            default:
                return Color.WHITE;
        }
    }
    
    //returns the maximum value in an array
    int findMaxNode(List available, int[] weights) {
        int max = -1;
        int index = -1;
        for(int i=0; i<available.size(); i++) {
            int node = (int)available.get(i);
            if(weights[node] > max) {
                max = weights[node];
                index = node;
            }
        }
        return index;
    }
    
    //take node with most weight
    boolean takeTurnGreedy(boolean[][] edges, char[] occupied, int[] weights, char player) {
        boolean madeMove = false;
        List available = getAvailable(edges, occupied, player);
        int position = findMaxNode(available, weights);
        if(position >= 0) {
            occupied[position] = player;
            madeMove = true;
        }
        return madeMove;
    }
    
    List getAvailable(boolean[][] edges, char[] occupied, char player) {
        List available = new ArrayList();
        for(int i=0; i<edges.length; i++) {
            if(isValid(edges, occupied, player, i)) {
                available.add(i);
            }
        }
        return available;
    }
    
    boolean isValid(boolean[][] edges, char[] occupied, char player, int node) {
        if(occupied[node] != '0') {
            return false;
        }
        List nodesToCheck = getConnectedNodes(edges, node);
        for(int i=0; i<nodesToCheck.size(); i++) {
            if(occupied[(int)(nodesToCheck.get(i))] == player) {
                return false;
            }
        }
        return true;
    }
    
    //returns a list of all nodes connected to the given node
    List getConnectedNodes(boolean[][] edges, int node) {
        List connectedNodes = new ArrayList();
        for(int i=0; i<edges.length; i++) {
            if(edges[node][i]) {
                connectedNodes.add(i);
            }
            if(edges[i][node]) {
                connectedNodes.add(i);
            }
        }
        return connectedNodes;
    }
    
    boolean playsExist(boolean[][] edges, char[] occupied, char[] players) {
        boolean playsExist = false;
        for(int i=0; i<players.length; i++) {
            if(getAvailable(edges, occupied, players[i]).size() > 0) {
                return true;
            }
        }        
        return playsExist;
    }
    
    void printScores(char[] occupied, int[] weight, char[] players) {
        int[] scores = new int[players.length + 1];
        for(int i=0; i<scores.length; i++) {
            scores[i] = 0;
        }
        for(int i=0; i<occupied.length; i++) {
            if(occupied[i] == '0') {
                scores[scores.length - 1] += weight[i];
            } else {
                for(int j=0; j<players.length; j++) {
                    if(occupied[i] == players[j]) {
                        scores[j] += weight[i];
                    }
                }
            }
        }
        
        for(int i=0; i<players.length; i++) {
            System.out.println("Player  " + players[i] + ": " + scores[i]);
        }
        System.out.println("Unclaimed: " + scores[scores.length - 1]);
    }
}
