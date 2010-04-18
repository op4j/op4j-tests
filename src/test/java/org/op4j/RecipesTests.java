package org.op4j;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;
import org.javaruntype.type.Type;
import org.javaruntype.type.Types;
import org.junit.Test;
import org.op4j.functions.Call;
import org.op4j.functions.DecimalPoint;
import org.op4j.functions.Fn;
import org.op4j.functions.FnCalendar;
import org.op4j.functions.FnFunc;
import org.op4j.functions.FnList;
import org.op4j.functions.FnNumber;
import org.op4j.functions.FnString;
import org.op4j.functions.Function;
import org.op4j.functions.Get;
import org.op4j.ognl.functions.FnOgnl;

public class RecipesTests extends TestCase {

	
	
	@Test
	public void testOP4J_001() throws Exception {
	    // Creating a list from its elements
	    
	    List<String> result = Arrays.asList(new String[] {"hello", "hola", "ola", "ol�"});
	    
	    List<String> stringList = 
	        Op.onListFor("hello", "hola", "ola", "ol�").get();
	    
	    assertEquals(result, stringList);
	    
	}
	
	
    
    @Test
    public void testOP4J_002() throws Exception {
        // Creating a set from its elements

        final User user1 = new User();
        final User user2 = new User();
        final User user3 = new User();
        
        Set<User> result = new LinkedHashSet<User>(Arrays.asList(new User[] {user1, user2, user3}));
        
        Set<User> users = 
            Op.onSetFor(user1, user2, user3).get();
        
        assertEquals(result, users);
        
    }
	
    
    
    
    @Test
    @SuppressWarnings("boxing")
    public void testOP4J_003() throws Exception {
        // Creating maps by zipping keys and values

        Map<String,Integer> result = new LinkedHashMap<String,Integer>();
        result.put("John", Integer.valueOf(27));
        result.put("Mary", Integer.valueOf(49));
        result.put("Derek", Integer.valueOf(19));
        
        
        {
            Map<String,Integer> agesByName =
                Op.onListFor("John", "Mary", "Derek").zipValues(27, 49, 19).get();
            
            assertEquals(result, agesByName);
        }

        
        {
            Map<String,Integer> agesByName =
                Op.onListFor(27, 49, 19).zipKeys("John", "Mary", "Derek").get();        
            
            assertEquals(result, agesByName);
        }
        
    }
    
    
    
    
    @Test
    public void testOP4J_004() throws Exception {
        // Creating maps by coupling elements

        Map<String,String> result = new LinkedHashMap<String,String>();
        result.put("Spain", "Madrid");
        result.put("United Kingdom", "London");
        result.put("France", "Paris");
        
        
        Map<String,String> capitals =
            Op.onListFor("Spain", "Madrid", "United Kingdom", "London", "France", "Paris").couple().get();
                
        
        assertEquals(result, capitals);
        
    }


    
    
    
    @Test
    public void testOP4J_005() throws Exception {
        // Converting a String into a Calendar (using a pattern)

        Calendar result = Calendar.getInstance();
        result = DateUtils.truncate(result, Calendar.YEAR);
        result.set(Calendar.YEAR, 1978);
        result.set(Calendar.MONTH, 11);
        result.set(Calendar.DAY_OF_MONTH, 6);

        
        String date = "06/12/1978";        
        
        Calendar cal =
            Op.on(date).exec(FnString.toCalendar("dd/MM/yyyy")).get();
        
        assertEquals(result.getTimeInMillis(), cal.getTimeInMillis());
        
    }
    

    
    
