import java.io.*;
import java.util.*;

public class Parser {
    /***
     * For fast lookup use indexes
     * ***/
    protected HashMap<String,ArrayList<String>> employeesInfoIndex;
    protected HashMap<String,ArrayList<String>> firstNameIndex;
    protected HashMap<String,ArrayList<String>> lastNameIndex;
    protected HashMap<String,ArrayList<String>> fullNameIndex;
    protected HashMap<String,ArrayList<String>> organizationIndex;
    protected HashMap<String,ArrayList<String>> teamsIndex;

    public Parser(String emps,String org, String teams){
        initEmployeesIndex(emps);
        initOrganizationIndex(org);
        initTeamIndex(teams);
    }
    public void initOrganizationIndex(String datafile) {
        /***
         * Opens org file and builds
         * OrgIndex
         ***/
        String aLine;
        organizationIndex = new HashMap();
        try {
            FileInputStream fin = new FileInputStream(datafile);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fin));
            // extract column names
            buffer.readLine();
            // extract data
            ArrayList<String> newEntry;
            int tracker = 0;
            String id = null;
            while ((aLine = buffer.readLine()) != null) {
                StringTokenizer st =
                        new StringTokenizer(aLine, "|");
                newEntry = new ArrayList<>();
                tracker = 1;
                /*
                 * id: [title, org]
                 * */
                while (st.hasMoreTokens()) {
                    if (tracker < 3)
                        newEntry.add(st.nextToken());
                    else
                        id = st.nextToken();
                    tracker++;
                }
                organizationIndex.put(id,newEntry);
            }
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initTeamIndex(String datafile) {
        /***
         * Opens team file and builds
         * TeamIndex
         ***/
        String aLine;
        teamsIndex = new HashMap();
        try {
            FileInputStream fin = new FileInputStream(datafile);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fin));
            // extract column names
            buffer.readLine();
            // extract data
            String id,member;
            ArrayList members;
            while ((aLine = buffer.readLine()) != null) {
                StringTokenizer st =
                        new StringTokenizer(aLine, "|");
                id = st.nextToken();
                member = st.nextToken();
                /*
                  manager : List of Emps' ids
                 * managerId : [eId1,eId2,....,eId3]
                 * */
                if (!teamsIndex.containsKey(id)) {
                    members = new ArrayList();
                    members.add(member);
                    teamsIndex.put(id,members);
                } else {
                    teamsIndex.get(id).add(member);
                }
            }
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initEmployeesIndex(String datafile) {
        /***
         * Opens Employee file and builds
         * EmpIndex
         ***/
        String aLine;
        employeesInfoIndex = new HashMap();
        firstNameIndex = new HashMap<>();
        lastNameIndex = new HashMap<>();
        fullNameIndex = new HashMap<>();
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(datafile));
            // extract column names
            buffer.readLine();
            // extract data
            ArrayList<String> newEmployee,ids;
            int tracker = 0;
            String firstName,lastName,fullName;
            while ((aLine = buffer.readLine()) != null) {
                StringTokenizer st =
                        new StringTokenizer(aLine, "|");
                newEmployee = new ArrayList<>();
                ids = new ArrayList<>();
                tracker = 1;
                while (st.hasMoreTokens()) {
                    if (tracker < 5) {
                        /*
                         * [firstName, lastName, phoneNumber, address]
                         * */
                        newEmployee.add(st.nextToken());
                    } else {
                        /*
                         * id : [firstName, lastName, phoneNumber, address]
                         * */
                        firstName = newEmployee.get(0).toLowerCase();
                        lastName = newEmployee.get(1).toLowerCase();
                        fullName = firstName + " " + lastName;
                        final String id = st.nextToken();
                        employeesInfoIndex.put(id, newEmployee);

                        if (!lastNameIndex.containsKey(lastName)) {
                            lastNameIndex.put(lastName, new ArrayList<String>() {{
                                add(id);
                            }});
                        } else
                            lastNameIndex.get(lastName).add(id);

                        if (!firstNameIndex.containsKey(firstName)) {
                            firstNameIndex.put(firstName, new ArrayList<String>() {{
                                add(id);
                            }});
                        } else
                            firstNameIndex.get(firstName).add(id);

                        if (!fullNameIndex.containsKey(fullName)) {
                            fullNameIndex.put(fullName, new ArrayList<String>() {{
                                add(id);
                            }});
                        } else
                            fullNameIndex.get(fullName).add(id);
                    }
                    tracker++;
                }
            }
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
