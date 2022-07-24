
// javac namefile.java
// java namefile.java input
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.awt.*;
import javax.swing.*;

class GraphDraw extends JFrame {
    int width;
    int height;
    ArrayList<Node> nodes;
    ArrayList<edge> edges;

    public GraphDraw(String name) {
        this.setTitle(name);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nodes = new ArrayList<Node>();
        edges = new ArrayList<edge>();
        width = 30;
        height = 30;
    }

    class Node {
        int x, y;
        String name;

        public Node(String Name, int start, int end) {
            x = start;
            y = end;
            name = Name;
        }
    }

    class edge {
        int i, j;

        public edge(int from, int to) {
            i = from;
            j = to;
        }
    }

    public void addNode(String name, int x, int y) {
        nodes.add(new Node(name, x, y));
        this.repaint();
    }

    public void addEdge(int i, int j) {
        edges.add(new edge(i, j));
        this.repaint();
    }

    public void paint(Graphics g) {
        FontMetrics f = g.getFontMetrics();
        int nodeHeight = Math.max(height, f.getHeight());

        g.setColor(Color.black);
        for (edge e : edges) {
            g.drawLine(nodes.get(e.i).x, nodes.get(e.i).y,
                    nodes.get(e.j).x, nodes.get(e.j).y);
        }

        for (Node n : nodes) {
            int nodeWidth = Math.max(width, f.stringWidth(n.name) + width / 2);
            g.setColor(Color.white);
            g.fillOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2,
                    nodeWidth, nodeHeight);
            g.setColor(Color.black);
            g.drawOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2,
                    nodeWidth, nodeHeight);
            g.drawString(n.name, n.x - f.stringWidth(n.name) / 2,
                    n.y + f.getHeight() / 2);
        }
    }
}

public class CS3_4_20190515 {
    static int requestNum = 0;
    public static ArrayList<Integer> requests = new ArrayList<Integer>();
    public static ArrayList<Integer> temprequests = new ArrayList<Integer>();
    public static ArrayList<Integer> sortedRequests = new ArrayList<Integer>();
    public static int intialHead = 0;
    public static int totalOfMovements = 0;
    public static ArrayList<Integer> sequenceOfExecuting = new ArrayList<Integer>();
    public static ArrayList<Integer> tempSequenceExecuting = new ArrayList<Integer>();
    public static Scanner input = new Scanner(System.in);
    static int startDisk = 0;// This Assumption look like Lecture
    static int endDisk = 199;
    static String Direction = "left";

    // __________________________________________________________________________________________________________________________________________
    // ---------------------Some addational functions
    public static void restartInformation() {
        requests = temprequests;
        totalOfMovements = 0;
        sequenceOfExecuting = new ArrayList<>();
    }

    // __________________________________________________________________________________________________________________________________________
    // ---------------------FIRCT COME FIRST SERVE--------------------------------
    public static void FCFS() {
        restartInformation();
        System.out.println(
                "           sequence of head movement to access the requested cylinderst when using FCFS is :         \n");

        totalOfMovements += Math.abs(intialHead - requests.get(0));

        sequenceOfExecuting.add(intialHead);
        sequenceOfExecuting.add(requests.get(0));
        for (int i = 1; i < requests.size(); i++) {
            totalOfMovements += Math.abs(requests.get(i - 1) - requests.get(i));
            sequenceOfExecuting.add(requests.get(i));
        }
        System.out.println("SEQUENCE OF EXECUTING REQUEST IS " + sequenceOfExecuting);
        System.out.println("Number of total Movements is  " + totalOfMovements);
        System.out.println("\n");
    }