    @Test
    public void testOP4J_006() throws Exception {
        // Converting a list of Strings into a list of Calendars (using a pattern and a locale)

        Calendar resCal1 = Calendar.getInstance();
        resCal1 = DateUtils.truncate(resCal1, Calendar.YEAR);
        resCal1.set(Calendar.YEAR, 1492);
        resCal1.set(Calendar.MONTH, 9);
        resCal1.set(Calendar.DAY_OF_MONTH, 12);

        Calendar resCal2 = Calendar.getInstance();
        resCal2 = DateUtils.truncate(resCal2, Calendar.YEAR);
        resCal2.set(Calendar.YEAR, 1978);
        resCal2.set(Calendar.MONTH, 11);
        resCal2.set(Calendar.DAY_OF_MONTH, 6);

        List<Calendar> result = Arrays.asList(new Calendar[] { resCal1, resCal2 });
        
        
        List<String> dates = 
            Arrays.asList(new String[] { "12 de octubre, 1492", "06 de diciembre, 1978" });

        {
            List<Calendar> cals =
                Op.on(dates).map(FnString.toCalendar("dd 'de' MMMM, yyyy", "es")).get();
            
            for (int i = 0; i < cals.size(); i++) {
                assertEquals(result.get(i).getTimeInMillis(), cals.get(i).getTimeInMillis());
            }
        }

        {
            List<Calendar> cals =
                Op.on(dates).forEach().exec(FnString.toCalendar("dd 'de' MMMM, yyyy", "es")).get();
            
            for (int i = 0; i < cals.size(); i++) {
                assertEquals(result.get(i).getTimeInMillis(), cals.get(i).getTimeInMillis());
            }
        }
        
    }
    
    
    
    @Test
    @SuppressWarnings("boxing")
    public void testOP4J_007() throws Exception {
        // Filtering nulls from an array

        final Integer[] result = new Integer[] {12, 43, 92, 34 };
        

        final Integer[] values = new Integer[] {null, 12, 43, 92, null, 34 };
        
        Integer[] filteredValues =
            Op.on(values).removeAllNull().get();
            
        assertEquals(Integer[].class, filteredValues.getClass());
        assertEquals(Arrays.asList(result), Arrays.asList(filteredValues));

    }
    
    
    
    
    @Test
    public void testOP4J_008() throws Exception {
        // Parsing a String decimal number into a Double (using locale)

        final Double result = Double.valueOf(34.59);

        final String strValue = "34,59";
        
        Double value =
            Op.on(strValue).exec(FnString.toDouble("fr")).get();
            
        assertEquals(result, value);
        

    }
    
    
    
    @Test
    public void testOP4J_009() throws Exception {
        // Parsing a String number into a BigDecimal (no matter the decimal separator)

        final Double result1 = Double.valueOf(871.21);
        final Double result2 = Double.valueOf(421.441);

        final String strValue1 = "871,21";
        final String strValue2 = "421.441";
        
        Double value1 =
            Op.on(strValue1).exec(FnString.toDouble(DecimalPoint.CAN_BE_POINT_OR_COMMA)).get();
        Double value2 =
            Op.on(strValue2).exec(FnString.toDouble(DecimalPoint.CAN_BE_POINT_OR_COMMA)).get();
            
        assertEquals(result1, value1);
        assertEquals(result2, value2);
        

    }
    
    
    
    @Test
    @SuppressWarnings("boxing")
    public void testOP4J_010() throws Exception {
        // Converting an array of Strings into a null-free array of Integers

        final Integer[] result = new Integer[] { 431, 94, 398 };

        final String[] strArray = new String[] { "431", null, "94", "398" };

        {
            Integer[] values =
                Op.on(strArray).map(Types.INTEGER, FnString.toInteger()).removeAllNull().get();
                
            assertEquals(Integer[].class, values.getClass());
            assertEquals(Arrays.asList(result), Arrays.asList(values));
        }
        
        {
            Integer[] values =
                Op.on(strArray).forEach().exec(Types.INTEGER, FnString.toInteger()).endFor().removeAllNull().get();
                
            assertEquals(Integer[].class, values.getClass());
            assertEquals(Arrays.asList(result), Arrays.asList(values));
        }
        
        {
            List<Integer> valuesList = new ArrayList<Integer>();
            for (String element : strArray) {
                if (element != null) {
                    valuesList.add(Integer.parseInt(element));
                }
            }
            Integer[] values = valuesList.toArray(new Integer[valuesList.size()]);
            
            assertEquals(Arrays.asList(result), Arrays.asList(values));
        }

    }

    
    
