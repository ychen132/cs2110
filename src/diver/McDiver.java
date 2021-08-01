package diver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import graph.FindState;
import graph.FleeState;
import graph.Node;
import graph.NodeStatus;
import graph.SewerDiver;

public class McDiver extends SewerDiver {
    public List<Node> coinList;// created a coinList that is used latter for record all coins

    /** Find the ring in as few steps as possible. Once you get there, <br>
     * you must return from this function in order to pick<br>
     * it up. If you continue to move after finding the ring rather <br>
     * than returning, it will not count.<br>
     * If you return from this function while not standing on top of the ring, <br>
     * it will count as a failure.
     *
     * There is no limit to how many steps you can take, but you will receive<br>
     * a score bonus multiplier for finding the ring in fewer steps.
     *
     * At every step, you know only your current tile's ID and the ID of all<br>
     * open neighbor tiles, as well as the distance to the ring at each of <br>
     * these tiles (ignoring walls and obstacles).
     *
     * In order to get information about the current state, use functions<br>
     * currentLocation(), neighbors(), and distanceToRing() in state.<br>
     * You know you are standing on the ring when distanceToRing() is 0.
     *
     * Use function moveTo(long id) in state to move to a neighboring<br>
     * tile by its ID. Doing this will change state to reflect your new position.
     *
     * A suggested first implementation that will always find the ring, but <br>
     * likely won't receive a large bonus multiplier, is a depth-first walk. <br>
     * Some modification is necessary to make the search better, in general. */
    @Override
    public void find(FindState state) {
        // TODO : Find the ring and return.
        // DO NOT WRITE ALL THE CODE HERE. DO NOT MAKE THIS METHOD RECURSIVE.
        // Instead, write your method (it may be recursive) elsewhere, with a
        // good specification, and call it from this one.
        //
        // Working this way provides you with flexibility. For example, write
        // one basic method, which always works. Then, make a method that is a
        // copy of the first one and try to optimize in that second one.
        // If you don't succeed, you can always use the first one.
        //
        // Use this same process on the second method, flee.
        HashSet<Long> visited= new HashSet<>(); // created a set to record visited data
        dfs(state, visited);// run dfs to find coin
    }

    /** Flee --get out of the sewer system before the steps are all used, trying to <br>
     * collect as many coins as possible along the way. McDiver must ALWAYS <br>
     * get out before the steps are all used, and this should be prioritized above<br>
     * collecting coins.
     *
     * You now have access to the entire underlying graph, which can be accessed<br>
     * through FleeState. currentNode() and exit() will return Node objects<br>
     * of interest, and getNodes() will return a collection of all nodes on the graph.
     *
     * You have to get out of the sewer system in the number of steps given by<br>
     * stepToGo(); for each move along an edge, this number is <br>
     * decremented by the weight of the edge taken.
     *
     * Use moveTo(n) to move to a node n that is adjacent to the current node.<br>
     * When n is moved-to, coins on node n are automatically picked up.
     *
     * You must return from this function while standing at the exit. Failing <br>
     * to do so before steps run out or returning from the wrong node will be<br>
     * considered a failed run.
     *
     * Initially, there are enough steps to get from the starting point to the<br>
     * exit using the shortest path, although this will not collect many coins.<br>
     * For this reason, a good starting solution is to use the shortest path to<br>
     * the exit. */
    @Override
    public void flee(FleeState state) {
        // TODO: Get out of the sewer system before the steps are used up.
        // DO NOT WRITE ALL THE CODE HERE. Instead, write your method elsewhere,
        // with a good specification, and call it from this one.
        // findShortestExit(state);
        // trying to get more coins
        findValueExit(state);// run method to find coins and exit

    }

    /** Writing a dfs method to find where the coins at The input includes the current state as well
     * as the visited state ID */
    public void dfs(FindState state, HashSet<Long> visited) {
        if (state.distanceToRing() == 0) { return; }// return if it is found
        long CurrLocation= state.currentLocation();
        visited.add(CurrLocation);
        ArrayList<NodeStatus> SortN= new ArrayList<>();
        for (NodeStatus n : state.neighbors()) {
            if (!visited.contains(n.getId())) {
                SortN.add(n);
            }
        }// Record all the valid neighbors for current state to an arrayList
        Collections.sort(SortN, new Comparator<NodeStatus>() {
            @Override
            public int compare(NodeStatus x, NodeStatus y) {
                return x.getDistanceToRing() < y.getDistanceToRing() ? -1 : 1;
            }
        });// Using comparator to re-order the arraylist so that we run the closest one first
        for (NodeStatus n : SortN) {
            state.moveTo(n.getId());
            dfs(state, visited);
            if (state.distanceToRing() == 0) { return; }
            state.moveTo(CurrLocation);// backtracking
        }

        /*        for (NodeStatus n : state.neighbors()) {
            if (!visited.contains(n.getId())) {
                state.moveTo(n.getId());
                dfs(state, visited);
                if (state.distanceToRing() == 0) { return; }
                state.moveTo(CurrLocation);
        
            } // worked code without improvement.
        }*/
    }

    /** Create a function that finds the shortest path from currentNode to exit Used A6 for the
     * shortest path method. */
    public void findShortestExit(FleeState state) {
        if (state.currentNode().equals(state.exit())) { return; }
        List<Node> shortest= A6.shortest(state.currentNode(), state.exit());
        while (!state.currentNode().equals(state.exit())) {
            for (Node n : shortest) {
                if (state.currentNode().getNeighbors().contains(n)) {
                    state.moveTo(n);
                }
            }
        }
    }

    /** Based on the findShortestExit() Create a function that takes coins along exiting Based on
     * the coins on the graph, find the highest values it can take if possible/not breaking the
     * steps rules move to take that coin */
    public void findValueExit(FleeState state) {
        if (state.currentNode().equals(state.exit())) { return; }
        boolean hasMoved= false;
        SetUpCoinList(state);
        if (coinList.size() == 0) {
            findShortestExit(state);
            return;
        }
        for (int i= 0; i < coinList.size(); i++ ) {
            Node GoVal= coinList.get(i);
            if (CanReach(GoVal, state) == true) {
                List<Node> shortest= A6.shortest(state.currentNode(), GoVal);
                while (!state.currentNode().equals(GoVal)) {
                    for (Node n : shortest) {
                        if (state.currentNode().getNeighbors().contains(n)) {
                            state.moveTo(n);
                        }
                    }
                }
                hasMoved= true;
                break;
            }

        }
        if (hasMoved == true) {
            findValueExit(state);
        } else {
            findShortestExit(state);
        }

    }

    /** Identify if a node is reachable given the current state and steps left */
    public boolean CanReach(Node GoVal, FleeState state) {
        List<Node> shortest= A6.shortest(state.currentNode(), GoVal);
        List<Node> exitPath= A6.shortest(GoVal, state.exit());
        if (state.stepsToGo() < A6.pathSum(exitPath) + A6.pathSum(shortest)) {
            return false;
        } else {
            return true;
        }
    }

    /** Setting up the coin list and sorted with the highest */
    public void SetUpCoinList(FleeState state) { // create a coin List that has all node with coins
        coinList= new ArrayList<>();
        for (Node n : state.allNodes()) {
            if (n.getTile().coins() > 0) {
                coinList.add(n);
            }
        }
        Collections.sort(coinList, new Comparator<Node>() {// Sort the list with all the nodes with
                                                           // highest coin first
            @Override
            public int compare(Node x, Node y) {
                return x.getTile().coins() <= y.getTile().coins() ? 1 : -1;
            }
        });

    }

}