    // ___________________________________________________________________________________________________________________________________________
    // ----------------------Shortest Seek Time First-----------------
    public static int calulcateShortDistance(int currentPosition, ArrayList<Integer> temprequest) {

        int tempShortDis = Integer.MAX_VALUE;
        int NearstRequset = 0;

        for (int i = 0; i < requests.size(); i++) {
            if (Math.abs(requests.get(i) - currentPosition) < tempShortDis && !temprequest.contains(requests.get(i))) {
                NearstRequset = i;
                tempShortDis = Math.abs(requests.get(i) - currentPosition);
            }
        }
        return NearstRequset;
    }

    public static void SSTF() {
        restartInformation();

        System.out.println(
                "           sequence of head movement to access the requested cylinderst when using SSTF is :         \n");

        ArrayList<Integer> ExecutedRequestes = new ArrayList<>();

        int NearstRequset = calulcateShortDistance(intialHead, ExecutedRequestes);
        ExecutedRequestes.add(intialHead);
        ExecutedRequestes.add(requests.get(NearstRequset));

        totalOfMovements += Math.abs(intialHead - requests.get(NearstRequset));

        for (int i = 1; i < requests.size(); i++) {
            NearstRequset = calulcateShortDistance(ExecutedRequestes.get(i), ExecutedRequestes);

            ExecutedRequestes.add(requests.get(NearstRequset));

            totalOfMovements += Math.abs(ExecutedRequestes.get(i) - requests.get(NearstRequset));

        }

        System.out.println("SEQUENCE IS " + ExecutedRequestes);
        System.out.println("Number of total Movements is  " + totalOfMovements);
        sequenceOfExecuting = ExecutedRequestes;
        System.out.println("\n");
    }

    // ___________________________________________________________________________________________________________________________________________
    // ----------SCAN------
    // don't forget if we move left then must we reach start disk else we must reach
    // endDisk(RESOURCE :BOOK ,LEC,(SLide24 Binkpart))
    public static void SCAN() {
        restartInformation();
        System.out.println(
                "           sequence of head movement to access the requested cylinderst when using SCAN is :         \n");
        sortedRequests = requests;
        Collections.sort(sortedRequests);
        List<Integer> LeftInitial = new ArrayList<>();
        List<Integer> RightInitial = new ArrayList<>();
        int i = 0;
        for (; i < sortedRequests.size(); i++) {
            if (sortedRequests.get(i) < intialHead) {
                LeftInitial.add(sortedRequests.get(i));
                continue;
            }
            break;// note why hence we break and we must check every elment? because we sort array
                  // above "173"
        }
        
        RightInitial = sortedRequests.subList(i, sortedRequests.size());
        sequenceOfExecuting.add(intialHead);
        if (Direction.equals("left")) {
            // note we add elements in Left list are sorted so we need last element(before
            // initial Head )so we pass write LeftInitial.get(LeftInitial.size() - 1)
            // look like if we have data 98 183 37 122 14 124 and initial head is 53 so
            // first we sort so list(Requests) and will be 14 34 98 122 124.. and left list
            // will be 0 14 34 so head will move from inital head to 34 .
            totalOfMovements += Math.abs(LeftInitial.get(LeftInitial.size() - 1) - intialHead);

            sequenceOfExecuting.add(LeftInitial.get(LeftInitial.size() - 1));// add first request execute

            for (i = 1; i < LeftInitial.size(); i++) {
                // LeftInitial.size() - 1 - i :العنصر اللى قبل الاخير وهكذا
                totalOfMovements += Math
                        .abs((LeftInitial.get(LeftInitial.size() - 1 - i)) - LeftInitial.get(LeftInitial.size() - i));
                sequenceOfExecuting.add(LeftInitial.get(LeftInitial.size() - 1 - i));
            }

            totalOfMovements += Math.abs(LeftInitial.get(0) - startDisk);// 0:starting disk

            // why we don't add end disk because working mechanism algo scan :go to start
            // point and reach last request as for Cscan go to end and start
            sequenceOfExecuting.add(startDisk);// add start disk because we reach to it after finsihing Left list

            // work on right list
            totalOfMovements += Math.abs((RightInitial.get(0)) - startDisk);// 0:starting disk
            sequenceOfExecuting.add(RightInitial.get(0));

            for (i = 1; i < RightInitial.size(); i++) {
                totalOfMovements += Math.abs((RightInitial.get(i)) - (RightInitial.get(i - 1)));
                sequenceOfExecuting.add(RightInitial.get(i));
            }

        } else {
            totalOfMovements += Math.abs(RightInitial.get(0) - intialHead);

            sequenceOfExecuting.add(RightInitial.get(0));// add first request execute

            for (i = 1; i < RightInitial.size(); i++) {
                totalOfMovements += Math.abs((RightInitial.get(i)) - (RightInitial.get(i - 1)));
                sequenceOfExecuting.add(RightInitial.get(i));
            }

            totalOfMovements += Math.abs(RightInitial.get(RightInitial.size() - 1) - endDisk);
            sequenceOfExecuting.add(endDisk);

            totalOfMovements += Math.abs(LeftInitial.get(LeftInitial.size() - 1) - endDisk);

            sequenceOfExecuting.add(LeftInitial.get(LeftInitial.size() - 1));

            for (i = 1; i < LeftInitial.size(); i++) {
                totalOfMovements += Math
                        .abs((LeftInitial.get(LeftInitial.size() - 1 - i)) - LeftInitial.get(LeftInitial.size() - i));
                sequenceOfExecuting.add(LeftInitial.get(LeftInitial.size() - 1 - i));
            }

        }

        System.out.println("SEQUENCE OF EXECUTING REQUEST IS " + sequenceOfExecuting);
        System.out.println("Number of total Movements is  " + totalOfMovements);
        sortedRequests = new ArrayList<>();
        System.out.println("\n");
    }