    @Test
    @SuppressWarnings("boxing")
    public void testOP4J_011() throws Exception {
        // Filtering numbers greater than a value out of a list

        final List<Integer> result = Arrays.asList(new Integer[] { 53, 430 });

        List<Integer> values = Arrays.asList(new Integer[] { 6641, 53, 430, 1245 });
        final List<Integer> originalValues = values;

        {
            values =
                Op.on(values).removeAllTrue(FnNumber.greaterThan(500)).get();
                
            assertEquals(result, values);
        }

        values = originalValues;
        
        {
            List<Integer> valuesAux = new ArrayList<Integer>();
            for (Integer value : values) {
                if (value.longValue() <= 500) {
                    valuesAux.add(value);
                }
            }
            values = valuesAux;
            
            assertEquals(result, values);
        }

    }


    
    
    @Test
    public void testOP4J_012() throws Exception {
        // Converting a list into an array

        final String[] result = new String[] { "earth", "air", "fire", "water" };

        List<String> elementList = Arrays.asList(new String[] { "earth", "air", "fire", "water" });

        {
            String[] elementArray =
                Op.on(elementList).toArrayOf(Types.STRING).get();
                
            assertEquals(String[].class, elementArray.getClass());
            assertEquals(Arrays.asList(result), Arrays.asList(elementArray));
        }

        {
            Set<String> elementSet =
                Op.on(elementList).toSet().get();
                
            assertEquals(Arrays.asList(result), new ArrayList<String>(elementSet));
        }
        
        {
            String[] elementArray = 
                elementList.toArray(new String[elementList.size()]);
            
            assertEquals(String[].class, elementArray.getClass());
            assertEquals(Arrays.asList(result), Arrays.asList(elementArray));
        }

    }


    
    
    @Test
    public void testOP4J_013() throws Exception {
        // Removing duplicates from an array (or list)

        final String[] result = new String[] { "Lisboa", "Madrid", "Paris", "Bruxelles" };

        String[] capitals = new String[] { "Lisboa", "Madrid", "Paris", "Lisboa", "Bruxelles" };
        String[] originalCapitals = (String[]) ArrayUtils.clone(capitals);

        {
            capitals = Op.on(capitals).distinct().get();
                
            assertEquals(String[].class, capitals.getClass());
            assertEquals(Arrays.asList(result), Arrays.asList(capitals));
        }

        {
            List<String> capitalList = Arrays.asList(originalCapitals);
            
            // capitalList == LIST [ "Lisboa", "Madrid", "Paris", "Lisboa", "Bruxelles" ]
            capitalList = Op.on(capitalList).distinct().get();
                
            assertEquals(Arrays.asList(result), capitalList);
        }
        
        {
            Set<String> capitalSet = new LinkedHashSet<String>();
            for (String capital : capitals) {
                capitalSet.add(capital);
            }
            capitals = capitalSet.toArray(new String[capitalSet.size()]);
            
            assertEquals(String[].class, capitals.getClass());
            assertEquals(Arrays.asList(result), Arrays.asList(capitals));
        }

    }


    
    
