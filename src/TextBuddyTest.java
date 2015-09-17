import static org.junit.Assert.*;

import org.junit.*;

public class TextBuddyTest {

	@Test 
	public void testTextBuddy() {
		TextBuddy tb = new TextBuddy("mytextfile.txt");

		assertEquals("added to mytextfile.txt: \"this is first line.\"",
				tb.processUserCommand("add this is first line."));
		assertEquals("Invalid command encountered for: add", tb.processUserCommand("add"));
		assertEquals("1. this is first line.", tb.processUserCommand("display"));
		assertEquals(
				"added to mytextfile.txt: \"and here comes the second line.\"",
				tb.processUserCommand("add and here comes the second line."));
		assertEquals(
				"1. this is first line.\n2. and here comes the second line.",
				tb.processUserCommand("display"));
		assertEquals(
				"deleted from mytextfile.txt: \"and here comes the second line.\"",
				tb.processUserCommand("delete 2"));
		assertEquals("Invalid command encountered for: delete 123",
				tb.processUserCommand("delete 123"));
		assertEquals(
				"Invalid command encountered for: delete this is first line",
				tb.processUserCommand("delete this is first line"));
		assertEquals("1. this is first line.", tb.processUserCommand("display"));
		assertEquals("all content deleted from mytextfile.txt", tb.processUserCommand("clear"));
		assertEquals("mytextfile.txt is empty", tb.processUserCommand("display"));
	}
	
	@Test
	public void test2(){
		TextBuddy tb = new TextBuddy("mytextfile.txt");
		
		assertEquals("added to mytextfile.txt: \"hello world\"", tb.processUserCommand("add hello world"));
		assertEquals("1. hello world", tb.processUserCommand("display"));
		assertEquals("added to mytextfile.txt: \"this is a whole new world\"", tb.processUserCommand("add this is a whole new world"));
		assertEquals("added to mytextfile.txt: \"what is this doing here?\"", tb.processUserCommand("add what is this doing here?"));
		assertEquals("deleted from mytextfile.txt: \"what is this doing here?\"", tb.processUserCommand("delete 3"));
		assertEquals("1. hello world\n2. this is a whole new world", tb.processUserCommand("display"));
		assertEquals("all content deleted from mytextfile.txt", tb.processUserCommand("clear"));
		assertEquals("mytextfile.txt is empty", tb.processUserCommand("display"));
		assertEquals("Invalid command encountered for: delete 2", tb.processUserCommand("delete 2"));
		assertEquals("Invalid command encountered for: wakakakaka", tb.processUserCommand("wakakakaka"));
		assertEquals("mytextfile.txt is empty", tb.processUserCommand("display"));
		assertEquals("added to mytextfile.txt: \"minions are cute\"", tb.processUserCommand("add minions are cute"));
		assertEquals("added to mytextfile.txt: \"i want to catch a pikachu\"", tb.processUserCommand("add i want to catch a pikachu"));
		assertEquals("Invalid command encountered for: delete 0", tb.processUserCommand("delete 0"));
		assertEquals("1. minions are cute\n2. i want to catch a pikachu", tb.processUserCommand("display"));
		assertEquals("added to mytextfile.txt: \"i throw a pokeball\"", tb.processUserCommand("add i throw a pokeball"));
		assertEquals("deleted from mytextfile.txt: \"minions are cute\"", tb.processUserCommand("delete 1"));
		assertEquals("1. i want to catch a pikachu\n2. i throw a pokeball", tb.processUserCommand("display"));
		assertEquals("added to mytextfile.txt: \"pikachu caught!\"", tb.processUserCommand("add pikachu caught!"));
		assertEquals("1. i want to catch a pikachu\n2. i throw a pokeball\n3. pikachu caught!", tb.processUserCommand("display"));
		assertEquals("all content deleted from mytextfile.txt", tb.processUserCommand("clear"));
		assertEquals("mytextfile.txt is empty", tb.processUserCommand("display"));
	}
	
	@Test
	public void test3(){
		TextBuddy tb = new TextBuddy("mytextfile.txt");
		
		assertEquals("added to mytextfile.txt: \"hello world\"", tb.processUserCommand("add hello world"));
		assertEquals("added to mytextfile.txt: \"i am second\"", tb.processUserCommand("add i am second"));
		assertEquals("added to mytextfile.txt: \"third\"", tb.processUserCommand("add third"));
		assertEquals("added to mytextfile.txt: \"for the forth fourth\"", tb.processUserCommand("add for the forth fourth"));
		assertEquals("added to mytextfile.txt: \"HI5\"", tb.processUserCommand("add HI5"));
		assertEquals("1. hello world\n2. i am second\n3. third\n4. for the forth fourth\n5. HI5", tb.processUserCommand("display"));
		assertEquals("1. hello world\n2. i am second\n3. third\n4. for the forth fourth\n5. HI5", tb.processUserCommand("display"));
		assertEquals("deleted from mytextfile.txt: \"i am second\"", tb.processUserCommand("delete 2"));
		assertEquals("1. hello world\n2. third\n3. for the forth fourth\n4. HI5", tb.processUserCommand("display"));
		assertEquals("deleted from mytextfile.txt: \"for the forth fourth\"", tb.processUserCommand("delete 3"));
		assertEquals("1. hello world\n2. third\n3. HI5", tb.processUserCommand("display"));
		
	}
	
	@Test 
	public void testNew(){
		TextBuddy tb = new TextBuddy("mytextfile.txt");
		assertEquals("added to mytextfile.txt: \"zebras are black and white\"", tb.processUserCommand("add zebras are black and white"));
		assertEquals("added to mytextfile.txt: \"apple of my eye\"", tb.processUserCommand("add apple of my eye"));
		assertEquals("mytextfile.txt is sorted", tb.processUserCommand("sort"));
		assertEquals("1. apple of my eye\n2. zebras are black and white", tb.processUserCommand("display"));
		assertEquals("Invalid command encountered for: sort b", tb.processUserCommand("sort b"));
	}
	
	@Test
	public void testSort() {
		TextBuddy tb = new TextBuddy("mytextfile.txt");
		assertEquals("added to mytextfile.txt: \"zzz\"", tb.processUserCommand("add zzz"));
		assertEquals("added to mytextfile.txt: \"yyy\"", tb.processUserCommand("add yyy"));
		assertEquals("added to mytextfile.txt: \"www\"", tb.processUserCommand("add www"));
		assertEquals("added to mytextfile.txt: \"wwa\"", tb.processUserCommand("add wwa"));
		assertEquals("added to mytextfile.txt: \"ww\"", tb.processUserCommand("add ww"));
		assertEquals("added to mytextfile.txt: \"bbb\"", tb.processUserCommand("add bbb"));
		assertEquals("added to mytextfile.txt: \"a\"", tb.processUserCommand("add a"));
		assertEquals("added to mytextfile.txt: \"tomorrow\"", tb.processUserCommand("add tomorrow"));
		assertEquals("mytextfile.txt is sorted", tb.processUserCommand("sort"));
		assertEquals("1. a\n2. bbb\n3. tomorrow\n4. ww\n5. wwa\n6. www\n7. yyy\n8. zzz", tb.processUserCommand("display"));
		assertEquals("all content deleted from mytextfile.txt", tb.processUserCommand("clear"));
		assertEquals("mytextfile.txt is empty", tb.processUserCommand("sort"));
		assertEquals("mytextfile.txt is empty", tb.processUserCommand("display"));
	}
}
