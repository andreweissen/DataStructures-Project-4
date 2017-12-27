/**
 * DirectedGraph.java - Constructs the directed graph for classes
 * Begun 12/04/17
 * @author Andrew Eissen
 */

package commandlinecompiler;

import java.util.*;

/**
 * Main GUI class, generic
 */
final class DirectedGraph<T> {

    /**
     * Note:
     * As denoted in the Project 4 Indications document supplied the week of the assignment, the two
     * required structures have been included to accommodate generic vertex types.
     */

    /**
     * As per the rubric, the author switched the key/value designation of the <code>HashMap</code>,
     * as the use of <code>Integer</code>s as keys and generics as values made sense in light of
     * Figure 4 in the project rubric. This was in response to the framework supplied in the P4
     * Indications document mentioned above.
     */
    private final HashMap<Integer, T> mapToInteger;

    /**
     * As denoted on page 2 of the rubric, the <code>ArrayList</code> of <code>LinkedList</code>s
     * of <code>Integer</code>s has been implemented as the second required structure.
     */
    private final ArrayList<LinkedList<Integer>> adjacentVertices;

    /**
     * <code>counter</code> is used to denote the integer index of the vertex in question, as well
     * as denote the general number of vertices present, as seen in <code>inTopologicalOrder</code>.
     */
    private int counter;

    /**
     * Default constructor
     */
    public DirectedGraph() {
        this.mapToInteger = new HashMap<>();
        this.adjacentVertices = new ArrayList<>();
        this.setCounter(0);
    }

    /**
     * Setter for <code>counter</code>
     * @param counter
     * @return void
     */
    private void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * One of the four methods required of the digraph class by the Project 4 Indications, the
     * <code>addVertex</code> method handles the addition of generic vertex nodes to the main
     * <code>HashMap</code>, and also adds an associated <code>LinkedList</code> to the adjacency
     * <code>ArrayList</code>. The counter, indicating the number of vertices present, is
     * incremented at the end.
     * @param vertex
     * @return void
     */
    private void addVertex(T vertex) {
        if (!this.mapToInteger.containsValue(vertex)) {
            this.mapToInteger.put(this.counter, vertex);
            this.adjacentVertices.add(new LinkedList<>());
            this.counter++;
        }
    }

    /**
     * Another of the four required methods, the <code>addEdge</code> method establishes the linked
     * relationship between vertex nodes, adding them to their associated <code>LinkedList</code>.
     * The <code>getKey</code> utility method is used to acquire the key of generic vertex values.
     * @param fromVertex
     * @param toVertex
     * @return void
     */
    private void addEdge(T fromVertex, T toVertex) {
        this.adjacentVertices.get(this.getKey(fromVertex)).add(this.getKey(toVertex));
    }

