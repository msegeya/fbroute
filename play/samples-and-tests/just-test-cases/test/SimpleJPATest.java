import org.junit.*;
import play.test.*;
import java.util.*;

import models.*;

public class SimpleJPATest extends UnitTest {
    
    @Before
    public void setup() {
        Fixtures.deleteAll();
        Fixtures.load("users.yml");
    }

    @Test
    public void testImport() {
        assertEquals(2, User.count());
        List<User> users = User.findAll();
        User a = users.get(0);
        User b = users.get(1);
        assertNotNull(a);
        assertNotNull(b);
        assertEquals("A", a.name);
        assertEquals("B", b.name);
        assertFalse(a.c);
        assertTrue(b.c);
        assertNull(a.b);
        assertFalse(b.b);
        assertNull(a.i);
        assertEquals(34, (int)b.i);
        assertEquals(45, a.j);
        assertEquals(0, b.j);
        assertEquals(34, (int)b.i);
        assertEquals(10000, b.l);
        assertEquals(0, a.l);
        assertNull(a.k);
        assertNull(b.birth);
        Calendar cal = Calendar.getInstance();
        cal.setTime(a.birth);
        assertEquals(2009, cal.get(Calendar.YEAR));
        assertEquals(11, cal.get(Calendar.MONTH));
        assertEquals(12, cal.get(Calendar.DAY_OF_MONTH));
    }
    
    @Test
    public void dynamicFinders() {
        List<User> users = User.findAll();
        User a = users.get(0);
        User b = users.get(1);
        //
        assertEquals(a, User.find("byName", "A").first());
        assertEquals(b, User.find("byNameAndC", "B", true).first());
        assertNull(User.find("byNameAndC", "B", false).first());
        assertEquals(a, User.find("byNameLikeAndJ", "%a%", 45).first());
        assertEquals(b, User.find("byNameLikeAndBAndCAndLAndI", "%b%", false, true, 10000L, 34).first());
        assertNull(User.find("byNameLikeAndBAndCAndLAndI", "%b%", false, true, 10000L, 32).first());
        assertEquals(a, User.find("byBIsNull").first());
        assertEquals(b, User.find("byBIsNotNull").first());
    }
    
    @Test
    public void simpleFinders() {
        List<User> users = User.findAll();
        User a = users.get(0);
        User b = users.get(1);
        //
        assertEquals(a, User.find("name", "A").first());
        assertEquals(a, User.find("name = ?", "A").first());
        assertEquals(a, User.find("name=?", "A").first());
        assertEquals(b, User.find("name = ? and c = ?", "B", true).first());
        assertNull(User.find("name = ? and c = ?", "B", false).first());
        assertEquals(a, User.find("name like ? and j = ?", "%A%", 45).first());
        assertEquals(b, User.find("name like ? and b = ? and c = ? and l = ? and i = ?", "%B%", false, true, 10000L, 34).first());
        assertNull(User.find("name like ? and b = ? and c = ? and l = ? and i = ?", "%B%", false, true, 10000L, 32).first());
        assertEquals(a, User.find("b is null").first());
        assertEquals(b, User.find("b is not null").first());
    }  
    
    @Test
    public void fullFinders() {
        List<User> users = User.findAll();
        User a = users.get(0);
        User b = users.get(1);
        //
        assertEquals(a, User.find("from User where name = ?", "A").first());
        assertEquals(b, User.find("from User where  name = ? and c = ?", "B", true).first());
        assertNull(User.find("from User where name = ? and c = ?", "B", false).first());
        assertEquals(a, User.find("from User where name like ? and j = ?", "%A%", 45).first());
        assertEquals(b, User.find("from User where name like ? and b = ? and c = ? and l = ? and i = ?", "%B%", false, true, 10000L, 34).first());
        assertNull(User.find("from User where name like ? and b = ? and c = ? and l = ? and i = ?", "%B%", false, true, 10000L, 32).first());
        assertEquals(a, User.find("from User where b is null").first());
        assertEquals(b, User.find("from User where b is not null").first());
        
        assertEquals(a, User.find("select u from User u where u.name = ?", "A").first());
        assertEquals(b, User.find("select u from User u where u.name = ? and u.c = ?", "B", true).first());
        assertNull(User.find("select u from User u where u.name = ? and u.c = ?", "B", false).first());
        assertEquals(a, User.find("select u from User u where u.name like ? and u.j = ?", "%A%", 45).first());
        assertEquals(b, User.find("select u from User u where u.name like ? and u.b = ? and u.c = ? and u.l = ? and u.i = ?", "%B%", false, true, 10000L, 34).first());
        assertNull(User.find("select u from User u where u.name like ? and u.b = ? and u.c = ? and u.l = ? and u.i = ?", "%B%", false, true, 10000L, 32).first());
        assertEquals(a, User.find("select u from User u where u.b is null").first());
        assertEquals(b, User.find("select u from User u where u.b is not null").first());
    } 
    
    @Test
    public void orderBy() { 
        List<User> users = User.findAll();
        User a = users.get(0);
        User b = users.get(1);
        //
        assertEquals(a, User.find("from User order by name ASC").first());
        assertEquals(b, User.find("from User order by name DESC").first()); 
        assertEquals(a, User.find("from User order by b ASC").first());
        assertEquals(b, User.find("from User order by b DESC").first());
        assertEquals(b, User.find("from User order by j ASC").first());
        assertEquals(a, User.find("from User order by j DESC").first());
    }
    
}