    // ___________________________________________________________________________________________________________________________________________
    // ----------C-SCAN----
    public static void C_SCAN() {// note we add start and disk because we reach to these and in assignment
        // require show head movement
        restartInformation();
        System.out.println(
                "           sequence of head movement to access the requested cylinderst when using C-SCAN is :         \n");

        sortedRequests = requests;
        Collections.sort(sortedRequests);
        List<Integer> LeftInitial = new ArrayList<>();

        List<Integer> RightInitial = new ArrayList<>();
        int i = 0;
        for (; i < sortedRequests.size(); i++) {
            if (sortedRequests.get(i) < intialHead) {
                LeftInitial.add(sortedRequests.get(i));
                continue;
            }
            break;// note why hence we break and we must check every elment because we sort array
                  // above
        }
        RightInitial = sortedRequests.subList(i, sortedRequests.size());
        if (Direction.equals("right")) {

            totalOfMovements += Math.abs((RightInitial.get(0)) - intialHead);
            sequenceOfExecuting.add(RightInitial.get(0));

            for (i = 1; i < RightInitial.size(); i++) {
                totalOfMovements += Math.abs((RightInitial.get(i)) - (RightInitial.get(i - 1)));
                sequenceOfExecuting.add(RightInitial.get(i));
            }

            // from last request to end disk
            totalOfMovements += Math.abs(RightInitial.get(RightInitial.size() - 1) - endDisk);
            sequenceOfExecuting.add(endDisk); // add end and start disk because we reach to these

            totalOfMovements += Math.abs(startDisk - endDisk);// return to starting point and go to serve
            sequenceOfExecuting.add(startDisk);// add end and start disk because we reach to these

            totalOfMovements += Math.abs(startDisk - LeftInitial.get(0));// return to starting point and go to serve
            sequenceOfExecuting.add(LeftInitial.get(0));

            for (i = 1; i < LeftInitial.size(); i++) {
                totalOfMovements += Math
                        .abs(LeftInitial.get(i) - LeftInitial.get(i - 1));
                sequenceOfExecuting.add(LeftInitial.get(i));
            }
        } else {

            totalOfMovements += Math.abs((LeftInitial.get(LeftInitial.size() - 1)) - intialHead);
            sequenceOfExecuting.add(LeftInitial.get(LeftInitial.size() - 1));

            for (i = 1; i < LeftInitial.size(); i++) {
                totalOfMovements += Math
                        .abs(LeftInitial.get(LeftInitial.size() - i) - LeftInitial.get(LeftInitial.size() - i - 1));
                sequenceOfExecuting.add(LeftInitial.get(LeftInitial.size() - i - 1));
            }
            totalOfMovements += Math.abs(startDisk - LeftInitial.get(0));
            sequenceOfExecuting.add(startDisk);

            totalOfMovements += Math.abs(startDisk - endDisk);
            sequenceOfExecuting.add(endDisk);

            totalOfMovements += Math.abs(RightInitial.get(RightInitial.size() - 1) - endDisk);

            sequenceOfExecuting.add(RightInitial.get(RightInitial.size() - 1));

            for (i = 1; i < RightInitial.size(); i++) {
                totalOfMovements += Math.abs(
                        (RightInitial.get(RightInitial.size() - i)) - (RightInitial.get(RightInitial.size() - 1 - i)));
                sequenceOfExecuting.add(RightInitial.get(RightInitial.size() - 1 - i));
            }
        }
        sequenceOfExecuting.add(0,intialHead);
        System.out.println("SEQUENCE  IS " + sequenceOfExecuting);
        System.out.println("Number of total Movements is  " + totalOfMovements);
        sortedRequests = new ArrayList<>();
        System.out.println("\n");
    }

