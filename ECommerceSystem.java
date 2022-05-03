import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/*
 * Models a simple ECommerce system. Keeps track of products for sale, registered customers, product orders and
 * orders that have been shipped to a customer
 */
public class ECommerceSystem
{
	ArrayList<Product>  tempproducts = new ArrayList<Product>(); // Temp arraylist that holds products from the .txt to be transfered to the map / Also will be sorted and printed for the 2 sort functions
	Map<String,Product> products = new LinkedHashMap<String,Product>(); // New product map that holds all product available
	Map<Product,Integer> statistics = new HashMap<Product,Integer>(); // New map to hold the statistics of orders for each product
	ArrayList<Customer> customers = new ArrayList<Customer>();	

	ArrayList<ProductOrder> orders = new ArrayList<ProductOrder>();
	ArrayList<ProductOrder> shippedOrders = new ArrayList<ProductOrder>();

	// These variables are used to generate order numbers, customer id's, product id's 
	int orderNumber = 500;
	int customerId = 900;
	int productId = 700;

	// General variable used to store an error message when something is invalid (e.g. customer id does not exist)  
	String errMsg = null;

	// Random number generator
	Random random = new Random();

	public ECommerceSystem()
	{
		// NOTE: do not modify or add to these objects!! - the TAs will use for testing
		// If you do the class Shoes bonus, you may add shoe products

		// Create some products
		tempproducts = getProductsFromFile("products.txt"); //FILE I/O, gathers products from txt into arraylist
		
		//Adding shoe products for testing and usage in the system(As they are not in the .txt file)
		//int[][] stockCounts = {{4, 2}, {3, 5}, {1, 4,}, {2, 3}, {4, 2}};
		//tempproducts.add(new Shoes("Prada", generateProductId(), 595.0, stockCounts));
		
		transferListMap(); //Moves all information from arraylist to map
		
		statsbegin(); //Begins the statistics of all the products
		
		// Create some customers
		customers.add(new Customer(generateCustomerId(),"Inigo Montoya", "1 SwordMaker Lane, Florin"));
		customers.add(new Customer(generateCustomerId(),"Prince Humperdinck", "The Castle, Florin"));
		customers.add(new Customer(generateCustomerId(),"Andy Dufresne", "Shawshank Prison, Maine"));
		customers.add(new Customer(generateCustomerId(),"Ferris Bueller", "4160 Country Club Drive, Long Beach"));
	}
	
	//Sets the statistics of all the products to 0
	private void statsbegin() {
		for(Product p: products.values()) {
			statistics.put(p, 0);
		}
	}
	
	//Increases the stats of the product by 1
	private void statsIncre(Product p) {
		statistics.put(p, statistics.get(p) + 1);
	}
	
	//This method will be reached by the interface and will help print the statistics in order by using arraylists
	public void sortAndPrintStats() {
		//Converting set of keys into an arraylist
		Set<Product> keySet = statistics.keySet();
		ArrayList<Product>  P = new ArrayList<Product>(keySet);
		
		//Converting a collection of values to arraylist
		Collection<Integer> values = statistics.values(); 
		ArrayList<Integer>  s = new ArrayList<Integer>(values);
		
		//Holds the sorted arraylist of keys to be printed in this order
		ArrayList<Product>  sortedP = new ArrayList<Product>();
		
		//Loops the whole value arraylist to figure which product has the highest order
		while(!s.isEmpty()) {
			//default highest value index
			int largeIndex = s.size() - 1;
			
			//Search through the arraylist to find largest value's index
			for(int i = s.size() - 2; i >= 0 ; i--) {
				if(s.get(largeIndex) < s.get(i)) {
					largeIndex = i;
				}
			}
			//adds the product to the sorted arraylist
			sortedP.add(P.get(largeIndex));
			//removes index from the two base arraylist as they have no further purpose
			P.remove(largeIndex);
			s.remove(largeIndex);
		}
		
		//Prints the statistics
		printStats(sortedP);
	}
	
