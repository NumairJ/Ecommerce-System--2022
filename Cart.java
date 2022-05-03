import java.util.ArrayList;
/*
 * Creates a Cart object that manages all items in the cart from adding and removing objects
 */
public class Cart {
	ArrayList<CartItem>  products = new ArrayList<CartItem>(); //Arraylist of Cartitems
	
	//Creates the Cart Object
	public Cart()
	{
	}
	
	//adds product and product options into the cartItem arraylist
	public void addToCart(Product object, String options) {
		products.add(new CartItem(object, options));
	}
	
	//Removes cartitem based on product
	public void removeFromCart(Product object) {
		int index = products.indexOf(new CartItem(object,""));//Finds index of product
		CartItem order = products.get(index);
		products.remove(index);
	}
	
	//Print all products in the cart
	public void printCart() {
		for(CartItem p: products) {
			p.print();
		}
	}
	
	//Clears the arraylist
	public void clearCart() {
		products.clear();
	}
	
	//Returns the size of the arraylist
	public int getSize() {
		return products.size();
	}
	
	//returns an arraylist of all cartitems
	public ArrayList<CartItem> getProducts(){
		return products;
	}
}