    @Test
    public void testOP4J_014() throws Exception {
        // Sorting an array

        final String[] result = new String[] { "Arctic", "Atlantic", "Indian", "Pacific", "Southern" };

        String[] oceans = new String[] { "Pacific", "Atlantic", "Indian", "Arctic", "Southern" };
        String[] originalOceans = (String[]) ArrayUtils.clone(oceans);

        {
            oceans = Op.on(oceans).sort().get();
                
            assertEquals(String[].class, oceans.getClass());
            assertEquals(Arrays.asList(result), Arrays.asList(oceans));
        }
        
        oceans = (String[]) ArrayUtils.clone(originalOceans);
        
        {
            List<String> oceansList = Arrays.asList(oceans);
            Collections.sort(oceansList);
            oceans = oceansList.toArray(new String[oceansList.size()]);
            
            assertEquals(String[].class, oceans.getClass());
            assertEquals(Arrays.asList(result), Arrays.asList(oceans));
        }

    }

    
    
    
    @Test
    public void testOP4J_015() throws Exception {
        // Adding an element to an array

        final String[] result = new String[] { "Lettuce", "Tomato", "Onion", "Olive Oil" };
        String[] ingredients = new String[] { "Lettuce", "Tomato", "Onion" };
        String[] originalIngredients = (String[]) ArrayUtils.clone(ingredients);
        
        {
            
            ingredients = Op.on(ingredients).add("Olive Oil").get();

            assertEquals(String[].class, ingredients.getClass());
            assertEquals(Arrays.asList(result), Arrays.asList(ingredients));
            
        }
        
        ingredients = (String[]) ArrayUtils.clone(originalIngredients);
        
        {
            final String[] result2 = new String[] { "Lettuce", "Tomato", "Onion", "Olive Oil", "Balsamic Vinegar" };
            
            ingredients = Op.on(ingredients).addAll("Olive Oil", "Balsamic Vinegar").get();

            assertEquals(String[].class, ingredients.getClass());
            assertEquals(Arrays.asList(result2), Arrays.asList(ingredients));
            
        }
        
        ingredients = (String[]) ArrayUtils.clone(originalIngredients);
        
        {
            ingredients = Arrays.copyOf(ingredients, ingredients.length + 1);
            ingredients[ingredients.length - 1] = "Olive Oil";

            assertEquals(String[].class, ingredients.getClass());
            assertEquals(Arrays.asList(result), Arrays.asList(ingredients));
            
        }

    }

    
    
    
    
    
    
    @Test
    public void testOP4J_016() throws Exception {
        // Adding an element to an array at a specific position

        final String[] result = new String[] { "Talc", "Fluorite", "Quartz", "Diamond" };
        String[] minerals = new String[] { "Talc", "Quartz", "Diamond" };
        String[] originalMinerals = (String[]) ArrayUtils.clone(minerals);
        
        {
            
            minerals = Op.on(minerals).insert(1 ,"Fluorite").get();

            assertEquals(String[].class, minerals.getClass());
            assertEquals(Arrays.asList(result), Arrays.asList(minerals));
            
        }
        
        minerals = (String[]) ArrayUtils.clone(originalMinerals);
        
        {
            final String[] result2 = new String[] { "Talc", "Fluorite", "Apatite", "Quartz", "Diamond" };
            
            minerals = Op.on(minerals).insertAll(1, "Fluorite", "Apatite").get();

            assertEquals(String[].class, minerals.getClass());
            assertEquals(Arrays.asList(result2), Arrays.asList(minerals));
            
        }
        
        minerals = (String[]) ArrayUtils.clone(originalMinerals);
        
        {
            minerals = Arrays.copyOf(minerals, minerals.length + 1);
            for (int i = (minerals.length - 1), z = 1; i > z; i--) {
                minerals[i] = minerals[i - 1];
            }
            minerals[1] = "Fluorite";

            assertEquals(String[].class, minerals.getClass());
            assertEquals(Arrays.asList(result), Arrays.asList(minerals));
            
        }

    }

    
    
    
    
    
    
    @Test
    public void testOP4J_017() throws Exception {
        // Removing all accents (and other diacritics) from a String

        String[] conts = 
            new String[] { "\u00C1frica", "Am\u00E9rica", "Ant\u00E1rtida", "Asia", "Europa", "Ocean\u00EDa" };
        final String[] originalConts = conts.clone();

        String[] result =
            new String[] { "Africa", "America", "Antartida", "Asia", "Europa", "Oceania" };
        String[] capsResult =
            new String[] { "AFRICA", "AMERICA", "ANTARTIDA", "ASIA", "EUROPA", "OCEANIA" };
        
        {
            
            conts = Op.on(conts).map(FnString.asciify()).get();

            assertEquals(String[].class, conts.getClass());
            assertEquals(Arrays.asList(result), Arrays.asList(conts));
            
        }
        
        conts = originalConts.clone();
        
        {
            
            conts = Op.on(conts).forEach().exec(FnString.asciify()).exec(FnString.toUpperCase()).get();

            assertEquals(String[].class, conts.getClass());
            assertEquals(Arrays.asList(capsResult), Arrays.asList(conts));
            
        }
        
        conts = originalConts.clone();
        
        {
            
            conts = Op.on(conts).map(FnFunc.chain(FnString.asciify(), FnString.toUpperCase())).get();

            assertEquals(String[].class, conts.getClass());
            assertEquals(Arrays.asList(capsResult), Arrays.asList(conts));
            
        }

    }

    
    
    
    
        
    