    // ___________________________________________________________________________________________________________________________________________
    // ----------C-LOOK----
    public static void C_LOOK() {// look like C-scan but not reach start and end disk
        restartInformation();
        sortedRequests = requests;
        System.out.println(
                "           sequence of head movement to access the requested cylinderst when using C-LOOK is :         \n");

        Collections.sort(sortedRequests);
        List<Integer> LeftInitial = new ArrayList<>();

        List<Integer> RightInitial = new ArrayList<>();
        int i = 0;
        for (; i < sortedRequests.size(); i++) {
            if (sortedRequests.get(i) < intialHead) {
                LeftInitial.add(sortedRequests.get(i));
                continue;
            }
            break;// note why hence we break and we must check every elment because we sort array
                  // above
        }

        RightInitial = sortedRequests.subList(i, sortedRequests.size());

        if (Direction.equals("right")) {
            totalOfMovements += Math.abs((RightInitial.get(0)) - intialHead);
            sequenceOfExecuting.add(RightInitial.get(0));

            for (i = 1; i < RightInitial.size(); i++) {
                totalOfMovements += Math.abs((RightInitial.get(i)) - (RightInitial.get(i - 1)));
                sequenceOfExecuting.add(RightInitial.get(i));
            }

            // return to starting point and go to serve
            totalOfMovements += Math.abs(RightInitial.get(RightInitial.size() - 1) - LeftInitial.get(0));
            sequenceOfExecuting.add(LeftInitial.get(0));

            for (i = 1; i < LeftInitial.size(); i++) {
                totalOfMovements += Math
                        .abs(LeftInitial.get(i) - LeftInitial.get(i - 1));
                sequenceOfExecuting.add(LeftInitial.get(i));
            }
        } else {

            totalOfMovements += Math.abs(LeftInitial.get(LeftInitial.size() - 1) - intialHead);
            sequenceOfExecuting.add(LeftInitial.get((LeftInitial.size() - 1)));

            for (i = 1; i < LeftInitial.size(); i++) {
                totalOfMovements += Math
                        .abs(LeftInitial.get((LeftInitial.size() - i))
                                - LeftInitial.get((LeftInitial.size() - 1 - i)));
                sequenceOfExecuting.add(LeftInitial.get((LeftInitial.size() - 1 - i)));
            }

            totalOfMovements += Math.abs((RightInitial.get(RightInitial.size() - 1) - LeftInitial.get(0)));
            sequenceOfExecuting.add(RightInitial.get(RightInitial.size() - 1));

            for (i = 1; i < RightInitial.size(); i++) {
                totalOfMovements += Math.abs(
                        (RightInitial.get(RightInitial.size() - i)) - (RightInitial.get(RightInitial.size() - i - 1)));
                sequenceOfExecuting.add(RightInitial.get(RightInitial.size() - 1 - i));
            }

        }
        System.out.println("SEQUENCE  IS " + sequenceOfExecuting);
        System.out.println("Number of total Movements is  " + totalOfMovements);
        System.out.println("\n");
    }