	//Prints Stats for all products based on a sorted arralist of products
	public void printStats(ArrayList<Product>  sorted) {
		for(Product p: sorted) {
			//Book gets a special print statement as all books have the same product name and it would get confusing if I did not use
			//titles instead
			if(p.getCategory() == Product.Category.BOOKS) {
				Book temp = (Book) p;
				System.out.printf("\nId: %-5s Product: %-30s # of Orders: %-3d", p.getId(), temp.getTitle(), statistics.get(p));		

			}
			else {
				System.out.printf("\nId: %-5s Product: %-30s # of Orders: %-3d", p.getId(), p.getName(), statistics.get(p));	

			}
		}
	}
	/*
	 * Given a filename, the method will try/catch all the products within the .txt and return a arraylist of all products
	 * If there is an IO error it will print the stack trace and exit
	 */
	private ArrayList<Product> getProductsFromFile(String filename){
		ArrayList<Product> temp = new ArrayList<Product>(); //Temp arraylist to hold products
		File input = new File(filename); //opens
		try {
			Scanner in = new Scanner(input);
			//While there are lines left in the .txt continue to get all products
			while(in.hasNextLine()) {
				String[] lines = new String[5]; //Array to hold all the product information
				
				//loops 5 times to get all info on the product as each product takes 5 lines
				for(int i = 0; i < 5; i++) {
					lines[i] = in.nextLine();
				}
				
				//if and else to distinguish if the product is a book or other
				if(lines[0].equals("BOOKS")) {
					//initialize variables for books
					int hard = 0;
					int paper = 0;
					int year = 0;
					String title;
					String author;
					String[] stocks = lines[3].split(" "); //Splits the lines rather than using delimiter
					hard = Integer. parseInt(stocks[0]);
					paper = Integer. parseInt(stocks[1]);
					
					String[] info = lines[4].split(":");//Splits the lines rather than using delimiter
					title = info[0];
					author = info[1];
					year = Integer. parseInt(info[2]);
					//add book to temp arraylist
					temp.add(new Book(lines[1], generateProductId(), Double.parseDouble(lines[2]), paper, hard, title, author, year));
				}
				else {
					//get category using the .valueOf(String x)
					Product.Category i = Product.Category.valueOf(lines[0]);
					//add product to temp arraylist
					temp.add(new Product(lines[1], generateProductId(), Double.parseDouble(lines[2]), Integer. parseInt(lines[3]), i));
				}
			}
		in.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		//returns arraylist
		return temp;
		
	}
	
	//Transfer information from arraylist to map
	private void transferListMap() {
		for (Product p : tempproducts)
		{
			products.put(p.getId(), p);
		}
	}
	private String generateOrderNumber()
	{
		return "" + orderNumber++;
	}

	private String generateCustomerId()
	{
		return "" + customerId++;
	}

	private String generateProductId()
	{
		return "" + productId++;
	}

	public String getErrorMessage()
	{
		return errMsg;
	}

	public void printAllProducts()
	{

		for (Product p : products.values()) {
			p.print();
		}
	}

	public void printAllBooks()
	{
		for (Product p : products.values())
		{
			if (p.getCategory() == Product.Category.BOOKS)
				p.print();
		}
	}

	public ArrayList<Book> booksByAuthor(String author)
	{
		ArrayList<Book> books = new ArrayList<Book>();
		for (Product p : products.values())
		{
			if (p.getCategory() == Product.Category.BOOKS)
			{
				Book book = (Book) p;
				if (book.getAuthor().equals(author))
					books.add(book);
			}
		}
		return books;
	}

	public void printAllOrders()
	{
		for (ProductOrder o : orders)
			o.print();
	}

	public void printAllShippedOrders()
	{
		for (ProductOrder o : shippedOrders)
			o.print();
	}

	public void printCustomers()
	{
		for (Customer c : customers)
			c.print();
	}
	/*
	 * Given a customer id, print all the current orders and shipped orders for them (if any)
	 */
	public void printOrderHistory(String customerId)
	{
		// Make sure customer exists
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1)
		{
			throw new UnknownCustomerException(customerId);

		}	
		System.out.println("Current Orders of Customer " + customerId);
		for (ProductOrder order: orders)
		{
			if (order.getCustomer().getId().equals(customerId))
				order.print();
		}
		System.out.println("\nShipped Orders of Customer " + customerId);
		for (ProductOrder order: shippedOrders)
		{
			if (order.getCustomer().getId().equals(customerId))
				order.print();
		}
	}
	
	
	/*
	 * Given a customer id, print all the current products in their cart
	 */
	public void printCart(String customerId)
	{
		// Make sure customer exists
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1)
		{
			throw new UnknownCustomerException(customerId);
		}	
		