    @Test
    @SuppressWarnings("boxing")
    public void testOP4J_018() throws Exception {
        // Creating a Calendar from day, month and year

        {
            
            Calendar result = Calendar.getInstance();
            result.clear();
            result.set(Calendar.DAY_OF_MONTH, 12);
            result.set(Calendar.MONTH, Calendar.OCTOBER);
            result.set(Calendar.YEAR, 1492);
            
            Calendar date = Op.onListFor(1492, 10, 12).exec(FnCalendar.fieldIntegerListToCalendar()).get();
            
            assertEquals(result.getTimeInMillis(), date.getTimeInMillis());
            
        }

        {
            
            Calendar result = Calendar.getInstance();
            result.clear();
            result.set(Calendar.DAY_OF_MONTH, 12);
            result.set(Calendar.MONTH, Calendar.OCTOBER);
            result.set(Calendar.YEAR, 1492);
            result.set(Calendar.HOUR_OF_DAY, 2);
            result.set(Calendar.MINUTE, 34);
            
            Calendar date = Op.onListFor(1492, 10, 12, 2, 34).exec(FnCalendar.fieldIntegerListToCalendar()).get();
            
            assertEquals(result.getTimeInMillis(), date.getTimeInMillis());
            
        }


    }

    
    
    @Test
    public void testOP4J_019() throws Exception {
        // Extract some text from a String using a regular expression

        final String[] books = 
            new String[] {"Title=The Origin of Species; Price=24.90EUR",
                          "Title=Odyssey; Price=13.50EUR",
                          "Title=A Midsummer Night's Dream; Price=18.20EUR" };
        
        final String[] resultTitles = 
            new String[] {"The Origin of Species",
                          "Odyssey",
                          "A Midsummer Night's Dream" };
        
        {

            final String regex = "Title=(.*?); Price(.*)";
            
            String[] titles = Op.on(books).forEach().exec(FnString.matchAndExtract(regex, 1)).get();
            
            assertEquals(String[].class, titles.getClass());
            assertEquals(Arrays.asList(resultTitles), Arrays.asList(titles));
            
        }
        
        {

            final String regex = "Title=(.*?); Price(.*)";
            
            String[] titles = Op.on(books).map(FnString.matchAndExtract(regex, 1)).get();
            
            assertEquals(String[].class, titles.getClass());
            assertEquals(Arrays.asList(resultTitles), Arrays.asList(titles));
            
        }

        
        {

            final String regex = "(.*?)=(.*?); (.*?)=(.*?)";
            
            List<Map<String,String>> bookInfo = 
                Op.on(books).toList().forEach().
                    exec(FnString.matchAndExtractAll(regex, 1,2,3,4)).
                    exec(FnList.ofString().couple()).get();
            
            assertEquals(3, bookInfo.size());
            assertEquals("Title", bookInfo.get(0).entrySet().iterator().next().getKey());
            assertEquals(resultTitles[0], bookInfo.get(0).get("Title"));
            assertEquals("24.90EUR", bookInfo.get(0).get("Price"));
            
        }

    }
    

    

    
    