    /**
     * This utility method is used to determine the key of a <code>HashMap</code> entry given the
     * value. It was modified from an answer given on StackOverflow. See the following:
     * https://stackoverflow.com/questions/1383797/java-hashmap-how-to-get-key-from-value
     * @param value
     * @return Integer
     */
    private Integer getKey(T value) {
        for (Map.Entry<Integer, T> entry : this.mapToInteger.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * This is an overloaded helper method of one of the required methods. This utility method takes
     * several more parameters and is called in a recursive manner over all adjacent vertices. It
     * was modified slightly from this source: http://www.geeksforgeeks.org/topological-sorting/
     * @param index
     * @param visited
     * @param stack
     * @return void
     * @throws CycleOccurrenceException
     */
    private void inTopologicalOrder(int index, boolean[] visited, Stack<T> stack)
            throws CycleOccurrenceException {

        visited[index] = true;
        Iterator<Integer> iterator = this.adjacentVertices.get(index).iterator();

        // Iterates over the the associated <code>LinkedList</code>
        while (iterator.hasNext()) {
            Integer i = iterator.next();
            if (!visited[i]) {
                this.inTopologicalOrder(i, visited, stack);
            } else {
                throw new CycleOccurrenceException("Cycle detected");
            }
        }

        // We grab the value of the integer index in question and move it into the stack
        stack.push(this.mapToInteger.get(index));
    }

    /**
     * One of the expected methods, <code>inTopologicalOrder</code> sorts the nodes according a
     * topological algorithm. This method is called from the body of <code>GUIMouseAdapter</code>
     * and throws the two required user-defined exceptions to be handled by the associated
     * <code>compileInTopologicalOrder</code> method in the adapter class body. As such, it is set
     * to <code>Public</code> access levels.
     * <br />
     * <br />
     * This method was modified slightly from the source in question, as with the method above:
     * http://www.geeksforgeeks.org/topological-sorting/
     * @param vertex
     * @return ArrayList<T>
     * @throws InvalidClassNameException
     * @throws CycleOccurrenceException
     */
    public ArrayList<T> inTopologicalOrder(T vertex)
            throws InvalidClassNameException, CycleOccurrenceException {

        // Store the values
        ArrayList<T> classContents = new ArrayList<>();

        /**
         * Per the Week 7 Graph Module reading, the use of a <code>Stack</code> is recommended
         * for such topological sort attempts. This one accepts generic vertices.
         */
        Stack<T> stack = new Stack<>();

        // Key for the inputted value
        Integer vertexIndex;

        /**
         * A boolean array the size of the accepted node count is used as a pseudo-object property.
         * It is interesting to note that initially the author used a <code>Vertex</code> subclass
         * similar to the <code>Node</code> classes used in previous assignments with certain
         * properties. However, that endlessly complicated matters and this current implementation
         * seemed more optimized overall.
         */
        boolean[] visitedVertices = new boolean[this.counter];

        /**
         * As per the project design rubric, a user-created exception related to a case of incorrect
         * class input is thrown here. If the <code>HashMap</code> does contain an entry with the
         * proper value, the value of <code>vertexIndex</code> is set to the key.
         */
        if (this.mapToInteger.containsValue(vertex)) {
            vertexIndex = this.getKey(vertex);
        } else {
            throw new InvalidClassNameException("No such class");
        }

        /**
         * Set the visited state of all vertices to <code>false</code>. Originally was a utility
         * method that was simply added to the main method as it was only being called once anyway.
         * It saw more use during the aforementioned <code>Vertex</code> subclass era.
         */
        for (int i = 0; i < this.counter; i++) {
            visitedVertices[i] = false;
        }

        /**
         * Recursively employs the overloaded recursive version of this method to achieve the
         * desired sort for adjacent vertices.
         */
        if (!visitedVertices[vertexIndex]) {
            this.inTopologicalOrder(vertexIndex, visitedVertices, stack);
        }

        // Pops Stack elements and moves them into the ArrayList used for storing vertices
        while (!stack.isEmpty()) {
            classContents.add(stack.pop());
        }

        // Returns ArrayList of generic vertex values
        return classContents;
    }

    /**
     * The last of the required methods as outlined in the Project 4 Indications. This method builds
     * the directed graph itself from the inputted <code>ArrayList</code> of generic type arrays. As
     * discussed in the appropriate click handler in the <code>P4GUI</code>, the decision to input
     * generic arrays was done to preserve the generic nature of the class. It made more sense (in
     * terms of code division) to have the GUI class handle <code>String</code>s and this class to
     * handle generics.
     * <br />
     * <br />
     * The use of two nested <code>for</code> loops may not have been the best choice or the most
     * optimized, but the author could not think of how best to preserve the adjacency relationship
     * while preserving the file's organizational structure. Iterating over each array and the
     * collection as a whole necessitated some sort of similar looping structure.
     * <br />
     * <br />
     * In any case, for each array, edges between related vertex nodes are established by invoking
     * the <code>addEdge</code> for the first array element (representing the class upon which the
     * others are dependent) and those that follow, denoting the dependency relationship between
     * the origin class and the other array elements.
     * <br />
     * <br />
     * The author considers it an important point to discuss the rationale for not catching cycles
     * within this method. As directed graphs <em>can</em> contain cycles (provided they are not
     * directed acrylic graphs), there was no need to throw exceptions related to the presence of
     * cycles within the method. The topological sort method, on the other hand, <em>does</em> throw
     * such exceptions, as such sorts cannot occur with cycles present. Furthermore, as the rubric
     * and the Indications both made no explicit mention of which method should catch cycle
     * exceptions, the author exercised discretion in this case and elected to allow the method to
     * build cycled graphs if necessary.
     * @param inputArrays <code>ArrayList</code> of <code>T</code> arrays
     * @return void
     */
    public void buildDirectedGraph(ArrayList<T[]> inputArrays) {
        for (T[] array : inputArrays) {
            for (int i = 0; i < array.length; i++) {
                this.addVertex(array[i]);

                /**
                 * Only for the elements following the first class (the origin of sorts). Since the
                 * <code>ArrayList</code> of arrays is arranged in such a way as to ensure that each
                 * array represents a line in the file and thus a dependency relationship, an edge
                 * may be established between the origin element and each of the following classes
                 * depending on it.
                 */
                if (i != 0) {
                    this.addEdge(array[0], array[i]);
                }
            }
        }

        /**
         * Testing method to log the organizational framework in the console, per the Project 4
         * Indications document. This method only serves to implement a similar design to Figure 4
         * in the rubric, in addition to the <code>HashMap</code> key/value pairs. Commented out for
         * the deliverables version.
         */
        //this.displayGraph();
    }

    /**
     * As per the Project 4 Indications document, the author has elected to include the unit testing
     * method that seeks to imitate Figure 4 in the project rubric. Displaying the contents of the
     * <code>LinkedList</code>s in <code>adjacentVertices</code>, the method also prints the
     * contents of the <code>HashMap</code> as a
     * <span style="font-family:Comic Sans MS; font-size:15px; color:yellow;">boNus priZe :)</span>
     */
    public void displayGraph() {
        System.out.println("Dependency diagram as per rubric Figure 4:");
        for (int i = 0; i < this.adjacentVertices.size(); i++) {
            System.out.println(i + " " + this.adjacentVertices.get(i).toString());
        }

        // Key/value mapping
        System.out.println("\n" + "HashMap mapping of Integer keys and T values:");
        System.out.println(Arrays.asList(this.mapToInteger) + "\n");
    }
}