/*
 * Creates an cart item object that has the product and its product option
 */
public class CartItem {
	private Product object;
	private String productOptions;
	
	//Creates cartitem
	public CartItem(Product object, String productOptions)
	{
		this.object = object;
		this.productOptions = productOptions;
	}
	
	//Returns the product object
	public Product getObject() {
		return object;
	}
	
	//Sets the Product object
	public void setObject(Product object) {
		this.object = object;
	}
	
	//Gets product options
	public String getProductOptions() {
		return productOptions;
	}
	
	//Sets product options
	public void setProductOptions(String productOptions) {
		this.productOptions = productOptions;
	}
	
	//Prints the cart item
	public void print() {
		System.out.print("Product:");
		object.print();
		System.out.println(" Product Options: " + productOptions);
	}
	
	//Determines if the two cart items are equal
	public boolean equals(Object other)
	{
		CartItem otherC = (CartItem) other;
		return this.object.equals(otherC.object);
	}
}