    @Test
    @SuppressWarnings("boxing")
    public void testOP4J_020() throws Exception {
        // Creating a map from its elements
    
        Map<String,String> result = new LinkedHashMap<String, String>();
        result.put("James Cheddar", "Finance");
        result.put("Richard Stilton", "Engineering");
        result.put("Bernard Brie", "Marketing");
        result.put("Antonio Cabrales", "Sales");
        
        
        {
            
            Map<String,String> peopleDept =
                Op.onMapFor("James Cheddar", "Finance").
                        and("Richard Stilton", "Engineering").
                        and("Bernard Brie", "Marketing").
                        and("Antonio Cabrales", "Sales").get();
                        
            assertEquals(result, peopleDept);
            
        }
        
        {
            
            Map<String,String> peopleDept =
                Op.onListFor(
                        "James Cheddar",  "Finance",   "Richard Stilton",  "Engineering",
                        "Bernard Brie",   "Marketing", "Antonio Cabrales", "Sales").
                        couple().get();
                        
            assertEquals(result, peopleDept);
            
        }
        
        {
            
            Map<String,Integer> peopleYearsInCoRes = new LinkedHashMap<String, Integer>();
            peopleYearsInCoRes.put("James Cheddar", 12);
            peopleYearsInCoRes.put("Richard Stilton", 2);
            peopleYearsInCoRes.put("Bernard Brie", 7);
            peopleYearsInCoRes.put("Antonio Cabrales", 9);
            
            Map<String,Integer> peopleYearsInCo =
                Op.onMapFor("James Cheddar", 12).
                        and("Richard Stilton", 2).
                        and("Bernard Brie", 7).
                        and("Antonio Cabrales", 9).get();
                        
            assertEquals(peopleYearsInCoRes, peopleYearsInCo);
            
        }
        

    }

    
    @Test
    public void testOP4J_021() throws Exception {
        // Creating a list with the results of calling a method on each element of another list
        
        final String[] namesArr = 
            new String[] { "James Cheddar", "Richard Stilton", "Bernard Brie", "Antonio Cabrales" };
        
        final User user1 = new User(namesArr[0]);
        final User user2 = new User(namesArr[1]);
        final User user3 = new User(namesArr[2]);
        final User user4 = new User(namesArr[3]);
        
        final List<User> users = Arrays.asList(new User[] { user1, user2, user3, user4 });
        final List<String> namesResult = Arrays.asList(namesArr);
        
        {
            
            List<String> names =
                Op.on(users).map(Get.attrOfString("name")).get();
            
            assertEquals(namesResult, names);
            
        }
        
        {
            
            List<String> names =
                Op.on(users).map(Call.methodForString("getName")).get();
            
            assertEquals(namesResult, names);
            
        }
        
        {
            
            List<String> names =
                Op.on(users).forEach().exec(Get.attrOfString("name")).get();
            
            assertEquals(namesResult, names);
            
        }
        
        {
            
            List<String> names = new ArrayList<String>();
            for (User user : users) {
                names.add(user.getName());
            }
            
            assertEquals(namesResult, names);
            
        }
        
        {
            
            User[] usersArray = users.toArray(new User[users.size()]);

            Type<User> userType = Types.forClass(User.class);
            String[] names =
                Op.onArrayOf(userType, usersArray).
                    map(Types.STRING, Get.attrOfString("name")).get();
            
            assertEquals(namesResult, Arrays.asList(names));
            
        }
        
        {
            
            List<String> namesList = new ArrayList<String>();
            for (User user : users) {
                namesList.add(user.getName());
            }
            String[] names = namesList.toArray(new String[namesList.size()]);
            
            assertEquals(namesResult, Arrays.asList(names));
            
        }
        
    }
    
    
    
    
    @Test
    public void testOP4J_022() throws Exception {
        // Executing an OGNL expression on an array of Strings
        
        final String[] riverNames = new String[] { "Eume", "Ulla", "Tambre" };
        final String[] riversResult = new String[] { "River Eume", "River Ulla", "River Tambre" };
        
        {
            
            String[] rivers = 
                Op.on(riverNames).map(FnOgnl.evalForString("'River ' + #target")).get();
            
            assertEquals(Arrays.asList(riversResult), Arrays.asList(rivers));
            
        }
        
        {
            
            Function<String[],String[]> riverize = 
                Fn.on(Types.ARRAY_OF_STRING).map(FnOgnl.evalForString("'River ' + #target")).get();
            
            String[] rivers = riverize.execute(riverNames);
            
            assertEquals(Arrays.asList(riversResult), Arrays.asList(rivers));
            
        }
        
    }
    
        
        
