package org.op4j;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;
import org.javaruntype.type.Type;
import org.javaruntype.type.Types;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.op4j.functions.Call;
import org.op4j.functions.DecimalPoint;
import org.op4j.functions.ExecCtx;
import org.op4j.functions.Fn;
import org.op4j.functions.FnCalendar;
import org.op4j.functions.FnFunc;
import org.op4j.functions.FnInteger;
import org.op4j.functions.FnList;
import org.op4j.functions.FnNumber;
import org.op4j.functions.FnObject;
import org.op4j.functions.FnString;
import org.op4j.functions.Function;
import org.op4j.functions.Get;
import org.op4j.functions.IFunction;
import org.op4j.jodatime.functions.FnDateMidnight;
import org.op4j.ognl.functions.FnOgnl;
import org.op4j.operators.impl.op.generic.Level0GenericUniqOperator;

public class RecipesTests extends TestCase {

	
	
	@Test
	public void testOP4J_001() throws Exception {
	    // Creating a list from its elements
	    
	    List<String> result = Arrays.asList(new String[] {"hello", "hola", "ola", "olá"});
	    
	    List<String> stringList = 
	        Op.onListFor("hello", "hola", "ola", "olá").get();
	    
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
                Fn.onArrayOf(Types.STRING).map(FnOgnl.evalForString("'River ' + #target")).get();
            
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
    public void testOP4J_024() throws Exception {
        // Executing an op4j function directly (without an expression)

        String value = "Saint-&Eacute;tienne est une ville de France";
        String result = "Saint-\u00C9tienne est une ville de France";

        {
            
            value = Op.on(value).exec(FnString.unescapeHTML()).get();
            
            assertEquals(result, value);
            
        }

        {
            
            value = FnString.unescapeHTML().execute(value);
            
            assertEquals(result, value);
            
        }
        
    }


    
    @Test
    public void testOP4J_025() throws Exception {
        // Building a map from a list by executing functions on its elements

        List<Country> countries = new ArrayList<Country>();
        Country country1 = new Country("Spain", Integer.valueOf(45989016));
        countries.add(country1);
        Country country2 = new Country("France", Integer.valueOf(65447374));
        countries.add(country2);
        Country country3 = new Country("Portugal", Integer.valueOf(10707924));
        countries.add(country3);
        Country country4 = new Country("United Kingdom", Integer.valueOf(62041708));
        countries.add(country4);
        Country country5 = new Country("Ireland", Integer.valueOf(4459300));
        countries.add(country5);

        
        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
        result.put("Spain", Integer.valueOf(45989016));
        result.put("France", Integer.valueOf(65447374));
        result.put("Portugal", Integer.valueOf(10707924));
        result.put("United Kingdom", Integer.valueOf(62041708));
        result.put("Ireland", Integer.valueOf(4459300));
        
        {
            
            Map<String,Integer> populationByCountry =
                Op.on(countries).
                    toMap(Get.attrOfString("name"), Get.attrOfInteger("population")).get();
            
            assertEquals(result, populationByCountry);
            
        }
        
        {
            
            Map<String,Integer> populationByCountry = new LinkedHashMap<String, Integer>();
            for (Country country : countries) {
                populationByCountry.put(country.getName(), country.getPopulation());
            }
            
            assertEquals(result, populationByCountry);
            
        }
        
        {
            
            
            Map<String, Integer> sortedResult = new LinkedHashMap<String, Integer>();
            sortedResult.put("France", Integer.valueOf(65447374));
            sortedResult.put("United Kingdom", Integer.valueOf(62041708));
            sortedResult.put("Spain", Integer.valueOf(45989016));
            sortedResult.put("Portugal", Integer.valueOf(10707924));
            sortedResult.put("Ireland", Integer.valueOf(4459300));
            
            
            Map<String,Integer> populationByCountry =
                Op.on(countries).
                    sortBy(Get.attrOfInteger("population")).reverse().
                    toMap(Get.attrOfString("name"), Get.attrOfInteger("population")).get();
            
            
            
            assertEquals(
                    new ArrayList<Map.Entry<String,Integer>>(sortedResult.entrySet()), 
                    new ArrayList<Map.Entry<String,Integer>>(populationByCountry.entrySet()));
            
        }
        
        
        {
            
            
            Map<String, Integer> sortedResult = new LinkedHashMap<String, Integer>();
            sortedResult.put("France", Integer.valueOf(65447374));
            sortedResult.put("United Kingdom", Integer.valueOf(62041708));
            sortedResult.put("Spain", Integer.valueOf(45989016));
            sortedResult.put("Portugal", Integer.valueOf(10707924));
            sortedResult.put("Ireland", Integer.valueOf(4459300));
            
            
            Map<String,Integer> populationByCountry =
                Op.on(countries).
                    toMap(Get.attrOfString("name"), Get.attrOfInteger("population")).
                    sortBy(Get.attrOfInteger("value")).reverse().get();
            
            
            
            assertEquals(
                    new ArrayList<Map.Entry<String,Integer>>(sortedResult.entrySet()), 
                    new ArrayList<Map.Entry<String,Integer>>(populationByCountry.entrySet()));
            
        }
        
        
        
        
    }
    

    
    @Test
    public void testOP4J_026() throws Exception {
        // Modifying some elements in an array (depending on a specific condition)

        
        List<String> herbs = Arrays.asList(new String[] {"*parsley*", "BASIL", "*Coriander*", "Spearmint" });
        List<String> result = Arrays.asList(new String[] {"Parsley (sold out!)", "Basil", "Coriander (sold out!)", "Spearmint" });

        {
            herbs = 
                Op.on(herbs).forEach().
                    ifTrue(FnString.matches("\\*.*?\\*")).
                        exec(FnString.matchAndExtract("\\*(.*?)\\*",1)).
                        exec(FnOgnl.evalForString("#target + ' (sold out!)'")).
                    endIf().exec(FnFunc.chain(FnString.toLowerCase(),FnString.capitalize())).get();
            
            assertEquals(result, herbs);
            
        }

        herbs = Arrays.asList(new String[] {"*parsley*", "BASIL", "*Coriander*", "Spearmint" });
        {
            herbs = 
                Op.on(herbs).forEach().
                    execIfTrue(
                        FnString.matches("\\*.*?\\*"),
                        FnFunc.chain(
                                FnString.matchAndExtract("\\*(.*?)\\*",1),
                                FnOgnl.evalForString("#target + ' (sold out!)'"))).
                    exec(FnFunc.chain(FnString.toLowerCase(),FnString.capitalize())).get();
            
            assertEquals(result, herbs);
            
        }

        herbs = Arrays.asList(new String[] {"*parsley*", "BASIL", "*Coriander*", "Spearmint" });
        result = Arrays.asList(new String[] {"Parsley (sold out!)", "Basil (on sale)", "Coriander (sold out!)", "Spearmint (on sale)" });
        {
            herbs = 
                Op.on(herbs).forEach().
                    execIfTrue(
                        FnString.matches("\\*.*?\\*"),
                        FnFunc.chain(
                                FnString.matchAndExtract("\\*(.*?)\\*",1),
                                FnOgnl.evalForString("#target + ' (sold out!)'")),
                        FnOgnl.evalForString("#target + ' (on sale)'")).
                    exec(FnFunc.chain(FnString.toLowerCase(),FnString.capitalize())).get();
            
            assertEquals(result, herbs);
            
        }

        herbs = Arrays.asList(new String[] {"*parsley*", "BASIL", "*Coriander*", "Spearmint" });
        result = Arrays.asList(new String[] {"Parsley (sold out!)", "Basil (on sale)", "Coriander (sold out!)", "Spearmint (on sale)" });
        {
            Function<List<String>,List<String>> processHerbNames = 
                Fn.onListOf(Types.STRING).forEach().
                    execIfTrue(
                        FnString.matches("\\*.*?\\*"),
                        FnFunc.chain(
                                FnString.matchAndExtract("\\*(.*?)\\*",1),
                                FnOgnl.evalForString("#target + ' (sold out!)'")),
                        FnOgnl.evalForString("#target + ' (on sale)'")).
                    exec(FnFunc.chain(FnString.toLowerCase(),FnString.capitalize())).get();
            
            herbs = processHerbNames.execute(herbs);
            
            assertEquals(result, herbs);
            
        }

        
    }

    
    
    
    @Test
    public void testOP4J_027() throws Exception {
        // Checking if all the members of an array meet a specific condition
        
        Integer[] values = new Integer[] { 100, 23, 587 };

        {
            Boolean result = 
                Op.on(values).all(FnInteger.lessThan(800)).get();
            
            assertEquals(Boolean.TRUE, result);
            
        }

    }
    
    
    
    
    @Test
    public void testOP4J_028() throws Exception {
        // Converting the keys in a map
        
        Map<String,String> map = new LinkedHashMap<String, String>();
        map.put("1", "one");
        map.put("2", "two");
        map.put("3", "three");

        
        {
            Map<Integer,String> result = new LinkedHashMap<Integer, String>();
            result.put(Integer.valueOf(1), "one");
            result.put(Integer.valueOf(2), "two");
            result.put(Integer.valueOf(3), "three");
            
            Map<Integer,String> newMap = 
                Op.on(map).forEachEntry().onKey().exec(FnString.toInteger()).get();
            
            assertEquals(result, newMap);
            
        }        
    }

    
    @Test
    public void testOP4J_029() throws Exception {
        // Convert a List<String> to List<Calendar>, keep only dates from 2010 and generate a list map with them using
        //the month as key
        
        List<String> asString = new ArrayList<String>();
        asString.add("20100505");
        asString.add("20100614");
        asString.add("19980407");
        asString.add("20100209");
        asString.add("20100215");
        asString.add("20110101");
        asString.add("20100620");
        asString.add("20100301");        
        
        
        List<Calendar> asCalendar;
        {
            // Convert List<String> into List<Calendar>            
            asCalendar = Op.on(asString).forEach()
                .exec(FnString.toCalendar("yyyyMMdd")).get();
            
            List<Calendar> result = Arrays.asList(
                    new DateTime(2010, 5, 5, 0, 0, 0, 0).toCalendar(Locale.getDefault()),
                    new DateTime(2010, 6, 14, 0, 0, 0, 0).toCalendar(Locale.getDefault()),
                    new DateTime(1998, 4, 7, 0, 0, 0, 0).toCalendar(Locale.getDefault()),
                    new DateTime(2010, 2, 9, 0, 0, 0, 0).toCalendar(Locale.getDefault()),
                    new DateTime(2010, 2, 15, 0, 0, 0, 0).toCalendar(Locale.getDefault()),
                    new DateTime(2011, 1, 1, 0, 0, 0, 0).toCalendar(Locale.getDefault()),
                    new DateTime(2010, 6, 20, 0, 0, 0, 0).toCalendar(Locale.getDefault()),
                    new DateTime(2010, 3, 1, 0, 0, 0, 0).toCalendar(Locale.getDefault()));            
            assertEquals(result, asCalendar);            
        }       
        
        {
            // Filter List<Calendar> by keeping only 2010 dates
            asCalendar = Op.on(asCalendar)
                .removeAllTrue(FnObject.lessThan(new DateTime(2010, 1, 1, 0, 0, 0, 0)
                    .toCalendar(Locale.getDefault())))
                .removeAllTrue(FnObject.greaterOrEqTo(new DateTime(2011, 1, 1, 0, 0, 0, 0)
                    .toCalendar(Locale.getDefault()))).get();
            
            List<Calendar> result = Arrays.asList(
                    new DateTime(2010, 5, 5, 0, 0, 0, 0).toCalendar(Locale.getDefault()),
                    new DateTime(2010, 6, 14, 0, 0, 0, 0).toCalendar(Locale.getDefault()),
                    new DateTime(2010, 2, 9, 0, 0, 0, 0).toCalendar(Locale.getDefault()),
                    new DateTime(2010, 2, 15, 0, 0, 0, 0).toCalendar(Locale.getDefault()),
                    new DateTime(2010, 6, 20, 0, 0, 0, 0).toCalendar(Locale.getDefault()),
                    new DateTime(2010, 3, 1, 0, 0, 0, 0).toCalendar(Locale.getDefault()));            
            assertEquals(result, asCalendar);            
        }
        
        {
            // Create a map of list with the month as its key
            Map<Integer, List<Calendar>> output = Op.on(asCalendar)
                .zipAndGroupKeysBy(Call.methodForInteger("get", Calendar.MONTH)).get();
            
            Map<Integer, List<Calendar>> result = new LinkedHashMap<Integer, List<Calendar>>();
            result.put(Integer.valueOf(1), Arrays.asList(
                    new DateTime(2010, 2, 9, 0, 0, 0, 0).toCalendar(Locale.getDefault()),
                    new DateTime(2010, 2, 15, 0, 0, 0, 0).toCalendar(Locale.getDefault())));
            result.put(Integer.valueOf(2), Arrays.asList(
                    new DateTime(2010, 3, 1, 0, 0, 0, 0).toCalendar(Locale.getDefault())));
            result.put(Integer.valueOf(4), Arrays.asList(
                    new DateTime(2010, 5, 5, 0, 0, 0, 0).toCalendar(Locale.getDefault())));
            result.put(Integer.valueOf(5), Arrays.asList(
                    new DateTime(2010, 6, 14, 0, 0, 0, 0).toCalendar(Locale.getDefault()),
                    new DateTime(2010, 6, 20, 0, 0, 0, 0).toCalendar(Locale.getDefault())));
            assertEquals(result, output);
        }
        
        Map<Integer, List<Calendar>> output = Op.on(asString).forEach()
            .exec(FnString.toCalendar("yyyyMMdd")).endFor()
            .removeAllTrue(FnObject.lessThan(new DateTime(2010, 1, 1, 0, 0, 0, 0).toCalendar(Locale.getDefault())))
            .removeAllTrue(FnObject.greaterOrEqTo(new DateTime(2011, 1, 1, 0, 0, 0, 0).toCalendar(Locale.getDefault())))
            .zipAndGroupKeysBy(Call.methodForInteger("get", Calendar.MONTH)).get();
        
    }
    
    
    @Test
    public void testOP4J_030() throws Exception {
        // Create a map with two keys: "VALID" with the strings valid as integer and "INVALID" with the not valid ones
        // Convert the strings valid as integer to integer
        
        List<String> list = new ArrayList<String>();
        list.add("5");
        list.add("3.4");
        list.add("89.7");
        list.add("-13.999");
        list.add("5f7");
        list.add("537");
        list.add("323a");
        list.add("3,23");
        
        Map<String, List<String>> result = new LinkedHashMap<String, List<String>>();
        result.put("VALID", Arrays.asList(new String[] {"5", "3.4", "89.7", "-13.999", "537", "3,23"}));
        result.put("INVALID", Arrays.asList(new String[] {"5f7", "323a"}));
        
        {
            Map<String, List<String>> validInvalidIntegers = Op.on(list).zipAndGroupKeysBy(new IFunction<String, String>() {
                    public String execute(String input, ExecCtx ctx) throws Exception {
                        return Op.on(input).exec(FnString.isInteger(DecimalPoint.IS_POINT)).get().booleanValue() 
                            ? "VALID" : "INVALID";
                    }
                }).get();
            
            assertEquals(result, validInvalidIntegers);            
        }       
        
        {
            Map<String, List<String>> validInvalidIntegers = Op.on(list).zipAndGroupKeysBy(new IFunction<String, String>() {
                    public String execute(String input, ExecCtx ctx) throws Exception {
                        return Op.on(input).exec(FnString.isInteger(DecimalPoint.IS_COMMA)).get().booleanValue() 
                            ? "VALID" : "INVALID";
                    }
                }).get();
            
            assertEquals(result, validInvalidIntegers);            
        }
        
        {
        
            List<Integer> resultAsIntegerIfComma = Arrays.asList(new Integer[]{
                    Integer.valueOf(5), Integer.valueOf(34), Integer.valueOf(897),
                    Integer.valueOf(-13999), Integer.valueOf(537), Integer.valueOf(3)
            });
            
            List<Integer> asIntegerIfComma = Op.on(result.get("VALID")).forEach()
                .exec(FnString.toInteger(DecimalPoint.IS_COMMA)).get();
            
            assertEquals(resultAsIntegerIfComma, asIntegerIfComma);
        
        }
        
        {
            List<Integer> resultAsIntegerIfPoint = Arrays.asList(new Integer[]{
                    Integer.valueOf(5), Integer.valueOf(3), Integer.valueOf(89),
                    Integer.valueOf(-13), Integer.valueOf(537), Integer.valueOf(323)
            });
        
            List<Integer> asIntegerIfPoint = Op.on(result.get("VALID")).forEach()
                .exec(FnString.toInteger(DecimalPoint.IS_POINT)).get();
            assertEquals(resultAsIntegerIfPoint, asIntegerIfPoint);
        }
    }
    
    
    @Test
    public void testOP4J_031() throws Exception {
        // Given a List<String>, create a map with two keys: "VALID" with the strings valid as a percentage between 0 and 100
        // and "INVALID" with the not valid ones
        
        List<String> list = new ArrayList<String>();
        list.add("5");
        list.add("3.4");
        list.add("-13");
        list.add("0");
        list.add("5k7");
        list.add("ten");
        list.add("32");
        list.add("1,2");
        
        Map<String, List<String>> result = new LinkedHashMap<String, List<String>>();
        result.put("VALID", Arrays.asList(new String[] {"5", "3.4", "0", "32", "1,2"}));
        result.put("INVALID", Arrays.asList(new String[] {"-13", "5k7", "ten"}));
        
        {
            Map<String, List<String>> validInvalidPercentages = Op.on(list).zipAndGroupKeysBy(new IFunction<String, String>() {
                    public String execute(String input, ExecCtx ctx) throws Exception {
                        return Op.on(input).exec(FnString.isInteger(DecimalPoint.IS_POINT)).get().booleanValue()
                            && Op.on(input).exec(FnString.toInteger(DecimalPoint.IS_POINT)).exec(FnNumber.between(0, 100)).get().booleanValue()
                                ? "VALID" : "INVALID";
                    }
                }).get();
            
            assertEquals(result, validInvalidPercentages);            
        }      
        
    }
    
    
    
    @Test
    public void testOP4J_032() throws Exception {
        // Given a List<String> where each string represents a date in the 
        //format mm/dd/yyyy, convert it to a List<DateMidnight>
       
        
        List<String> list = new ArrayList<String>();
        list.add("12/24/2000");
        list.add("02/02/2010");
        list.add("04/04/2002");
        list.add("11/22/2005");
        list.add("02/07/2005");
        list.add("03/05/2005");
        list.add("09/13/2006");
        list.add("12/29/2007");
            
        
        List<DateMidnight> result = new ArrayList<DateMidnight>();
        result.add(new DateMidnight(2000, 12, 24));
        result.add(new DateMidnight(2010, 2, 2));
        result.add(new DateMidnight(2002, 4, 4));
        result.add(new DateMidnight(2005, 11, 22));
        result.add(new DateMidnight(2005, 2, 7));
        result.add(new DateMidnight(2005, 3, 5));
        result.add(new DateMidnight(2006, 9, 13));
        result.add(new DateMidnight(2007, 12, 29));
        
        {
            String pattern = "MM/dd/yyyy";
            
            List<DateMidnight> datemidnights = Op.on(list).forEach()
                .exec(FnDateMidnight.strToDateMidnight(pattern)).get();
        
            assertEquals(result, datemidnights);            
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

    
    
    public static class Country {
        private final String name;
        private final Integer population;
        public Country(String name, Integer population) {
            super();
            this.name = name;
            this.population = population;
        }
        public String getName() {
            return this.name;
        }
        public Integer getPopulation() {
            return this.population;
        }
    }
    
    
    
}

