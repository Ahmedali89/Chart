import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.*;

public class OrgChart {
    private static Parser par;
    private static Vector<String> data;
    public static void main(String arg []) {
        /*** main func: program executs ***/

        /*** Parsing Files ***/
        par = new Parser("personal", "organization", "team");

        /*** Processing Data  ***/
        String [] arr = {"Doe","3"}; // test case
        int itr = 1;
        final int group = 5;
        ArrayList<String> line = new ArrayList<>();
        try {
            lookUpData(arg);
            BufferedWriter fout = new BufferedWriter( new FileWriter("output.txt",true));
            fout.write(String.format("%-20s %-15s %-15s %-20s %-15s%n", "FirstName", "LastName", "Title", "Organization", "PhoneNumber"));
            fout.newLine();
            while (data.size() > 0) {
                if(itr%group != 0) {
                    line.add(data.remove(0));
                } else {
                    line.add(data.remove(0));
                    fout.write(String.format("%-20s %-15s %-15s %-15s %-15s%n", line.get(0), line.get(1), line.get(2), line.get(3), line.get(4)));
                    fout.newLine();
                    fout.flush();
                    line = new ArrayList<>();
                }
                itr++;
            }
            fout.close();
        } catch(IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Name must be not found!!");
        }
    }

    private static void lookUpData(String... params) {
        /***
         * Uses Indexes [firstNameIndex,lastNameIndex, or fullNameIndex] made by parser to find id
         * Then uses the id to navigate orgIndex and teamIndex
         * ***/
        String firstName = null,lastName = null,level = null;
        ArrayList ids = null;
        for (String arg : params) {
            if (arg.matches("^[a-zA-Z]*$")) {
                arg = arg.toLowerCase();
                if(firstName == null) {
                    firstName = arg;
                }
                else if (lastName == null) {
                    lastName = arg;
                }
            } else {
                level = arg;
            }
        }

        if (params.length == 3) {
            ids = par.fullNameIndex.get(firstName+" "+lastName);

        }
        else if(params.length == 2) {
            if (level != null) {
                ids = par.firstNameIndex.get(firstName);
                if(ids == null) {
                    lastName = firstName;
                    ids = par.lastNameIndex.get(lastName);
                }
            } else {
                ids = par.fullNameIndex.get(firstName +" "+lastName);
            }
        } else if(params.length == 1) {
            ids = par.firstNameIndex.get(firstName);
            if(ids == null) {
                ids = par.lastNameIndex.get(lastName);
            }
        }
        if (ids != null){
            if(level == null)
                level = "1";
            data = collectData(ids,Integer.parseInt(level));
        }

    }

    private static Vector<String> collectData(ArrayList<String> ids, Integer level) {
        Vector<String> result  = new Vector<>();
        for(String id: ids) {
            BreadthFirstSearch(id,level,result);
        }
        return result;
    }

    private static void BreadthFirstSearch(String id,Integer level, Vector<String> result) {
        /***
         * ja
         * ***/
        Queue<Pair<String, Integer>> queue = new ArrayDeque<>();
        queue.add(new Pair(id,level));
        String firstName,lastName,title,org,phone;
        ArrayList<String> members = null;
        HashSet<String> seen = new HashSet<>();
        while(queue.size() > 0) {
            Pair pair = queue.poll();
            String emp = (String) pair.getKey();
            int stage = (int) pair.getValue();
            firstName = par.employeesInfoIndex.get(emp).get(0);
            lastName = par.employeesInfoIndex.get(emp).get(1);
            phone = par.employeesInfoIndex.get(emp).get(2);
            title = par.organizationIndex.get(emp).get(0);
            org = par.organizationIndex.get(emp).get(1);
            result.addAll(Arrays.asList(firstName,lastName,title,org,phone));
            members =  par.teamsIndex.get(emp);
            if (members != null) {
                for (String member : members) {
                    if (stage > 0 && !seen.contains(member)) {
                        seen.add(member);
                        queue.add(new Pair(member, stage - 1));
                    }
                }
            }
        }
    }

}