    @Test
    public void testOP4J_023() throws Exception {
        // Grouping a list of objects by the value of one of their attributes

        List<City> cities = new ArrayList<City>();
        City city1 = new City("Spain", "Santiago");
        cities.add(city1);
        City city3 = new City("France", "Marseille");
        cities.add(city3);
        City city4 = new City("Portugal", "Porto");
        cities.add(city4);
        City city2 = new City("Spain", "Barcelona");
        cities.add(city2);
        City city5 = new City("Portugal", "Lisboa");
        cities.add(city5);
        City city6 = new City("Portugal", "Viseu");
        cities.add(city6);
        
        Map<String,List<City>> result = new LinkedHashMap<String,List<City>>();
        result.put("Spain", Arrays.asList(new City[] {city1, city2}));
        result.put("France", Arrays.asList(new City[] {city3}));
        result.put("Portugal", Arrays.asList(new City[] {city4 ,city5, city6}));
        
        {
            
            Map<String,List<City>> citiesByCountry =
                Op.on(cities).zipAndGroupKeysBy(Get.attrOfString("country")).get();
            
            assertEquals(result, citiesByCountry);
            
        }
        
        {
            
            Map<String,List<City>> citiesByCountry =
                Op.on(cities).zipAndGroupKeysBy(Call.methodForString("getCountry")).get();
            
            assertEquals(result, citiesByCountry);
            
        }
        
        {
            
            Map<String,List<City>> citiesByCountry =
                new LinkedHashMap<String, List<City>>();
            for (City city : cities) {
                List<City> citiesForCountry = citiesByCountry.get(city.getCountry());
                if (citiesForCountry == null) {
                    citiesForCountry = new ArrayList<City>();
                    citiesByCountry.put(city.getCountry(), citiesForCountry);
                }
                citiesForCountry.add(city);
            }

            assertEquals(result, citiesByCountry);
            
        }
        
    }

    
    
    
    @Test
    public void testOP4J_XXX() throws Exception {

        List<City> cities = new ArrayList<City>();
        City city1 = new City("Spain", "Santiago");
        cities.add(city1);
        City city3 = new City("France", "Marseille");
        cities.add(city3);
        City city4 = new City("Portugal", "Porto");
        cities.add(city4);
        City city2 = new City("Spain", "Barcelona");
        cities.add(city2);
        City city5 = new City("Portugal", "Lisboa");
        cities.add(city5);
        City city6 = new City("Portugal", "Viseu");
        cities.add(city6);
        
        Map<String,List<String>> namesResult = new LinkedHashMap<String,List<String>>();
        namesResult.put("Spain", 
                Arrays.asList(new String[] {city1.getName(), city2.getName()}));
        namesResult.put("France", 
                Arrays.asList(new String[] {city3.getName()}));
        namesResult.put("Portugal", 
                Arrays.asList(new String[] {city4.getName(), city5.getName(), city6.getName()}));
        
        {
            
            Map<String,List<String>> cityNamesByCountry =
                Op.on(cities).toGroupMap(
                        Get.attrOfString("country"), Get.attrOfString("name")).get();
            
            assertEquals(namesResult, cityNamesByCountry);
            
        }
        
    }    
    

    
    
    
    
    /*
     * 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++
     *   AUXILIARY CLASSES
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 
     */
    
    public static class User {
        
        private final String name;
        
        public User() {
            super();
            this.name = "[none]";
        }
        
        public User(final String name) {
            super();
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
    }

    
    
    public static class City {
        private final String country;
        private final String name;
        public City(String country, String name) {
            super();
            this.country = country;
            this.name = name;
        }
        public String getCountry() {
            return this.country;
        }
        public String getName() {
            return this.name;
        }
        
    }

    
    
}