    // ___________________________________________________________________________________________________________________________________________
    // ------------------------LOOK---
    public static void LOOK() {// look like scan but not reach start and end disk
        restartInformation();
        System.out.println(
                "           sequence of head movement to access the requested cylinderst when using LOOK is :         \n");

        sortedRequests = requests;
        Collections.sort(sortedRequests);
        List<Integer> LeftInitial = new ArrayList<>();
        List<Integer> RightInitial = new ArrayList<>();
        int i = 0;
        for (; i < sortedRequests.size(); i++) {
            if (sortedRequests.get(i) < intialHead) {
                LeftInitial.add(sortedRequests.get(i));
                continue;
            }
            break;// note why hence we break and we must check every elment because we sort array
                  // above

        }
        RightInitial = sortedRequests.subList(i, sortedRequests.size());

        if (Direction.equals("left")) {
            totalOfMovements += Math.abs(LeftInitial.get(LeftInitial.size() - 1) - intialHead);

            sequenceOfExecuting.add(LeftInitial.get(LeftInitial.size() - 1));// add first request execute

            for (i = 1; i < LeftInitial.size(); i++) {
                // LeftInitial.size() - 1 - i :العنصر اللى قبل الاخير وهكذا
                totalOfMovements += Math
                        .abs((LeftInitial.get(LeftInitial.size() - 1 - i)) - LeftInitial.get(LeftInitial.size() - i));
                sequenceOfExecuting.add(LeftInitial.get(LeftInitial.size() - 1 - i));
            }

            totalOfMovements += Math.abs((RightInitial.get(0)) - LeftInitial.get(0));
            sequenceOfExecuting.add(RightInitial.get(0));

            for (i = 1; i < RightInitial.size(); i++) {
                totalOfMovements += Math.abs((RightInitial.get(i)) - (RightInitial.get(i - 1)));
                sequenceOfExecuting.add(RightInitial.get(i));
            }
        } else {
            totalOfMovements += Math.abs((RightInitial.get(0)) - intialHead);
            sequenceOfExecuting.add(RightInitial.get(0));

            for (i = 1; i < RightInitial.size(); i++) {
                totalOfMovements += Math.abs((RightInitial.get(i)) - (RightInitial.get(i - 1)));
                sequenceOfExecuting.add(RightInitial.get(i));
            }

            totalOfMovements += Math
                    .abs(RightInitial.get(RightInitial.size() - 1) - LeftInitial.get(LeftInitial.size() - 1));

            sequenceOfExecuting.add(LeftInitial.get(LeftInitial.size() - 1));

            for (i = 1; i < LeftInitial.size(); i++) {
                totalOfMovements += Math
                        .abs((LeftInitial.get(LeftInitial.size() - 1 - i)) - LeftInitial.get(LeftInitial.size() - i));
                sequenceOfExecuting.add(LeftInitial.get(LeftInitial.size() - 1 - i));
            }

        }
        System.out.println("SEQUENCE  IS " + sequenceOfExecuting);
        System.out.println("Number of total Movements is  " + totalOfMovements);
        System.out.println("\n");
    }