		System.out.println("Current Cart of Customer " + customerId);
		Customer customer = customers.get(index);
		customer.custCart.printCart();
	}
	
	/*
	 * Given a customer id, product id and product option add reference to object and options to the cart
	 */
	public void addCart(String customerId, String productId, String productOptions)
	{
		// Make sure customer exists
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1)
		{
			throw new UnknownCustomerException(customerId);

		}	
		Customer customer = customers.get(index);

		// Get product 
		if (!products.containsKey(productId))
		{
			throw new UnknownProductException(productId);

		}
		Product product = products.get(productId);
		
		// Check if the options are valid for this product (e.g. Paperback or Hardcover or EBook for Book product)
		if (!product.validOptions(productOptions))
		{
			throw new InvalidProductOptionException(product, productOptions);
		}
		// Is it in stock?
		if (product.getStockCount(productOptions) == 0)
		{
			throw new ProductStockException(product);
		}
		//reduce stockcount(This is to avoid an error if two customer order the last product)
		product.reduceStockCount(productOptions);
		
		//Adds the product and options to the customer's cart object
		customer.custCart.addToCart(product, productOptions);
		
	}
	
	/*
	 * Given a customer id and product id remove said product from cart
	 */
	public void removeCart(String customerId, String productId)
	{
		// Make sure customer exists
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1)
		{
			throw new UnknownCustomerException(customerId);

		}	
		Customer customer = customers.get(index);
		// Get product 
		if (!products.containsKey(productId))
		{
			throw new UnknownProductException(productId);

		}
		Product product = products.get(productId);	
		//Makes sure the cart has said product
		index = customer.custCart.getProducts().indexOf(new CartItem(product,""));
		if (index == -1)
		{
			throw new ProductCartException(productId, customerId);
		}
		
		customer.custCart.removeFromCart(product);
	}
	
	public String cartOrder(String customerId)
	{
		// Get customer
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1)
		{
			throw new UnknownCustomerException(customerId);
		}
		Customer customer = customers.get(index);
		ArrayList<CartItem>  customerCart = customer.custCart.products;
		ArrayList<Product>  p = new ArrayList<Product>();
		
		for(int i = 0; i < customer.custCart.getSize(); i++) {
			Product product = customerCart.get(i).getObject();
			String productOptions = customerCart.get(i).getProductOptions();
			
			// Create a ProductOrder
			ProductOrder order = new ProductOrder(generateOrderNumber(), product, customer, productOptions);
	
			// Add to orders and return
			orders.add(order);
			p.add(product);
			// Stats
			statsIncre(product);
		}
		//Clears cart
		customer.custCart.clearCart();
		return "Ordered all cart items";
	}

	public String orderProduct(String productId, String customerId, String productOptions)
	{
		// Get customer
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1)
		{
			throw new UnknownCustomerException(customerId);

		}
		Customer customer = customers.get(index);

		// Get product 
		if (!products.containsKey(productId))
		{
			throw new UnknownProductException(productId);

		}
		Product product = products.get(productId);

		// Check if the options are valid for this product (e.g. Paperback or Hardcover or EBook for Book product)
		if (!product.validOptions(productOptions))
		{
			throw new InvalidProductOptionException(product, productOptions);

		}
		// Is it in stock?
		if (product.getStockCount(productOptions) == 0)
		{
			throw new ProductStockException(product);

		}
		// Create a ProductOrder
		ProductOrder order = new ProductOrder(generateOrderNumber(), product, customer, productOptions);
		product.reduceStockCount(productOptions);

		// Add to orders and return
		orders.add(order);
		
		// Stats
		statsIncre(product);
		return order.getOrderNumber();
	}

	/*
	 * Create a new Customer object and add it to the list of customers
	 */

	public void createCustomer(String name, String address)
	{
		// Check to ensure name is valid
		if (name == null || name.equals(""))
		{
			throw new InvalidCustNameException(name);
		}
		// Check to ensure address is valid
		if (address == null || address.equals(""))
		{
			throw new InvalidCustAdressException(address);
		}
		Customer customer = new Customer(generateCustomerId(), name, address);
		customers.add(customer);
	}

	public ProductOrder shipOrder(String orderNumber)
	{
		// Check if order number exists
		int index = orders.indexOf(new ProductOrder(orderNumber,null,null,""));
		if (index == -1)
		{
			throw new UnknownOrderException(orderNumber);
		}
		ProductOrder order = orders.get(index);
		orders.remove(index);
		shippedOrders.add(order);
		return order;
	}

	/*
	 * Cancel a specific order based on order number
	 */
	public void cancelOrder(String orderNumber)
	{
		// Check if order number exists
		int index = orders.indexOf(new ProductOrder(orderNumber,null,null,""));
		if (index == -1)
		{
			throw new UnknownOrderException(orderNumber);

		}
		ProductOrder order = orders.get(index);
		orders.remove(index);
			
	}
	
	
	// Sort products by increasing price
	public void sortByPrice()
	{
		
		Collections.sort(tempproducts, new PriceComparator());
		for(Product p: tempproducts) {
			p.print();
		}
	}

	private class PriceComparator implements Comparator<Product>
	{
		public int compare(Product a, Product b)
		{
			if (a.getPrice() > b.getPrice()) return 1;
			if (a.getPrice() < b.getPrice()) return -1;	
			return 0;
		}
	}
	
	// Sort products alphabetically by product name
	public void sortByName()
	{
		Collections.sort(tempproducts, new NameComparator());
		for(Product p: tempproducts) {
			p.print();
		}
	}

	private class NameComparator implements Comparator<Product>
	{
		public int compare(Product a, Product b)
		{
			return a.getName().compareTo(b.getName());
		}
	}
	
	// Sort products alphabetically by product name
	public void sortCustomersByName()
	{
		Collections.sort(customers);
	}
}
/*
 * ALL THE EXCEPTIONS
 */
