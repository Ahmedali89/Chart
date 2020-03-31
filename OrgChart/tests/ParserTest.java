import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void initOrganizationIndex() {
        Parser par =  new Parser("personal", "organization", "team");
        assertEquals(11,par.organizationIndex.size());
    }

    @Test
    void initTeamIndex() {
        Parser par =  new Parser("personal", "organization", "team");
        assertEquals(6,par.teamsIndex.size());
    }

    @Test
    void initEmployeesIndex() {
        Parser par =  new Parser("personal", "organization", "team");
        assertEquals(9,par.firstNameIndex.size());
        assertEquals(9,par.lastNameIndex.size());
        assertEquals(10,par.fullNameIndex.size());
        assertEquals(11,par.employeesInfoIndex.size());
    }
}