    // ___________________________________________________________________________________________________________________________________________
    // -----------------------NEWLY OPTIMIZED ALGORITHM ---
    /*
     * 5- You should summarize the newly optimized algorithm in your own words:
     * 
     * 1-this is Best alogrithm to Disk Scheduling in save head movements
     * because head move only one direction without back and other alogrithms head
     * to service all requests must move in two direction
     * 2-Eaiser alogrithm in implementation
     * 3-move one direction (From starting to ending )
     * 4-Faster
     */
    public static void newlyOptimizedAlgorithm() {
        restartInformation();
        System.out.println(
                "           sequence of head movement to access the requested cylinderst when using newlyOptimizedAlgorithm is :         \n");
        sortedRequests = requests;
        Collections.sort(sortedRequests);

        // solution number 1----------------------------------------------
        // totalOfMovements += Math.abs(intialHead - sortedRequests.get(0));
        // sequenceOfExecuting.add(sortedRequests.get(0));
        // for (int i = 1; i < sortedRequests.size(); i++) {
        // totalOfMovements += Math.abs(sortedRequests.get(i - 1) -
        // sortedRequests.get(i));

        // sequenceOfExecuting.add(sortedRequests.get(i));
        // }
        // --------------------------------------------------------------------------------------------------------------------

        // soultion number 2
        sequenceOfExecuting = sortedRequests;
        totalOfMovements = sortedRequests.get(sortedRequests.size() - 1);
        
        System.out.println("SEQUENCE IS " + sequenceOfExecuting);
        System.out.println("Number of total Movements is " + totalOfMovements);
        System.out.println("\n");
    }

    // ___________________________________________________________________________________________________________________________________________
    public static void draw() {

        GraphDraw frame = new GraphDraw("Disk Scheduling");

        frame.setSize(4000, 3000);

        frame.setVisible(true);

        frame.addNode("TOATL OF MOVEMENTS IS " + totalOfMovements, 800, 100);

        int starting = 800;
        int Ending = 200;

        frame.addNode("Inital Head " + intialHead, starting, Ending);

        if (sequenceOfExecuting.get(0) > intialHead) {
            frame.addNode("Position  " + sequenceOfExecuting.get(0) + "Come From Initial Head", starting + 75,
                    Ending + 50);
        } else {
            frame.addNode("Position  " + sequenceOfExecuting.get(0) + "Come From Initial Head", starting - 75,
                    Ending + 50);
        }

        ArrayList<Integer> Drawan = new ArrayList<>();

        Drawan.add(intialHead);
        Drawan.add(sequenceOfExecuting.get(0));
        frame.addEdge(1, 2);

        int max = Collections.max(sequenceOfExecuting);
        int min = Collections.min(sequenceOfExecuting);

        for (int i = 1; i < sequenceOfExecuting.size(); i++) {
            int position = count(Drawan, i);

            if (sequenceOfExecuting.get(i) == max) {
                frame.addNode(
                        "Position  " + sequenceOfExecuting.get(i) + "\nCome From" + sequenceOfExecuting.get(i - 1),
                        starting + 1000, Ending + 50 * (i + 1));

                Drawan.add(sequenceOfExecuting.get(i));
                frame.addEdge(i + 1, i + 2);
                continue;
            }
            if (sequenceOfExecuting.get(i) == min) {
                frame.addNode(
                        "Position  " + sequenceOfExecuting.get(i) + "\nCome From" + sequenceOfExecuting.get(i - 1),
                        starting - 700, Ending + 50 * (i + 1));

                Drawan.add(sequenceOfExecuting.get(i));
                frame.addEdge(i + 1, i + 2);
                continue;
            }

            frame.addNode(
                    "Position  " + sequenceOfExecuting.get(i) + "\nCome From" + sequenceOfExecuting.get(i - 1),
                    starting + 75 * position, Ending + 50 * (i + 1));

            Drawan.add(sequenceOfExecuting.get(i));
            frame.addEdge(i + 1, i + 2);

        }
        // 98, 183, 37, 122, 14, 124, 65, 67

    }