/*
 * Creates an exceptions that extends runtimeexception for invalid customer ID
 */
class UnknownCustomerException extends RuntimeException{
	public UnknownCustomerException(String id) {
		super("Customer " + id + " Not Found");
	}
}

/*
 * Creates an exceptions that extends runtimeexception for invalid product ID
 */
class UnknownProductException extends RuntimeException{
	public UnknownProductException(String id) {
		super("Product " + id + " Not Found");
	}
}

/*
 * Creates an exceptions that extends runtimeexception for invalid order ID
 */
class UnknownOrderException extends RuntimeException{	
	public UnknownOrderException(String id) {
		super("Order " + id + " Not Found");	
	}
	
}

/*
 * Creates an exceptions that extends runtimeexception for ordering a product that is out of stock
 */
class ProductStockException extends RuntimeException{
	public ProductStockException(Product p) {
		super("Product " + p.getName() + " ProductId " + p.getId() + " Out of Stock");
	}
}

/*
 * Creates an exceptions that extends runtimeexception for removing a product ID that is not in the cart
 */
class ProductCartException extends RuntimeException{
	public ProductCartException(String pId, String cId) {
		super("product " + pId + " Not Found in " + "Customer " + cId + "'s cart");
	}
}

/*
 * Creates an exceptions that extends runtimeexception for invalid product options
 */
class InvalidProductOptionException extends RuntimeException{

	public InvalidProductOptionException(Product p, String options) {
		super("Product " + p.getName() + " ProductId " + p.getId() + " Invalid Options: " + options);
	}
}

/*
 * Creates an exceptions that extends runtimeexception for invalid customer name
 */
class InvalidCustNameException extends RuntimeException{
	public InvalidCustNameException (String name) {
		super("Invalid Customer Name " + name);
	}
}

/*
 * Creates an exceptions that extends runtimeexception for invalid customer adress
 */
class InvalidCustAdressException extends RuntimeException{
	public InvalidCustAdressException(String adress) {
		super("Invalid Customer Adress " + adress);
	}
}