    public static int count(ArrayList<Integer> DRAWNA, int i) {
        int counter = 0;
        for (int j = 0; j < DRAWNA.size(); j++) {
            if (sequenceOfExecuting.get(i) < DRAWNA.get(j))
                counter--;
            else
                counter++;
        }
        return counter;
    }
    // ___________________________________________________________________________________________________________________________________________

    public static void COMMANDLINE(String[] ARGS) {
        for (int i = 0; i < ARGS.length; i++)
            requests.add(Integer.parseInt(ARGS[i]));
        temprequests = requests;

        System.out.println("Enter initial Head :");
        intialHead = input.nextInt();

        System.out.println("Enter Direction which 1-right 2-left :");
        int choice = input.nextInt();

        if (choice == 1)// why hence i don't write else because defualt valur for Driection is left
            Direction = "right";

        System.out.println(
                "Enter Number of Algorithm that do you need see visualizing the sequence ofthe head movements which :\n1-FCFS\n2-SSTF\n3-SCAN\n4-C_SCAN\n5-LOOK\n6-C_LOOK\n7-newly optimizedalgorithm\n8-exit ");
        choice = input.nextInt();
        while (choice != 8) {
            switch (choice) {
                case 1:
                    FCFS();
                    break;
                case 2:
                    SSTF();
                    break;
                case 3:
                    SCAN();
                    break;
                case 4:
                    C_SCAN();
                    break;
                case 5:
                    LOOK();
                    break;
                case 6:
                    C_LOOK();
                    break;
                case 7:
                    newlyOptimizedAlgorithm();

                    break;
            }
            System.out.println(
                    "Enter Number of Algorithm that do you need see visualizing the sequence ofthe head movements which :\n1-FCFS\n2-SSTF\n3-SCAN\n4-C_SCAN\n5-LOOK\n6-C_LOOK\n7-newly optimizedalgorithm\n8-exit ");
            choice = input.nextInt();
        }
    }

    public static void GUI() {
        String temp = JOptionPane.showInputDialog(null, "Enter Initial head");
        intialHead = Integer.parseInt(temp);

        // temp = JOptionPane.showInputDialog(null, "Enter NumberOfRequest");
        // int NumberOfRequests = Integer.parseInt(temp);

        // for (int i = 0; i < NumberOfRequests; i++) {
        // temp = JOptionPane.showInputDialog(null, "Enter Request" + i);
        // requests.add(Integer.parseInt(temp));
        // }

        // ex lec ,we write these to save time
        requests.add(98);
        requests.add(183);
        requests.add(37);
        requests.add(122);
        requests.add(14);
        requests.add(124);
        requests.add(65);
        requests.add(67);

        temprequests = requests;
        temp = JOptionPane.showInputDialog(null, "Enter Number of direction which 1-Right 2- Left :");
        int choice = Integer.parseInt(temp);

        if (choice == 1)// why hence i don't write else because defualt valur for Driection is left
            Direction = "right";

        while (choice != 8) {
            temp = JOptionPane.showInputDialog(null,
                    "Enter Number of Algorithm that do you need see visualizing the sequence ofthe head movements which :\n1-FCFS\n2-SSTF\n3-SCAN\n4-C_SCAN\n5-LOOK\n6-C_LOOK\n7-newly optimizedalgorithm\n8Exit ");

            choice = Integer.parseInt(temp);

            switch (choice) {
                case 1:
                    FCFS();
                    break;
                case 2:
                    SSTF();
                    break;
                case 3:
                    SCAN();
                    break;
                case 4:
                    C_SCAN();
                    break;
                case 5:
                    LOOK();
                    break;
                case 6:
                    C_LOOK();
                    break;
                case 7:
                    newlyOptimizedAlgorithm();
                    break;
            }
            draw();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void Menu(String[] ARGS) {
        if (ARGS.length > 0)
            COMMANDLINE(ARGS);
        else
            GUI();

    }

    public static void main(String[] args) {

        Menu(args);

    }
}
